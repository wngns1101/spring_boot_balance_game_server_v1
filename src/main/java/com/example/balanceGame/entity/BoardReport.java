package com.example.balanceGame.entity;

import com.example.balanceGame.controller.http.request.BoardReportRequest;
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
public class BoardReport {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_key")
    private Long reportKey;

    // 게시글 신고자
    private Long userKey;

    // 신고할 게시글
    private Long boardKey;

    // 신고 제목
    private String reportTitle;

    // 신고 내역
    private String reportContent;

    // 신고 날짜
    private LocalDateTime reportDate;
}
