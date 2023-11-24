package com.example.demo.controller;

import com.example.demo.DTO.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.sevice.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<Board> save(@ModelAttribute CommentDto commentDto){

        System.out.println(commentDto);
        Comment comment = commentService.save(commentDto);

        if (comment != null){
            return new ResponseEntity<>(comment.getBoard(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comments")
    public ResponseEntity commentList(@ModelAttribute Long boardId){
        System.out.println(boardId);
        List<Comment> comments = commentService.commentList(boardId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
