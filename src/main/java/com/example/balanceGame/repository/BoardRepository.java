package com.example.balanceGame.repository;

import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.FindAllBoardDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.BoardGameResult;
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
import static com.example.balanceGame.entity.QBoardGameResult.boardGameResult;
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
            e.printStackTrace();
            return false;
        }
    }

    public Board findByBoardKeyAndUserKey(Long boardKey, long userKey) {
        return qm.selectFrom(board)
                .where(board.boardKey.eq(boardKey)
                        .and(board.user.userKey.eq(userKey)))
                .fetchOne();
    }

    public Board findByBoardKey(Long boardkey) {
        return em.find(Board.class, boardkey);
    }

    public BoardDetailDto findBoardAndHeartByBoardKey(long boardKey) {
        BoardDetailDto boardHeartDto = qm.select(Projections.bean(BoardDetailDto.class, board.boardKey, board.user.userName, board.boardTitle, board.leftContent, board.rightContent, board.leftCount, board.rightCount, board.heartCount))
                .from(board)
                .where(board.boardKey.eq(boardKey))
                .fetchOne();
        return boardHeartDto;
    }

    public List<FindAllBoardDto> findAllByDate(PageRequest pageRequest) {
        return qm.select(Projections.bean(FindAllBoardDto.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
                .from(board)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.boardDate.desc())
                .fetch();
    }

    public List<FindAllBoardDto> findAllByHeart(PageRequest pageRequest) {
        return qm.select(Projections.bean(FindAllBoardDto.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
                .from(board)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(board.heartCount.desc())
                .fetch();
    }

    public List<FindAllBoardDto> findAllByUserHeart(PageRequest pageRequest, Long userKey) {
        return qm.select(Projections.bean(FindAllBoardDto.class, board.boardKey, board.user.userName, board.boardDate, board.boardTitle, board.leftContent, board.rightContent, board.heartCount))
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


    public boolean delete(Board byBoardKeyAndUserKey) {
        try {
            em.remove(byBoardKeyAndUserKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public BoardGameResult findBoardGameResultByUserKeyAndBoardKey(Long boardKey, long userKey) {
        return qm.selectFrom(boardGameResult)
                .where(boardGameResult.boardKey.eq(boardKey).and(boardGameResult.userKey.eq(userKey))).fetchOne();
    }
}
