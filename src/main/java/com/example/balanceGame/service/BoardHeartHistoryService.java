package com.example.balanceGame.service;

import com.example.balanceGame.entity.Board;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardHeartHistoryService {
    private final BoardRepository boardRepository;
//    private final BoardHeartHistoryRepository boardHeartHistoryRepository;

    @Transactional
    public void insert(Long boardKey, String userName) {
        Board board = boardRepository.findByBoardKey(boardKey);
        if (board == null) {
            throw new NotFoundException();
        }

//        boardHeartHistoryRepository.findHeartByBoardIdAndUserId(boardKey, )
    }
}
