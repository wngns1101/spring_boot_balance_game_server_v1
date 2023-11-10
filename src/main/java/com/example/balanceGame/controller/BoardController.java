package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.controller.http.response.BoardDetailResponse;
import com.example.balanceGame.controller.http.response.FindAllByDateResponse;
import com.example.balanceGame.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

    // 게시글 상세조회 controller
    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> detail(@RequestParam("boardKey") long boardKey) {
        return boardService.findBoardProfile(boardKey);
    }

    // 게시글 페이징 날짜순으로 조회
    @GetMapping("/findAllByDate")
    public ResponseEntity<FindAllByDateResponse> findAllByDate(@RequestParam("page") Integer page, @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return boardService.findAllByDate(pageRequest);
    }
}
