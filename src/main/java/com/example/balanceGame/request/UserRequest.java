package com.example.balanceGame.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserRequest {
    // 이름
    private String userName;

    // 비밀번호
    private String userPw;

    // 이메일
    private String userEmail;
}
