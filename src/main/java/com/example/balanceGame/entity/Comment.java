package com.example.balanceGame.entity;

import com.example.balanceGame.request.CommentRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_key")
    private long commentKey;

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

    public Comment(CommentRequest commentRequest, User user, Board board) {
        this.board = board;
        this.user = user;
        this.commentContent = commentRequest.getCommentContent();
        this.commentTime = LocalDateTime.now();
    }


    public static Comment createComment(CommentRequest commentRequest, User user, Board board) {
        return new Comment(commentRequest, user, board);
    }
}