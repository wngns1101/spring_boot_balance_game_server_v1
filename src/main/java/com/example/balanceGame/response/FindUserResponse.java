package com.example.balanceGame.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindUserResponse {
    // 응답 메시지
    private String message;

    // 아이디
    private String userId;

    // 이름
    private String userName;

    // 이메일
    private String userEmail;
}
