package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.CommentRequest;
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
        CommentRequest commentRequest = CommentRequest.builder()
                .boardKey(2L)
                .commentContent("아니지 멍청아")
                .build();

        Principal principal = () -> "test5";

        ResponseEntity responseEntity = commentService.regist(commentRequest, principal);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
