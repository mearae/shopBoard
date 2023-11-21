package com.example.demo.sevice;

import com.example.demo.DTO.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void saveBoard(BoardDto boardDto){
        Board board = boardDto.toEntity();
        boardRepository.save(boardDto.toEntity());
    }
}
