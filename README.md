# shopBoard

#### 2023.11.21
- 게시판에 글을 등록
- createBoard.html 파일을 이용해 작성자와 작성내용을 post로 보냄
- 작성시간은 자동으로 작성 당시의 현재시간으로 고정되도록 설정
- 수정시간은 매번 글을 바꿀 때마다 시간이 바뀌도록 준비

#### 2023.11.22
- id를 Long으로 수정
- DTO에 id 추가
- 페이징 기법 학습
    - Pageable 객체
    - pageable.getPageNumber() - 1; -> 시작페이지 1로
    - repository.findAll(PageRequest.of(page, size)) -> 한 페이지 당 게시물 개수, 출현하는 페이지 번호 개수
    - @PageableDefault(page = 1) -> 받아오는 Pageable의 첫 번호를 1로
    - Model에 게시물을 등록해 가상의 Json 파일 생성 -> html에서 사용
- 목록을 보여줄 paging.html 파일 생성
- index.html로 게시판 첫 화면 설정 (글쓰기 버튼)
  - Model에 저장한 boardList라는 Page 넣음
  - 게시물의 id, 제목(링크), 작성날짜가 나옴
```html
<tr th:each="board: ${boardList}">
    <td th:text="${board.getId}"></td>
    <td> <a th:href="@{${board.getId}|(page = ${boardList.number + 1})}" th:text="${board.getTitle}"></a></td>
    <td th:text="*{#temporals.format(board.getCreateTime, 'yyyy-MM-dd HH:mm:ss')}"></td>
</tr>
```
  - 페이지 '1번'으로 돌아가는 링크
  - 페이지 1번인지 확인하고 '이전 페이지'로 옮기는 링크
```html
<a th:href="@{/paging(page=1)}">처음</a>
<a th:href="${boardList.first} ? '#' : @{/paging(page=${boardList.number})}">이전</a>
```
  - 페이지를 3개씩 보여줌 -> sequence로 배열을 page에 넣음
  - 현재 페이지의 링크는 링크 없이 텍스트만 설정
  - 이외의 페이지는 /paging으로 page값을 매개변수로 get 요청 -> 페이지 이동
```html
<span th:each="page: ${#numbers.sequence(startPage, endPage)}">
    <!-- 현재페이지는 링크 없이 숫자만 -->
    <span th:if="${page == boardList.number + 1}" th:text="${page}"></span>
    <!-- 현재페이지 번호가 아닌 다른 페이지번호에는 링크를 보여줌 -->
    <span th:unless="${page == boardList.number + 1}">
        <a th:href="@{/paging(page=${page})}" th:text="${page}"></a>
    </span>
</span>
```
  - 마지막 페이지인지 확인하고 다음 페이지로 이동하는 링크
  - 총 페이지 수를 이용해 마지막 페이지로 이동하는 링크
```html
<!-- 다음 링크 활성화 비활성화
    사용자: 2페이지, getNumber: 1, 3페이지-->
<a th:href="${boardList.last} ? '#' : @{/paging(page=${boardList.number + 2})}">다음</a>
<!-- 마지막 페이지로 이동 -->
<a th:href="@{/paging(page=${boardList.totalPages})}">마지막</a>
```

#### 2023.11.23
- 게시판과 Home의 구분을 위해 HomeController 생성
- 개발 편의성을 위해 DTO에 Board를 DTO로 변경하는 함수 추가
- /board/~ 로 링크 수정
- createBoard.html을 create.html로 변경
- 게시물 보기 작성
  - @GetMapping("/{id}") 과 @PathVariable Long id -> 게시물 번호로 링크 작성
  - model에 board(게시물), page(페이지 번호) 등록
  - detail.html 작성(목록, 수정, 삭제 버튼)
- 게시물 수정 작성
  - update.html 작성
  - 먼저 /update/{id} 링크로 해당 id의 게시물을 model에 넣음 -> /update 링크로 변경된 게시물 제목과 내용을 저장 -> 페이지 목록 화면(/board/)으로 이동
