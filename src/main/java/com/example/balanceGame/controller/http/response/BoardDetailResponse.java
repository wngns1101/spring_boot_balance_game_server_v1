package com.example.balanceGame.controller.http.response;

import com.example.balanceGame.dto.BoardDetailDto;
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
    private String message;
    private Long boardKey;
    private String userName;
    private String boardTitle;
    private String leftContent;
    private String rightContent;
    private int leftCount;
    private int rightCount;
    private Long heartCount;
    private List<CommentDto> commentList;

    public BoardDetailResponse(BoardDetailDto boardHeartDto, List<CommentDto> comment) {
        this.boardKey = boardHeartDto.getBoardKey();
        this.userName = boardHeartDto.getUserName();
        this.boardTitle = boardHeartDto.getBoardTitle();
        this.leftContent = boardHeartDto.getLeftContent();
        this.rightContent = boardHeartDto.getRightContent();
        this.leftCount = boardHeartDto.getLeftCount();
        this.rightCount = boardHeartDto.getRightCount();
        this.heartCount = boardHeartDto.getHeartCount();
        this.commentList = comment;
    }
}
