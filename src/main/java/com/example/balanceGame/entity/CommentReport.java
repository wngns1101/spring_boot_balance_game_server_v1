package com.example.balanceGame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReport {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentReport_key")
    private Long reportKey;

    // 댓글 신고자
    private Long userKey;

    // 신고할 댓글
    private Long commentKey;

    // 신고 제목
    private String reportTitle;

    // 신고 내용
    private String reportContent;

    // 신고 날짜
    private LocalDateTime commentTime;
}