- 게시물 삭제 작성
  - /delete/{id} 링크로 해당 id의 게시물을 삭제 -> 페이지 목록 화면(/board/paging)으로 이동

#### 2023.11.24
- 게시물과 댓글의 연관관계 생성
  - '1:다' 연관 관계
    - Board : @OneToMany(mappedBy = "board", ...)
    - Comment : @ManyToOne(...)
  - 소유(1)와 비소유(다)(여기에서 확인)
  - cascade = CascadeType.REMOVE : 소유자(게시물)이 삭제될 경우 그 소유물(댓글)이 자동 삭제
  - orphanRemoval = true : 만약 연결 관계가 끊어지면 삭제
  - fetch = FetchType.LAZY : 지연로딩 (성능 최적화)
  - @JoinColumn(name = "board_id") : 연관관계 생성 시 넣어질 이름
- Comment 객체로 댓글 Entity 생성
- 댓글 저장 구현 -> detail.html에 댓글들을 보여주는 함수 구현

#### 2023.11.17
- 게시물에 파일 등록하기
  - id, 파일 경로, 파일 이름, 파일 포멧, 파일 크기, 게시물과 연관관계(1:다)
- File 객체로 첨부 파일 Entity 생성
- 게시물 저장 Controller에 MultipartFile[] files 매개변수 추가
```java
@PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto,
                       @RequestParam MultipartFile[] files) throws IOException {
        boardService.save(boardDto, files);

        return "redirect:/board/paging";
    }
```
- 게시물 저장 Service에 아래 두 함수 수정 및 추가
```java
@Transactional
    public void save(BoardDto boardDto, MultipartFile[] files) throws IOException{
        boardDto.setCreateTime(LocalDateTime.now());
        Board idBoard = boardRepository.save(boardDto.toEntity());

        // 추가
        for (MultipartFile file : files){
            String[] fileNames = createFilePath(file);
            FileDto fileDto = new FileDto();
            fileDto.setFilePath(filePath);
            fileDto.setFileName(fileNames[0]);
            fileDto.setFileType(fileNames[1]);
            fileDto.setFileSize(file.getSize());
            File idFile = fileRepository.save(fileDto.toEntity());

            idFile.updateFromBoard(idBoard);
            fileRepository.save(idFile);
            idBoard.updateFromFile(FileDto.toFileDto(idFile));
        }
    }
```
```java
    // 추가
    private String[] createFilePath(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(filePath);

        // 만약 경로가 없다면... 경로 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일명 추출
        String originalFilename = file.getOriginalFilename();

        int split_idx = originalFilename.lastIndexOf(".");

        String fileName = originalFilename.substring(0, split_idx);
        // 확장자 추출
        String formatType = originalFilename.substring(split_idx);

        // UUID 생성
        String uuid = UUID.randomUUID().toString();

        Path path = uploadPath.resolve(
                uuid + originalFilename + formatType);

        Files.copy(file.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING);

        return new String[] {fileName, formatType};
    }
```

#### 2023.11.28
- Dto가 아닌 Entity 중심으로 코드를 적어야 함
- DB에 저장하지 않아야 할 데이터는 Entity에 있으면 안 되지만 사용할 때 필요할 것 같은 데이터는 Dto에 저장
- File Entity 수정 및 관련 함수 수정
  - uuid/파일명
  - uuid 속성 추가
  - 피일 저장 방법 일부 수정
  - 게시물 저장할 때 model에 files라는 이름으로 첨부파일 전달
- detail.html에서 첨부파일을 다운로드 할 수 있도록 링크 추가

- 일부 개인적인 변경사항
  - 적은 댓글들이 항상 나올 수 있도록 수정
  - 게시물 수정 시 제목, 내용, 첨부파일을 전부 지우고 새로 작성할 수 있도록 수정
