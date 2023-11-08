package com.example.balanceGame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardHeartDto {
    private String boardTitle;
    private String leftContent;
    private String rightContent;
    private int leftCount;
    private int rightCount;
    private long heartCount;
}
