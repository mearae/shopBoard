package com.example.demo.entity;

import com.example.demo.DTO.BoardDto;
import com.example.demo.DTO.CommentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 이름
    @Column(length = 50)
    private String userName;

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

    // 1 : 다 연관 관계
    // 소유(1)와 비소유(다)(여기에서 확인)
    // cascade = CascadeType.REMOVE : 소유자(게시물)이 삭제될 경우 그 소유물(댓글)이 자동 삭제
    // orphanRemoval = true : 만약 연결 관계가 끊어지면 삭제
    // fetch = FetchType.LAZY : 지연로딩 (성능 최적화)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new LinkedList<>();

    @Builder
    public Board(Long id, String userName, String title, String contents, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void updateFromDto(BoardDto boardDto){
        // 모든 변경 사항을 셋팅
        this.title = boardDto.getTitle();
        this.contents = boardDto.getContents();
    }

    public void updateFromComment(CommentDto commentDto){
        this.comments.add(commentDto.toEntitiy());
    }
}
