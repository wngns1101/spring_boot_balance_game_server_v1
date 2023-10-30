package com.example.balanceGame.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class BoardReport {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_key")
    private long reportKey;

    // 게시글 신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private User user;

    // 신고할 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_key")
    private Board board;

    // 신고 제목
    private String reportTitle;

    // 신고 내역
    private String reportContent;

    // 신고 날짜
    private LocalDateTime reportDate;

}
