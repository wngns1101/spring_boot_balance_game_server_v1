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
public class BoardHeartHistory {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_key")
    private Long heartKey;

    // 유저 정보
    private long userKey;

    // 게시글 정보
    private long boardId;

    // 생성 시간
    private LocalDateTime localDateTime;
}
