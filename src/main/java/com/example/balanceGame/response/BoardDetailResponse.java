package com.example.balanceGame.response;

import com.example.balanceGame.dto.BoardHeartDto;
import com.example.balanceGame.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {
    private String boardTitle;
    private String leftContent;
    private String rightContent;
    private int leftCount;
    private int rightCount;
    private long heartCount;
    private List<CommentDto> commentList;

    public BoardDetailResponse(BoardHeartDto boardHeartDto, List<CommentDto> comment) {
        this.boardTitle = boardHeartDto.getBoardTitle();
        this.leftContent = boardHeartDto.getLeftContent();
        this.rightContent = boardHeartDto.getRightContent();
        this.leftCount = boardHeartDto.getLeftCount();
        this.rightCount = boardHeartDto.getRightCount();
        this.heartCount = boardHeartDto.getHeartCount();
        this.commentList = comment;
    }
}
