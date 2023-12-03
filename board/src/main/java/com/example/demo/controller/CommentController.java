package com.example.demo.controller;

import com.example.demo.DTO.BoardDto;
import com.example.demo.DTO.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentDto> save(@ModelAttribute CommentDto commentDto){
        Comment comment = commentService.save(commentDto);

        if (comment != null){
            return ResponseEntity.ok().body(commentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        commentService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/update")
    public ResponseEntity update(@ModelAttribute CommentDto commentDto){
        commentService.update(commentDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> commentList(@ModelAttribute BoardDto boardDto){
        Long boardId = boardDto.getId();
        List<CommentDto> comments = commentService.commentList(boardId);

        return ResponseEntity.ok().body(comments);
    }
}
