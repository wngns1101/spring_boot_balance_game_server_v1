package com.example.balanceGame.service;

import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.dto.FindAllByDateDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.CommentRepository;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.dto.BoardHeartDto;
import com.example.balanceGame.controller.http.response.BoardDetailResponse;
import com.example.balanceGame.controller.http.response.FindAllByDateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
        User user = findUser(Long.parseLong(principal.getName()));

        Board board = Board.createBoard(boardRegistRequest, user);

        return boardRepository.regist(board);
    }

    // 게시글 상세 조회 메서드
    public ResponseEntity<BoardDetailResponse> findBoardProfile(long boardKey) {
        // 게시글 정보, 좋아요 숫자 조회
        BoardHeartDto boardHeartDto = boardRepository.findBoardAndHeart(boardKey);

        // 댓글 리스트 조회
        List<CommentDto> allComment = commentRepository.findAllComment(boardKey);

        // 조회 정보들 종합
        BoardDetailResponse boardDetailResponse = new BoardDetailResponse(boardHeartDto, allComment);

        return new ResponseEntity<>(boardDetailResponse, HttpStatus.OK);
    }

    public ResponseEntity<FindAllByDateResponse> findAllByDate(PageRequest pageRequest) {
        // 날짜 기준으로 20개의 데이터 조회
        List<FindAllByDateDto> allByDate = boardRepository.findAllByDate(pageRequest);

        // 조회한 게시글이 없을 때
        if (allByDate == null) {
            return new ResponseEntity<>(FindAllByDateResponse.builder().message("등록된 게시물이 없습니다.").build(), HttpStatus.OK);
        }

        // 조회 게시글 리턴
        return new ResponseEntity<>(FindAllByDateResponse.builder().message("게시물 조회에 성공했습니다.").findAllByDateDtos(allByDate).build(), HttpStatus.OK);
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

}
