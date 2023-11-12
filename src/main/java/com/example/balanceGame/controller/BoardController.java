package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.controller.http.response.BoardDetailResponse;
import com.example.balanceGame.controller.http.response.FindAllByDateResponse;
import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.dto.FindAllByDateDto;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        boolean regist = boardService.regist(boardRegistRequest, principal);

        if (regist) {
            return new ResponseEntity(Message.REGIST_BOARD, HttpStatus.OK);
        } else {
            return new ResponseEntity(Message.FAILED_REGIST_BOARD, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 상세조회 controller
    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> detail(@RequestParam("boardKey") long boardKey) {
        BoardDetailDto boardProfile = boardService.findBoardProfile(boardKey); // 게시글 상세, 좋아요 수 조회

        List<CommentDto> comment = boardService.findComment(boardKey); // 댓글 조회

        BoardDetailResponse boardDetailResponse = new BoardDetailResponse(boardProfile, comment); // Dto Response 형태로 종합

        return new ResponseEntity<>(boardDetailResponse, HttpStatus.OK);
    }

    // 게시글 페이징 날짜순으로 조회
    @GetMapping("/findAllByDate")
    public ResponseEntity<FindAllByDateResponse> findAllByDate(@RequestParam("page") Integer page, @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size); // 페이징 객체 생성

        List<FindAllByDateDto> allByDate = boardService.findAllByDate(pageRequest); // 날짜순 리스트 조회

        if (allByDate.size() == 0) {
            return new ResponseEntity<>(FindAllByDateResponse.builder().message(Message.FIND_BOARD_FAILED).build(), HttpStatus.OK); // 조회한 게시글이 없을 때
        }

        return new ResponseEntity<>(FindAllByDateResponse.builder().message(Message.FIND_BOARD_SUCCESS).findAllByDateDtos(allByDate).build(), HttpStatus.OK); // 조회 게시글 리턴
    }
}
