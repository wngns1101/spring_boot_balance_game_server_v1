package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardGameStartRequest {
    private Long boardKey;

    private String result;
}
