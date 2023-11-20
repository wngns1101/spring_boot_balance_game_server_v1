package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.HeartDeleteRequest;
import com.example.balanceGame.controller.http.request.HeartInsertRequest;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.service.BoardHeartHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "좋아요")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boardHeartHistory")
public class BoardHeartHistoryController {

    private final BoardHeartHistoryService boardHeartHistoryService;

    @PostMapping("/insert")
    public ResponseEntity insert(@RequestBody HeartInsertRequest heartInsertRequest, Principal principal) {
        Long userKey = Long.parseLong(principal.getName()); // 토큰에서 유저키 가져오기

        try {
            boolean insert = boardHeartHistoryService.insert(heartInsertRequest.getBoardKey(), userKey); // 좋아요 추가 메서드
            if (insert) {
                return new ResponseEntity<>(Message.HEART_SUCCESS, HttpStatus.OK); // 좋아요 추가 성공
            } else {
                return new ResponseEntity<>(Message.HEART_FAILED, HttpStatus.INTERNAL_SERVER_ERROR); // 좋아요 추가 살패
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR); // 동시에 실행시 낙관적락 에러
        }
    }

    @PostMapping("/delete")
    public ResponseEntity remove(@RequestBody HeartDeleteRequest heartDeleteRequest, Principal principal) {
        Long userKey = Long.parseLong(principal.getName()); // 토큰에서 유저키 가져오기

        try {
            boolean delete = boardHeartHistoryService.delete(heartDeleteRequest.getBoardKey(), userKey); // 좋아요 취소 처리
            if (delete) {
                return new ResponseEntity(Message.HEART_REMOVE, HttpStatus.OK); // 좋아요 취소 성공했을 때
            } else {
                return new ResponseEntity(Message.HEART_REMOVE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR); // 좋아요 취소 실패
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR); // 예기치 못한 오류
        }
    }
}
