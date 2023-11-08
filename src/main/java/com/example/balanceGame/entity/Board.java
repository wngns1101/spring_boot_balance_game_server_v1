package com.example.balanceGame.entity;

import com.example.balanceGame.request.BoardRegistRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_key")
    private long boardKey;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private User user;

    // 좋아요 수를 위한 외래키
    // 초기 생성에는 default 좋아요 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heart_key")
    private Heart like;

    // 댓글
    // 초기 생성에는 default 좋아요 0
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_key")
    private Comment comment;

    // 제목 ex) 짜장면 짬뽕 중 더 좋은 것은?
    private String boardTitle;

    // 왼쪽 답안 ex) 짜장면
    private String leftContent;

    // 오른쪽 답안 ex) 짬뽕
    private String rightContent;

    // 왼쪽을 누른 count
    private int leftCount = 0;

    // 오른쪽을 누른 count
    private int rightCount = 0;

    // 작성 시간
    private LocalDateTime boardDate;

    public Board(BoardRegistRequest boardRegistRequest, User user) {
        this.user = user;
        this.boardTitle = boardRegistRequest.getBoardTitle();
        this.leftContent = boardRegistRequest.getLeftContent();
        this.rightContent = boardRegistRequest.getRightContent();
        this.boardDate = LocalDateTime.now();
    }


    public static Board createBoard(BoardRegistRequest boardRegistRequest, User user) {
        return new Board(boardRegistRequest, user);
    }
}
