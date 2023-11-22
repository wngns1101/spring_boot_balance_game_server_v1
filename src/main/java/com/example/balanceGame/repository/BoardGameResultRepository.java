package com.example.balanceGame.repository;

import com.example.balanceGame.entity.BoardGameResult;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BoardGameResultRepository {
    private final EntityManager em;

    public boolean save(BoardGameResult boardGameResult) {
        try {
            em.persist(boardGameResult);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(BoardGameResult boardGameResultByUserKeyAndBoardKey) {
        try {
            em.remove(boardGameResultByUserKeyAndBoardKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
