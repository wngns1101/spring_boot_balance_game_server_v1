package com.example.balanceGame.entity;

import com.example.balanceGame.request.ModifyRequest;
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
public class User{
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    private long userKey;

    // 아이디
    private String userId;

    // 비밀번호
    private String userPw;

    // 이름
    private String userName;

    // 이메일
    private String userEmail;

    // 계정 상태 ex) 0 현재 사용, 1 휴먼
    private boolean userStatus;

    // 회원가입 시간
    private LocalDateTime createDate;

    // 탈퇴 시간
    private LocalDateTime deleteDate;

    // 작성한 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_key")
    private Board board;

    // 작성한 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_key")
    private BoardReport comment;

    // 신고 내역
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_key")
    private BoardReport report;

    // 유저 정보를 수정하는 비즈니스 로직
    public void modifyUser(ModifyRequest modifyRequest) {
        this.userId = modifyRequest.getUserId();
        this.userEmail = modifyRequest.getUserEmail();
        this.userName = modifyRequest.getUserName();
    }

    // 비밀번호를 수정하는 비즈니스 로직
    public void modifyPw(String modifyPw) {
        this.userPw = modifyPw;
    }
}
