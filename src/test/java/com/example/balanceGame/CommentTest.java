package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.CommentDeleteRequest;
import com.example.balanceGame.controller.http.request.CommentRegistRequest;
import com.example.balanceGame.controller.http.request.CommentReportRequest;
import com.example.balanceGame.repository.CommentRepository;
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
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void 댓글등록테스트() {
        // given
        CommentRegistRequest commentRegistRequest = CommentRegistRequest.builder()
                .boardKey(16L)
                .commentContent("아니지 멍청아")
                .build();

        Long userKey = 1L;

        // when
        Long regist = commentService.regist(commentRegistRequest, userKey);

        // then
        assertThat(regist).isEqualTo(commentRepository.findByCommentKey(regist).getCommentKey());
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

    @Test
    public void 댓글신고테스트() {
        // given
        CommentReportRequest commentReportRequest = new CommentReportRequest(1L, "댓글이 이게 뭐임", "진짜 쓰레기네");
        Long userKey = 1L;

        // when
        boolean report = commentService.report(commentReportRequest, userKey);

        // then
        assertThat(report).isTrue();
    }
}
