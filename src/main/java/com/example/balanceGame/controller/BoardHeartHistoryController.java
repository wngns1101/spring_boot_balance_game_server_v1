package com.example.balanceGame.controller;

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
import java.util.Map;

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

        boolean insert = boardHeartHistoryService.insert(heartInsertRequest.getBoardKey(), userKey); // 좋아요 추가

        if (insert) {
            return new ResponseEntity<>(Message.HEART_SUCCESS, HttpStatus.OK); // 좋아요 추가 성공
        } else {
            return new ResponseEntity<>(Message.HEART_FAILED, HttpStatus.INTERNAL_SERVER_ERROR); // 좋아요 추가 살패
        }
    }
}
