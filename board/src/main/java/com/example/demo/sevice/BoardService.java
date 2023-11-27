package com.example.demo.sevice;

import com.example.demo.DTO.BoardDto;
import com.example.demo.DTO.FileDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.File;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private final FileRepository fileRepository;

    // 집에서 본인 PC 이름 및 경로로 설정
    private String filePath = "C:/Users/G/Desktop/GitHub/shopBoard/board/Board Files/";

    public BoardDto findById(Long id){
        Board board = boardRepository.findById(id).get();

        return BoardDto.toBoardDto(board);
    }

    @Transactional
    public void save(BoardDto boardDto, MultipartFile[] files) throws IOException{
        boardDto.setCreateTime(LocalDateTime.now());
        Board idBoard = boardRepository.save(boardDto.toEntity());

        // 추가
        if (!files[0].isEmpty()) {
            for (MultipartFile file : files) {
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
    }

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

    @Transactional
    public void update(BoardDto boardDto, MultipartFile[] files) throws IOException {
        Optional<Board> boardOptional = boardRepository.findById(boardDto.getId());
        Board board = boardOptional.get();

        board.updateFromDto(boardDto);
        board.clearFile();
        fileRepository.deleteAllByBoard_id(board.getId());
        if (!files[0].isEmpty()) {
            for (MultipartFile file : files) {
                String[] fileNames = createFilePath(file);
                FileDto fileDto = new FileDto();
                fileDto.setFilePath(filePath);
                fileDto.setFileName(fileNames[0]);
                fileDto.setFileType(fileNames[1]);
                fileDto.setFileSize(file.getSize());
                File idFile = fileRepository.save(fileDto.toEntity());

                idFile.updateFromBoard(board);
                fileRepository.save(idFile);
                board.updateFromFile(FileDto.toFileDto(idFile));
            }
        }
        boardRepository.save(board);
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // paging용 함수
    public Page<BoardDto> paging(Pageable pageable){

        // 페이지 시작 번호 설정
        int page = pageable.getPageNumber() - 1;
        // 페이지에 호환될 게시물 개수
        int size = 5;

        // 전체 게시물 불러오기(수량별로 정렬)
        Page<Board> boards = boardRepository.findAll(PageRequest.of(page, size));

        return boards.map(board -> new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime()));
    }
}
