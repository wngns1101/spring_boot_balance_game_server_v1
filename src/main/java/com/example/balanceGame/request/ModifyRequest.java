package com.example.balanceGame.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ModifyRequest {
    // 아이디
    private String userId;

    // 이름
    private String userName;

    // 이메일
    private String userEmail;
}
