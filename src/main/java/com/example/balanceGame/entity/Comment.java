package com.example.balanceGame.entity;

import com.example.balanceGame.controller.http.request.CommentRegistRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_key")
    private Long commentKey;

    // 댓글을 달 게시물
    @ManyToOne
    @JoinColumn(name = "board_key")
    private Board board;

    // 댓글을 다는 사용자
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    // 댓글 내용
    private String commentContent;

    // 작성 시간
    private LocalDateTime commentTime;

    public Comment(CommentRegistRequest commentRegistRequest, User user, Board board) {
        this.board = board;
        this.user = user;
        this.commentContent = commentRegistRequest.getCommentContent();
        this.commentTime = LocalDateTime.now();
    }


    public static Comment createComment(CommentRegistRequest commentRegistRequest, User user, Board board) {
        return new Comment(commentRegistRequest, user, board);
    }
}