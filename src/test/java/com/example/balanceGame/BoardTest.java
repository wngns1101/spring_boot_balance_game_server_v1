package com.example.balanceGame;

import com.example.balanceGame.request.BoardRegistRequest;
import com.example.balanceGame.response.BoardDetailResponse;
import com.example.balanceGame.response.FindAllByDateResponse;
import com.example.balanceGame.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

    @Test
    @Rollback(value = false)
    @Transactional
    public void 게시글등록테스트() {
        // given
        BoardRegistRequest boardRegistRequest = BoardRegistRequest.builder() // 게시글 작성
                .boardTitle("짜장면 짬뽕 둘 중 뭐가 좋아?")
                .leftContent("짜장")
                .rightContent("짬뽕")
                .build();

        Principal principal = () -> "test";

        // when
        ResponseEntity result = boardService.regist(boardRegistRequest, principal); // 게시글 저장

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 게시글상세조회테스트() {
        // given and when
        ResponseEntity<BoardDetailResponse> boardProfile = boardService.findBoardProfile(2);

        // then
        log.info(String.valueOf(boardProfile.getBody()));
    }

    @Test
    public void 게시글페이징테스트() {
        // given
        int page = 0;
        int size = 20;

        // when
        ResponseEntity<FindAllByDateResponse> allByDate = boardService.findAllByDate(PageRequest.of(page, size));

        // then
        log.info(String.valueOf(allByDate));
    }
}
