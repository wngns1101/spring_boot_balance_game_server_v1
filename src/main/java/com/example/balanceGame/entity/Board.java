package com.example.balanceGame.entity;

import com.example.balanceGame.controller.http.request.BoardModifyRequest;
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

    // 수만 가져오려고 할 경우 별도 쿼리가 증가하기 때문에 성능 저하의 원인 유발
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardHeartHistory> heart;

    private Long heartCount = 0L;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comment;

    // 제목 ex) 짜장면 짬뽕 중 더 좋은 것은?
    private String boardTitle;

    // 왼쪽 답안 ex) 짜장면
    private String leftContent;

    // 오른쪽 답안 ex) 짬뽕
    private String rightContent;

    // 왼쪽을 누른 count
    private Long leftCount = 0L;

    // 오른쪽을 누른 count
    private Long rightCount = 0L;

    // 작성 시간
    private LocalDateTime boardDate;

    /*
        Optimistic Lock(낙관적 락)을 적용하기 위한 version
        좋아요 동시성 처리를 고민하던 중 싱글 서버이기 떄문에 Redis를 사용하지 앟고
        애플리케이션 단계에서 동시성 처리를 할 수 있다고 생각했다.
        Pessimistic Lock(비관적 락)은 불필요한 경우에도 Lock을 걸기 때문에 성능이 저하된다는 단점이 있어 낙관적 락을 사용하는 방법을 채택했다.
    */
    @Version
    private Long version;

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

    public void modifyBoard(BoardModifyRequest boardModifyRequest) {
        this.boardTitle = boardModifyRequest.getBoardTitle();
        this.leftContent = boardModifyRequest.getLeftContent();
        this.rightContent = boardModifyRequest.getRightContent();
        this.boardDate = LocalDateTime.now();
    }

    public void plusHeartCount() {
        if (this.heartCount == null) {
            this.heartCount = 1L; // 초기화되지 않은 경우 1로 초기화
        } else {
            this.heartCount += 1; // 이미 초기화된 경우 증가
        }
    }

    public void minusHeartCount() {
        if (this.heartCount != null && this.heartCount != 0) {
            this.heartCount -= 1;
        }
    }

    public void plusLeftCount() {
        if (this.leftCount == null){
            this.leftCount = 1L; // 초기화되지 않은 경우 1로 초기화
        } else {
            this.leftCount += 1; // 이미 초기화된 경우 증가
        }
    }

    public void minusLeftCount() {
        if (this.leftCount != null && this.leftCount != 0) {
            this.leftCount -= 1;
        }
    }

    public void plusRightCount() {
        if (this.rightCount == null){
            this.rightCount = 1L; // 초기화되지 않은 경우 1로 초기화
        } else {
            this.rightCount += 1; // 이미 초기화된 경우 증가
        }
    }

    public void minusRightCount() {
        if (this.rightCount != null && this.rightCount != 0) {
            this.rightCount -= 1;
        }
    }
}
