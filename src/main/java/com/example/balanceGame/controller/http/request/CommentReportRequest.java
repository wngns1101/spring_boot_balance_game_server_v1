package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportRequest {
    private Long commentKey;
    private String commentTitle;
    private String commentContent;
}
