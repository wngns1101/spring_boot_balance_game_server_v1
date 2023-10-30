package com.example.balanceGame.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Heart {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_key")
    private long heartKey;

    // 좋야요 누른 사용자
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    // 좋아요 게시물
    @ManyToOne
    @JoinColumn(name = "board_key")
    private Board board;

}
