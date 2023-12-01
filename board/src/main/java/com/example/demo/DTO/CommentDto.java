package com.example.demo.DTO;

import com.example.demo.entity.Comment;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String writer;

    private String contents;

    private Long boardId;

    private Long userId;

    public Comment toEntity(){
        return Comment.builder()
                .id(id)
                .contents(contents)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getUser().getName(),
                comment.getContents(),
                comment.getBoard().getId(),
                comment.getUser().getId());
    }
}
