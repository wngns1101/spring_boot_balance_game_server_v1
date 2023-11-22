package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameDeleteRequest {
    private Long boardKey; // 게시글 번호
}
