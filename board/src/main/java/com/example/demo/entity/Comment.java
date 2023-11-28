package com.example.demo.entity;

import com.example.demo.DTO.CommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String contents;

    // 1 : 다 연관 관계
    // 지연로딩
    // 연관관계 생성 시 넣어질 이름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String contents, Board board) {
        this.id = id;
        this.contents = contents;
        this.board = board;
    }
}
