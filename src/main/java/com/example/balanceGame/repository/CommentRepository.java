package com.example.balanceGame.repository;

import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.entity.Comment;
import com.example.balanceGame.entity.CommentReport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.example.balanceGame.entity.QComment.comment;
@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;
    private final JPAQueryFactory qm;

    public Long regist(Comment comment) {
        em.persist(comment);
        return comment.getCommentKey();
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
            e.printStackTrace();
            return false;
        }
    }

    public Comment findByCommentKey(Long commentKey) {
        return qm.selectFrom(comment).where(comment.commentKey.eq(commentKey)).fetchOne();
    }

    public boolean report(CommentReport commentReportBuilder) {
        try {
            em.persist(commentReportBuilder);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
