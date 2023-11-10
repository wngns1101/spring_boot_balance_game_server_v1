package com.example.balanceGame.service;

import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.Comment;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.CommentRepository;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.controller.http.request.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록 메서드
    @Transactional
    public ResponseEntity regist(CommentRequest commentRequest, Principal principal) {
        // 유저 정보 조회
        User user = findUser(Long.parseLong(principal.getName()));

        // 게시글 정보 조회
        Board board = findBoard(commentRequest.getBoardKey());

        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(commentRequest, user, board);

        return commentRepository.regist(comment);
    }

    // 유저 조회 메서드
    private User findUser(Long userKey) {
        User byUserKey = userRepository.findByUserKey(userKey);
        // 조회한 유저가 없으면 예외 throw
        if (byUserKey == null) {
            throw new NotFoundException();
        }

        return byUserKey;
    }

    private Board findBoard(Long boardKey) {
        // 유저 정보 조회
        Board byBoardKey = boardRepository.findByBoardKey(boardKey);

        // 조회한 유저가 없으면 예외 throw
        if (byBoardKey == null) {
            throw new NotFoundException();
        }

        return byBoardKey;
    }
}
