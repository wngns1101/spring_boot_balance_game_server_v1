package com.example.balanceGame.repository;

import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.FindAllBoard;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.Comment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.balanceGame.entity.QBoard.board;
import static com.example.balanceGame.entity.QComment.comment;

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

    public List<FindAllBoard> findAllByDate(PageRequest pageRequest) {
        return qm.select(Projections.bean(FindAllBoard.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
                .from(board)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.boardDate.desc())
                .fetch();
    }

    public List<FindAllBoard> findAllByHeart(PageRequest pageRequest) {
        return qm.select(Projections.bean(FindAllBoard.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
                .from(board)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.heartCount.desc())
                .fetch();
    }

    public List<FindAllBoard> findAllByUserHeart(PageRequest pageRequest, Long userKey) {
        return qm.select(Projections.bean(FindAllBoard.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
                .from(board)
                .where(board.heart.any().userKey.eq(userKey))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.heart.any().heartTime.desc())
                .fetch();
    }

    public Comment findCommentByBoardKeyAndUserKey(Long boardKey, Long commentKey, Long userKey) {
        return qm.selectFrom(comment)
                .where(comment.board.boardKey.eq(boardKey)
                        .and(comment.commentKey.eq(commentKey))
                        .and(comment.user.userKey.eq(userKey)))
                .fetchOne();
    }

    public boolean delete(Comment commentByBoardKeyAndUserKey) {
        try {
            em.remove(commentByBoardKeyAndUserKey);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }
}
