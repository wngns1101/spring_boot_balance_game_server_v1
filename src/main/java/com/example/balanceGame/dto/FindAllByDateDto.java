package com.example.balanceGame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByDateDto {
    private long boardKey;
    private String userName;
    private LocalDateTime boardDate;
    private String boardTitle;
    private String leftContent;
    private String rightContent;
}
