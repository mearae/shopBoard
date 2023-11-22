# shopBoard

##### 2023.11.21
- 게시판에 글을 등록
- createBoard.html 파일을 이용해 작성자와 작성내용을 post로 보냄
- 작성시간은 자동으로 작성 당시의 현재시간으로 고정되도록 설정
- 수정시간은 매번 글을 바꿀 때마다 시간이 바뀌도록 준비

#### 2023.11.22
- id를 Long으로 수정
- DTO에 id 추가
- 페이징 기법 학습
- 목록을 보여줄 paging.html 파일 생성
- index.html로 게시판 첫 화면 설정 (글쓰기 버튼)
```html
<tr th:each="board: ${boardList}">
    <td th:text="${board.getId}"></td>
    <td> <a th:href="@{${board.getId}|(page = ${boardList.number + 1})}" th:text="${board.getTitle}"></a></td>
    <td th:text="*{#temporals.format(board.getCreateTime, 'yyyy-MM-dd HH:mm:ss')}"></td>
</tr>
```
- Model에 저장한 boardList라는 Page 넣음
- 게시물의 id, 제목(링크), 작성날짜가 나옴
```html
<a th:href="@{/paging(page=1)}">처음</a>
<a th:href="${boardList.first} ? '#' : @{/paging(page=${boardList.number})}">이전</a>
```
- 페이지 1번으로 돌아가는 링크
- 페이지 1번인지 확인하고 이전 페이지로 옮기는 링크
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
- 페이지를 3개씩 보여줌 -> sequence로 배열을 page에 넣음
- 현재 페이지의 링크는 링크 없이 텍스트만 설정
- 이외의 페이지는 /paging으로 page값을 매개변수로 get 요청 -> 페이지 이동
```html
<!-- 다음 링크 활성화 비활성화
    사용자: 2페이지, getNumber: 1, 3페이지-->
<a th:href="${boardList.last} ? '#' : @{/paging(page=${boardList.number + 2})}">다음</a>
<!-- 마지막 페이지로 이동 -->
<a th:href="@{/paging(page=${boardList.totalPages})}">마지막</a>
```
- 마지막 페이지인지 확인하고 다음 페이지로 이동하는 링크
- 총 페이지 수를 이용해 마지막 페이지로 이동하는 링크
