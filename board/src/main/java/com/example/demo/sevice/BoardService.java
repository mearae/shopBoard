package com.example.demo.sevice;

import com.example.demo.DTO.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDto findById(Long id){
        Board board = boardRepository.findById(id).get();

        return BoardDto.toBoardDto(board);
    }

    @Transactional
    public void save(BoardDto boardDto){
        boardDto.setCreateTime(LocalDateTime.now());
        boardRepository.save(boardDto.toEntity());
    }

    @Transactional
    public void update(BoardDto boardDto) {
        Optional<Board> boardOptional = boardRepository.findById(boardDto.getId());
        Board board = boardOptional.get();

        board.updateFromDto(boardDto);

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
