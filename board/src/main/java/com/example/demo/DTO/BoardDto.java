package com.example.demo.DTO;

import com.example.demo.entity.Board;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    // 게시물 제목
    @Column(length = 50)
    private String title;

    // 내용
    @Column(length = 50)
    private String contents;

    // 최초 작성 시간
    private LocalDateTime createTime;

    // 최근 수정 시간
    private LocalDateTime updateTime;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .contents(contents)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
