package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.entity.BoardHeartHistory;
import com.example.balanceGame.service.BoardHeartHistoryService;
import com.example.balanceGame.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardHeartHistoryService boardHeartHistoryService;
    @Test
    public void 게시글등록테스트() {
        // given
        BoardRegistRequest boardRegistRequest = BoardRegistRequest.builder() // 게시글 작성
                .boardTitle("짜장면 짬뽕 둘 중 뭐가 좋아?")
                .leftContent("짜장")
                .rightContent("짬뽕")
                .build();

        Principal principal = () -> "1";

        // when
        boolean regist = boardService.regist(boardRegistRequest, principal);// 게시글 저장

        // then
        assertThat(regist).isEqualTo(true);
    }

    @Test
    public void 게시글상세조회테스트() {
        // given
        Long boardKey = 1L;

        // when
        BoardDetailDto boardProfile = boardService.findBoardProfile(boardKey);
        List<CommentDto> comment = boardService.findComment(boardKey);

        // then
        assertThat(boardProfile).isNotNull();
    }

    @Test
    public void 게시글좋아요추가테스트() {
        // given
        Long boardKey = 1L;
        Long userKey = 1L;

        // when
        boolean insert = boardHeartHistoryService.insert(boardKey, userKey);

        // then
        assertThat(insert).isTrue();
    }
}
