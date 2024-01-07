package com.example.balanceGame.service;

import com.example.balanceGame.controller.http.request.BoardGameStartRequest;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.BoardGameResult;
import com.example.balanceGame.entity.Enum.BoardCountResult;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardGameResultRepository;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardGameResultService {
    private final BoardGameResultRepository boardGameResultRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게임 시작
    @Transactional
    public boolean start(BoardGameStartRequest boardGameStartRequest, Long userKey) {
        Board board = findBoard(boardGameStartRequest.getBoardKey()); // 게시글 조회

        findUser(userKey); // 유저 존재 유무 조회

        BoardGameResult heartByUserKeyAndBoardKey = boardRepository.findBoardGameResultByUserKeyAndBoardKey(boardGameStartRequest.getBoardKey(), userKey); // 사전에 유저가 게임 참가했던 이력 있는지 조회

        if (heartByUserKeyAndBoardKey != null) { // 있으면 early return
            return false;
        }

        BoardCountResult type;

        if (boardGameStartRequest.getResult().equals("left")) {
            type = BoardCountResult.L;
            board.plusLeftCount();
        } else if (boardGameStartRequest.getResult().equals("right")) {
            type = BoardCountResult.R;
            board.plusRightCount();
        }else{
            log.info("zz");
            return false;
        }

        log.info(board.getLeftCount().toString());

        BoardGameResult boardGameResult = BoardGameResult
                .builder()
                .boardKey(boardGameStartRequest.getBoardKey())
                .userKey(userKey)
                .boardCountResult(type)
                .boardCountTime(LocalDateTime.now())
                .build();

        return boardGameResultRepository.save(boardGameResult);
    }

    @Transactional
    public boolean delete(Long boardKey, Long userKey) {
        Board board = findBoard(boardKey);
        findUser(userKey);

        BoardGameResult boardGameResultByUserKeyAndBoardKey = boardRepository.findBoardGameResultByUserKeyAndBoardKey(boardKey, userKey);

        if (boardGameResultByUserKeyAndBoardKey == null) {
            return false;
        }

        BoardCountResult boardCountResult = boardGameResultByUserKeyAndBoardKey.getBoardCountResult();

        if (boardCountResult.toString().equals("L")) {
            board.minusLeftCount();
        } else if (boardCountResult.toString().equals("R")) {
            board.minusRightCount();
        } else {
            return false;
        }

        return boardGameResultRepository.delete(boardGameResultByUserKeyAndBoardKey);
    }

    public Board findBoard(Long boardKey) {
        Board byBoardKey = boardRepository.findByBoardKey(boardKey);// 게시글 조회

        if (byBoardKey == null) {
            throw new FailedFindException(); // 없을시에 예외 throw
        }

        return byBoardKey;
    }

    private void findUser(Long userKey) {
        // 유저 정보 조회
        User byUserId = userRepository.findByUserKey(userKey);

        // 조회한 유저가 없으면 예외 throw
        if (byUserId == null) {
            throw new NotFoundException();
        }
    }
}
