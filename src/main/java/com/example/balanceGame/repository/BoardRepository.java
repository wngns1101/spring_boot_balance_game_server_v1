package com.example.balanceGame.repository;

import com.example.balanceGame.entity.Board;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.dto.BoardHeartDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import static com.example.balanceGame.entity.QBoard.board;
import static com.example.balanceGame.entity.QHeart.heart;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;

    public ResponseEntity regist(Board board) {
        try {
            em.persist(board);
            return new ResponseEntity(Message.REGIST_BOARD, HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Board findByBoardId(int boardkey) {
        return em.find(Board.class, boardkey);
    }

    public BoardHeartDto findBoardAndHeart(long boardKey) {
            JPAQueryFactory qm = new JPAQueryFactory(em);
            BoardHeartDto boardHeartDto = qm.select(Projections.bean(BoardHeartDto.class, board.boardTitle, board.leftContent, board.rightContent, board.leftCount, board.rightCount, heart.count().as("heartCount")))
                    .from(board)
                    .leftJoin(heart).on(board.boardKey.eq(heart.board.boardKey))
                    .where(board.boardKey.eq(boardKey))
                    .fetchOne();
            return boardHeartDto;
    }
}
