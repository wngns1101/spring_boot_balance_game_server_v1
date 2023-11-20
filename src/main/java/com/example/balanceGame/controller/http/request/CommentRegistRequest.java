package com.example.balanceGame.controller.http.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRegistRequest {
    private Long boardKey;
    private String commentContent;
}
