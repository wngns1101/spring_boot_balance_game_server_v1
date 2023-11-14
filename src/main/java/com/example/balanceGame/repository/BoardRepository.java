package com.example.balanceGame.repository;

import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.FindAllByDateDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.exception.Message;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.balanceGame.entity.QBoard.board;
import static com.example.balanceGame.entity.QComment.comment;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;
    private final JPAQueryFactory qm;

    public boolean regist(Board board) {
        try {
            em.persist(board);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Board findByBoardKey(Long boardkey) {
        return em.find(Board.class, boardkey);
    }

    public BoardDetailDto findBoardAndHeart(long boardKey) {
        BoardDetailDto boardHeartDto = qm.select(Projections.bean(BoardDetailDto.class, board.boardKey, board.user.userName, board.boardTitle, board.leftContent, board.rightContent, board.leftCount, board.rightCount, board.heartCount))
                .from(board)
                .where(board.boardKey.eq(boardKey))
                .fetchOne();
        return boardHeartDto;
    }

    public List<FindAllByDateDto> findAllByDate(PageRequest pageRequest) {
        return qm.select(Projections.bean(FindAllByDateDto.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent))
                .from(board)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.boardDate.desc())
                .fetch();
    }
}
