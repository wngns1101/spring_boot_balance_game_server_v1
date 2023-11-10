package com.example.balanceGame.controller.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String message;
    private String token;
}
