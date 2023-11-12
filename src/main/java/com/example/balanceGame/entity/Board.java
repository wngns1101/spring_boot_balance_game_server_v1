package com.example.balanceGame.entity;

import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_key")
/*
    수정: Long은 Wrapper 클래스로 특정 유형의 클래스이다.(long을 감싸는 클래스) null을 허용한다.
    도메인에서 컬럼의 id는 데이터가 생성될 시점에 값이 할당되므로 특정 시점에 따라 존재할 수도, 안 할 수도 있다.
    Long은 원시 타입의 long보다는 성능이 떨어지기 때문에 not null이 보장된 필드라면 long을 써야한다.
    private long boardKey;
 */
    private Long boardKey = 0L;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private User user;

/*  수정 전 필드
    수만 가져오려고 할 경우 별도 쿼리가 증가하기 때문에 성능 저하의 원인 유발
    @OneToOne(mappedBy = "board")
    private Heart heart;
 */
    private Long heartCount;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment;

    // 제목 ex) 짜장면 짬뽕 중 더 좋은 것은?
    private String boardTitle;

    // 왼쪽 답안 ex) 짜장면
    private String leftContent;

    // 오른쪽 답안 ex) 짬뽕
    private String rightContent;

    // 왼쪽을 누른 count
    private int leftCount = 0;

    // 오른쪽을 누른 count
    private int rightCount = 0;

    // 작성 시간
    private LocalDateTime boardDate;

    public Board(BoardRegistRequest boardRegistRequest, User user) {
        this.user = user;
        this.boardTitle = boardRegistRequest.getBoardTitle();
        this.leftContent = boardRegistRequest.getLeftContent();
        this.rightContent = boardRegistRequest.getRightContent();
        this.boardDate = LocalDateTime.now();
    }


    public static Board createBoard(BoardRegistRequest boardRegistRequest, User user) {
        return new Board(boardRegistRequest, user);
    }

    public void addCountHeart() {
        if (this.heartCount == null) {
            this.heartCount = 1L; // 초기화되지 않은 경우 1로 초기화
        } else {
            this.heartCount += 1; // 이미 초기화된 경우 증가
        }
    }
}
