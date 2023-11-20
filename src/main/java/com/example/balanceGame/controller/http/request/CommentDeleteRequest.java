package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteRequest {
    private Long boardKey;
    private Long commentKey;
}
