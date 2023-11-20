package com.example.balanceGame.service;

import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.BoardHeartHistory;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.repository.BoardHeartHistoryRepository;
import com.example.balanceGame.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardHeartHistoryService {
    private final EntityManager em;
    private final BoardRepository boardRepository;
    private final BoardHeartHistoryRepository boardHeartHistoryRepository;

    @Transactional
    public boolean insert(Long boardKey, Long userKey) {
            Board board = boardRepository.findByBoardKey(boardKey); // 게시글 조회

            if (board == null) {
                throw new FailedFindException(); // 게시글이 없으면 throw
            }

            // 좋아요 내역에 게시글에 유저가 좋아요한 기록이 있는지 조회
        BoardHeartHistory heartByBoardIdAndUserId = boardHeartHistoryRepository.findHeartByBoardIdAndUserId(boardKey, userKey);

        if (heartByBoardIdAndUserId != null) { // 있으면 early return
            return false;
        }

            board.plusHeartCount(); // 없을 경우 boardCount += 1


            BoardHeartHistory boardHeartHistory = BoardHeartHistory.builder() // 내역 저장할 엔티티 생성
                    .board(board)
                    .userKey(userKey)
                    .heartTime(LocalDateTime.now())
                    .build();

            return boardHeartHistoryRepository.save(boardHeartHistory);
    }

    @Transactional
    public boolean delete(Long boardKey, Long userKey) {
        Board byBoardKey = boardRepository.findByBoardKey(boardKey);

        if (byBoardKey == null) {
            throw new FailedFindException();
        }

        BoardHeartHistory heartByBoardIdAndUserId = boardHeartHistoryRepository.findHeartByBoardIdAndUserId(boardKey, userKey);

        if (heartByBoardIdAndUserId == null) { // 없으면 early return
            return false;
        }

        byBoardKey.minusHeartCount();

        return boardHeartHistoryRepository.delete(heartByBoardIdAndUserId);
    }
}
