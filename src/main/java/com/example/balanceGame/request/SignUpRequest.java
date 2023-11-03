package com.example.balanceGame.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignUpRequest {
    // 이름
    private String userId;

    // 비밀번호
    private String userPw;

    // 이름
    private String userName;

    // 이메일
    private String userEmail;
}
