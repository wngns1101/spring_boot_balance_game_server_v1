package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BoardModifyRequest {
    private Long boardKey;

    // 제목 ex) 짜장면 짬뽕 중 더 좋은 것은?
    private String boardTitle;

    // 왼쪽 답안 ex) 짜장면
    private String leftContent;

    // 오른쪽 답안 ex) 짬뽕
    private String rightContent;
}
