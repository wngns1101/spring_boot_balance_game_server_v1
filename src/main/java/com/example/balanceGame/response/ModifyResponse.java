package com.example.balanceGame.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ModifyResponse {
    private String message;
    private String token;
}
