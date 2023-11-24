package com.example.balanceGame.controller.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportRequest {
    private Long boardKey;
    private String reportTitle;
    private String reportContent;
}
