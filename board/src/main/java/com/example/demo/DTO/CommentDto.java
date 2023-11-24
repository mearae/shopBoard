package com.example.demo.DTO;

import com.example.demo.entity.Board;
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

    public Comment toEntitiy(){
        return Comment.builder()
                .id(id)
                .contents(contents)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                null,
                comment.getContents(),
                comment.getBoard().getId());
    }
}
