package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.BoardGameDeleteRequest;
import com.example.balanceGame.controller.http.request.BoardGameStartRequest;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.service.BoardGameResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name="게임결과")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boardGame")
public class BoardGameResultController {

    private final BoardGameResultService boardGameResultService;

    // 게임 시작하면 선택
    @PostMapping("/start")
    public ResponseEntity start(@RequestBody BoardGameStartRequest boardGameStartRequest, Principal principal) {
        try {
            Long userKey = Long.valueOf(principal.getName());
            boolean start = boardGameResultService.start(boardGameStartRequest, userKey);
            if (start) {
                return new ResponseEntity("게임 결과가 성공했습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity("게임 결과에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            e.printStackTrace();
            return new ResponseEntity("결과 반영에 실패했습니다. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody BoardGameDeleteRequest boardGameDeleteRequest, Principal principal) {
        try {
            Long userKey = Long.valueOf(principal.getName());
            Long boardKey = Long.valueOf(boardGameDeleteRequest.getBoardKey());
            boolean delete = boardGameResultService.delete(boardKey, userKey);
            if (delete) {
                return new ResponseEntity("참여한 게임 결과 삭제 성공", HttpStatus.OK);
            } else {
                return new ResponseEntity("참여한 게임 결과 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity("회원이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FailedFindException e) {
            e.printStackTrace();
            return new ResponseEntity("게시글이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
