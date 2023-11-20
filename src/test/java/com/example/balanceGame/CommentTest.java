package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.CommentDeleteRequest;
import com.example.balanceGame.controller.http.request.CommentRegistRequest;
import com.example.balanceGame.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import  static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest
public class CommentTest {
    @Autowired
    private CommentService commentService;

    @Test
    public void 댓글등록테스트() {
        // given
        CommentRegistRequest commentRegistRequest = CommentRegistRequest.builder()
                .boardKey(16L)
                .commentContent("아니지 멍청아")
                .build();

        Long userKey = 1L;

        // when
        boolean regist = commentService.regist(commentRegistRequest, userKey);

        // then
        assertThat(regist).isTrue();
    }

    @Test
    public void 댓글삭제테스트() {
        // given
        CommentDeleteRequest commentDeleteRequest = CommentDeleteRequest.builder()
                .boardKey(16L)
                .commentKey(6L)
                .build();

        Long userKey = 1L;

        // when
        boolean delete = commentService.delete(commentDeleteRequest, userKey);

        // then
        assertThat(delete).isTrue();
    }
}
