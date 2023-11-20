package com.example.balanceGame.repository;

import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.entity.Comment;
import com.example.balanceGame.exception.Message;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.example.balanceGame.entity.QComment.comment;
@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;
    private final JPAQueryFactory qm;

    public boolean regist(Comment comment) {
        try {
            em.persist(comment);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    public List<CommentDto> findAllComment(long boardKey) {
        return qm.select(Projections.bean(CommentDto.class, comment.commentKey, comment.commentTime, comment.commentContent))
                .from(comment)
                .where(comment.board.boardKey.eq(boardKey))
                .fetch();
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
