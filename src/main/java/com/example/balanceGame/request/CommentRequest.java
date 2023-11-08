package com.example.balanceGame.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    private int boardKey;
    private String commentContent;
}
