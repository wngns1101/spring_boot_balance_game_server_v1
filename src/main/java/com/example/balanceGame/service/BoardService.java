package com.example.balanceGame.service;

import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.CommentRepository;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.BoardRegistRequest;
import com.example.balanceGame.dto.BoardHeartDto;
import com.example.balanceGame.response.BoardDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    // 게시글 등록
    @Transactional
    public ResponseEntity regist(BoardRegistRequest boardRegistRequest, Principal principal) {
        // 사용자 추출
        User user = findUser(principal.getName());

        Board board = Board.createBoard(boardRegistRequest, user);

        return boardRepository.regist(board);
    }

    // 게시글 상세 조회 메서드
    public ResponseEntity<BoardDetailResponse> findBoardProfile(long boardKey) {
        // 게시글 정보, 좋아요 조회
        BoardHeartDto boardHeartDto = boardRepository.findBoardAndHeart(boardKey);

        // 댓글 리스트 조회
        List<CommentDto> allComment = commentRepository.findAllComment(boardKey);

        // 조회 정보들 종합
        BoardDetailResponse boardDetailResponse = new BoardDetailResponse(boardHeartDto, allComment);

        return new ResponseEntity<>(boardDetailResponse, HttpStatus.OK);
    }

    // 유저 조회 메서드
    private User findUser(String keyword) {
        // 유저 정보 조회
        User byUserId = userRepository.findByUserId(keyword);

        // 조회한 유저가 없으면 예외 throw
        if (byUserId == null) {
            throw new NotFoundException();
        }

        return byUserId;
    }
}
