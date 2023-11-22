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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void saveBoard(BoardDto boardDto){
        boardDto.setCreateTime(LocalDateTime.now());
        boardRepository.save(boardDto.toEntity());
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
                board.getCreateTime()));
    }
}
