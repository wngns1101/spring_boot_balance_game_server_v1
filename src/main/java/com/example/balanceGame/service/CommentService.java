package com.example.balanceGame.service;

import com.example.balanceGame.controller.http.request.CommentDeleteRequest;
import com.example.balanceGame.controller.http.request.CommentReportRequest;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.Comment;
import com.example.balanceGame.entity.CommentReport;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.CommentRepository;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.controller.http.request.CommentRegistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록 메서드
    @Transactional
    public Long regist(CommentRegistRequest commentRegistRequest, Long userKey) {
        User user = findUser((userKey)); // 유저 정보 조회

        Board board = findBoard(commentRegistRequest.getBoardKey()); // 게시글 정보 조회

        Comment comment = Comment.createComment(commentRegistRequest, user, board); // 댓글 엔티티 생성

        return commentRepository.regist(comment);
    }

    @Transactional
    public boolean delete(CommentDeleteRequest commentDeleteRequest, Long userKey) {
        User user = findUser(userKey);
        Comment commentByBoardKeyAndUserKey = boardRepository.findCommentByBoardKeyAndUserKey(commentDeleteRequest.getBoardKey(), commentDeleteRequest.getCommentKey(), user.getUserKey());

        if (commentByBoardKeyAndUserKey == null) {
            return false;
        }

        return commentRepository.delete(commentByBoardKeyAndUserKey);
    }

    @Transactional
    public boolean report(CommentReportRequest commentReportRequest, Long userKey) {
        Comment byCommentKey = commentRepository.findByCommentKey(commentReportRequest.getCommentKey());

        if (byCommentKey == null) {
            throw new FailedFindException();
        }

       CommentReport commentReportBuilder = CommentReport.builder()
                .commentKey(commentReportRequest.getCommentKey())
                .userKey(userKey)
                .reportTitle(commentReportRequest.getCommentTitle())
                .reportContent(commentReportRequest.getCommentContent())
                .commentTime(LocalDateTime.now()).build();

       return commentRepository.report(commentReportBuilder);
    }

    // 유저 조회 메서드
    private User findUser(Long userKey) {
        // 유저 정보 조회
        User byUserKey = userRepository.findByUserKey(userKey);

        // 조회한 유저가 없으면 예외 throw
        if (byUserKey == null) {
            throw new NotFoundException();
        }

        return byUserKey;
    }

    private Board findBoard(Long boardKey) {
        // 게시글 정보 조회
        Board byBoardKey = boardRepository.findByBoardKey(boardKey);

        // 조회한 유저가 없으면 예외 throw
        if (byBoardKey == null) {
            throw new NotFoundException();
        }

        return byBoardKey;
    }

}
