package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.BoardGameDeleteRequest;
import com.example.balanceGame.controller.http.request.BoardGameStartRequest;
import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.controller.http.request.HeartInsertRequest;
import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.repository.BoardGameResultRepository;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.service.BoardGameResultService;
import com.example.balanceGame.service.BoardHeartHistoryService;
import com.example.balanceGame.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardHeartHistoryService boardHeartHistoryService;

    @Autowired
    private BoardGameResultService boardGameResultService;

    @Autowired
    private BoardRepository boardRepository;
    @Test
    public void 게시글등록테스트() {
        // given
        BoardRegistRequest boardRegistRequest = BoardRegistRequest.builder() // 게시글 작성
                .boardTitle("짜장면 짬뽕 둘 중 뭐가 좋아?")
                .leftContent("짜장")
                .rightContent("짬뽕")
                .build();

        Principal principal = () -> "1";

        // when
        boolean regist = boardService.regist(boardRegistRequest, principal);// 게시글 저장

        // then
        assertThat(regist).isEqualTo(true);
    }

    @Test
    public void 게시글삭제테스트() {
        // given
        Long boardKey = 1L;
        Principal principal = () -> "1";

        // when
        boolean delete = boardService.delete(boardKey, Long.parseLong(principal.getName()));

        assertThat(delete).isTrue();
    }

    @Test
    public void 게시글상세조회테스트() {
        // given
        Long boardKey = 1L;

        // when
        BoardDetailDto boardProfile = boardService.findBoardProfile(boardKey);
        List<CommentDto> comment = boardService.findComment(boardKey);

        // then
        assertThat(boardProfile).isNotNull();
    }

    @Test
    public void 게시글좋아요추가테스트() {
        // given
        Long boardKey = 1L;
        Long userKey = 1L;

        // when
        boolean insert = boardHeartHistoryService.insert(boardKey, userKey);

        // then
        assertThat(insert).isTrue();
    }

    @Test
    public void 좋아요_취소_테스트() {
        // given
        Long userKey = 1L;
        Long boardKey = 16L;

        // when
        boolean delete = boardHeartHistoryService.delete(boardKey, userKey);

        // then
        assertThat(boardService.findBoardProfile(boardKey).getHeartCount()).isEqualTo(0);
    }

    @Test
    @Rollback(value = false)
    public void 게임_참가_테스트() {
        // given
        Long userKey = 1L;
        BoardGameStartRequest boardGameStartRequest = new BoardGameStartRequest(1L, "left");

        // when
        boardGameResultService.start(boardGameStartRequest, userKey);

        // then
        assertThat(boardRepository.findBoardGameResultByUserKeyAndBoardKey(userKey, boardGameStartRequest.getBoardKey()).getUserKey()).isEqualTo(userKey);
    }

    @Test
    @Rollback(value = false)
    public void 게임_결과_삭제_테스트() {
        // given
        Long userKey = 1L;
        BoardGameDeleteRequest boardGameDeleteRequest = new BoardGameDeleteRequest(1L);

        // when
        boardGameResultService.delete(boardGameDeleteRequest.getBoardKey(), userKey);

        // then
        assertThat(boardRepository.findBoardGameResultByUserKeyAndBoardKey(userKey, boardGameDeleteRequest.getBoardKey())).isNull();
    }

    @Test
    public void 동시_좋아요_API_호출_테스트() {
        //given
        ExecutorService service = Executors.newFixedThreadPool(2); // 동시 실행 쓰레드 2개

        //when
        service.execute(() -> { // 쓰레드 첫번째 실행
            RestTemplate restTemplate = new RestTemplate(); // Http 통신에 사용할 객체 생성
            String url = "http://localhost:8080/boardHeartHistory/insert"; // 호출 api url

            HeartInsertRequest dto = new HeartInsertRequest(); // 요청 객체 생성
            dto.setBoardKey(16L);

            HttpHeaders headers = new HttpHeaders(); // jwt 전달할 헤더 객체 생성
            headers.set("Authorization", "Bearer " + "user1토큰");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<HeartInsertRequest> request = new HttpEntity<>(dto, headers); // 요청 정보를 전달할 객체 생성

            ResponseEntity<String> response = restTemplate.exchange( // 요청을 실행하고 저장
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String body = response.getBody(); // 결과 확인
            log.info(body);
        });

        service.execute(() -> { // 쓰레드 두번째 실행
            RestTemplate restTemplate = new RestTemplate(); // Http 통신에 사용할 객체 생성
            String url = "http://localhost:8080/boardHeartHistory/insert"; // 호출 api url

            HeartInsertRequest dto = new HeartInsertRequest(); // 요청 객체 생성
            dto.setBoardKey(16L);

            HttpHeaders headers = new HttpHeaders(); // jwt 전달할 헤더 객체 생성
            headers.set("Authorization", "Bearer " + "user2토큰");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<HeartInsertRequest> request = new HttpEntity<>(dto, headers); // 요청 정보를 전달할 객체 생성

            ResponseEntity<String> response = restTemplate.exchange( // 요청을 실행하고 저장
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String body = response.getBody(); // 결과 확인
            log.info(body);
        });

        // 동시에 api를 호출 했을 때 동시성 문제가 생겼을 떄 낙관적 락을 사용해 예외를 throw 했기 때문에 증가는 1이 되어야한다.
        assertThat(boardService.findBoardProfile(16L).getHeartCount()).isEqualTo(1L);
    }

    @Test
    public void 동시_게임시작_API_호출_테스트() {
        //given
        ExecutorService service = Executors.newFixedThreadPool(2); // 동시 실행 쓰레드 2개

        //when
        service.execute(() -> { // 쓰레드 첫번째 실행
            RestTemplate restTemplate = new RestTemplate(); // Http 통신에 사용할 객체 생성
            String url = "http://localhost:8080/boardGame/start"; // 호출 api url

            BoardGameStartRequest dto = new BoardGameStartRequest(); // 요청 객체 생성
            dto.setBoardKey(1L);
            dto.setResult("left");

            HttpHeaders headers = new HttpHeaders(); // jwt 전달할 헤더 객체 생성
            headers.set("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzAwNjE5NzczLCJleHAiOjE3MDA2MjY5NzN9.aHj8KB2JaB0-A6yj7PHB2QqRoexUXIZ-i0MUqyAhckY");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<BoardGameStartRequest> request = new HttpEntity<>(dto, headers); // 요청 정보를 전달할 객체 생성

            ResponseEntity<String> response = restTemplate.exchange( // 요청을 실행하고 저장
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String body = response.getBody(); // 결과 확인
            log.info(body);
        });

        service.execute(() -> { // 쓰레드 두번째 실행
            RestTemplate restTemplate = new RestTemplate(); // Http 통신에 사용할 객체 생성
            String url = "http://localhost:8080/boardGame/start"; // 호출 api url

            BoardGameStartRequest dto = new BoardGameStartRequest(); // 요청 객체 생성
            dto.setBoardKey(1L);
            dto.setResult("left");

            HttpHeaders headers = new HttpHeaders(); // jwt 전달할 헤더 객체 생성
            headers.set("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzAwNjE3NDMxLCJleHAiOjE3MDA2MjQ2MzF9.LFS90f06yi9OXM6guvUG8rNt5O9_nMAqgnVWsPFG5Vo");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<BoardGameStartRequest> request = new HttpEntity<>(dto, headers); // 요청 정보를 전달할 객체 생성

            ResponseEntity<String> response = restTemplate.exchange( // 요청을 실행하고 저장
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String body = response.getBody(); // 결과 확인
            log.info(body);
        });

        // 동시에 api를 호출 했을 때 동시성 문제가 생겼을 떄 낙관적 락을 사용해 예외를 throw 했기 때문에 증가는 1이 되어야한다.
        assertThat(boardService.findBoardProfile(1L).getHeartCount()).isEqualTo(1L);
    }
}

