package com.example.demo.sevice;

import com.example.demo.DTO.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            Comment comment = commentDto.toEntity();
            Board board = optionalBoard.get();
            Hibernate.initialize(board.getComments());
            Hibernate.initialize(board.getFiles());

            comment.updateFromBoard(board);
            Comment idComment = commentRepository.save(comment);
            board.updateFromComment(CommentDto.toCommentDto(idComment));
            return idComment;
        } else {
            return null;
        }
    }

    public List<CommentDto> commentList(Long id) {
        List<Comment> comments = commentRepository.findAllByBoard_id(id);
        for (Comment c : comments){
            System.out.println("aaaaa : " + c.getId());
        }
        return null;
    }
}
