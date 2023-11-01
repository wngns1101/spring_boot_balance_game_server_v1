package com.example.balanceGame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "heart_key")
    private List<Heart> like;

    // 댓글
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_key")
    private List<Comment> comment;

    // 제목 ex) 짜장면 짬뽕 중 더 좋은 것은?
    private String boardTitle;

    // 왼쪽 답안 ex) 짜장면
    private String leftContent;

    // 오른쪽 답안 ex) 짬뽕
    private String rightContent;

    // 선택한 결과값 ex) 왼쪽은 0, 오른쪽은 1
    private boolean result;

    // 작성 시간
    private LocalDateTime boardDate;
}
