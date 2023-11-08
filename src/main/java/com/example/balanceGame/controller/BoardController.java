package com.example.balanceGame.controller;

import com.example.balanceGame.request.BoardRegistRequest;
import com.example.balanceGame.response.BoardDetailResponse;
import com.example.balanceGame.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name="게시글")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

     // 게시글 등록 controller
    @PostMapping("/regist")
    public ResponseEntity regist(@RequestBody BoardRegistRequest boardRegistRequest, Principal principal) {
        return boardService.regist(boardRegistRequest, principal);
    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> detail(@RequestParam("boardKey") long boardKey) {
        return boardService.findBoardProfile(boardKey);
    }
}
