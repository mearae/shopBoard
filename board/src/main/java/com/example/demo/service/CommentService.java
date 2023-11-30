package com.example.demo.service;

import com.example.demo.DTO.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment save(CommentDto commentDto) {
        Optional<Board> optionalBoard = boardRepository.findById(commentDto.getBoardId());
        if (optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            Hibernate.initialize(board.getComments());
            Hibernate.initialize(board.getBoardFiles());

            Comment comment = Comment.builder()
                    .contents(commentDto.getContents())
                    .board(board)
                    .build();
            commentRepository.save(comment);
            return comment;
        } else {
            return null;
        }
    }

    public List<CommentDto> commentList(Long id) {
        List<Comment> comments = commentRepository.findByBoard_id(id);
        List<CommentDto> commentDtos = new LinkedList<>();
        for (Comment c : comments){
            commentDtos.add(CommentDto.toCommentDto(c));
        }
        return commentDtos;
    }
}
