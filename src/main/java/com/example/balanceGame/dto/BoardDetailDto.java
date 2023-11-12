package com.example.balanceGame.dto;

import com.example.balanceGame.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {
    private Long boardKey;
    private String userName;
    private String boardTitle;
    private String leftContent;
    private String rightContent;
    private int leftCount;
    private int rightCount;
    private Long heartCount;
    private List<CommentDto> comments;
}
