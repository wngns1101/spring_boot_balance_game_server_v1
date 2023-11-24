package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.BoardDeleteRequest;
import com.example.balanceGame.controller.http.request.BoardModifyRequest;
import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import com.example.balanceGame.controller.http.request.BoardReportRequest;
import com.example.balanceGame.controller.http.response.BoardDetailResponse;
import com.example.balanceGame.controller.http.response.FindAllByDateResponse;
import com.example.balanceGame.controller.http.response.FindAllByHeartResponse;
import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.dto.FindAllBoardDto;
import com.example.balanceGame.exception.InternalServerException;
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
        try {
            boolean regist = boardService.regist(boardRegistRequest, principal);

            if (regist) {
                return new ResponseEntity(Message.REGIST_BOARD, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.REGIST_BOARD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 삭제 controller
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody BoardDeleteRequest boardDeleteRequest, Principal principal) {
        long userKey = Long.parseLong(principal.getName());
        try {
            boolean delete = boardService.delete(boardDeleteRequest.getBoardKey(), userKey);
            if (delete) {
                return new ResponseEntity(Message.DELETE_COMMENT, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.DELETE_COMMENT_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 수정 controller
    @PatchMapping("/modify")
    public ResponseEntity modify(@RequestBody BoardModifyRequest boardModifyRequest, Principal principal) {
        try {
            Long userKey = Long.valueOf(principal.getName());
            boolean modify = boardService.modify(boardModifyRequest, userKey);
            if (modify) {
                return new ResponseEntity(Message.MODIFY_BOARD_SUCCESS, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.MODIFY_BOARD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 상세조회 controller
    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> detail(@RequestParam("boardKey") long boardKey) {
        try {
            BoardDetailDto boardProfile = boardService.findBoardProfile(boardKey); // 게시글 상세, 좋아요 수 조회

            List<CommentDto> comment = boardService.findComment(boardKey); // 댓글 조회

            BoardDetailResponse boardDetailResponse = new BoardDetailResponse(boardProfile, comment); // Dto Response 형태로 종합

            return new ResponseEntity<>(boardDetailResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 게시글 페이징 날짜순으로 조회
    @GetMapping("/findAllByDate")
    public ResponseEntity<FindAllByDateResponse> findAllByDate(@RequestParam("page") Integer page, @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size); // 페이징 객체 생성
        try {
            List<FindAllBoardDto> allByDate = boardService.findAllByDate(pageRequest); // 날짜순 리스트 조회

            if (allByDate.size() == 0) {
                return new ResponseEntity<>(FindAllByDateResponse.builder().message(Message.FIND_BOARD_FAILED).build(), HttpStatus.OK); // 조회한 게시글이 없을 때
            }else{
                return new ResponseEntity<>(FindAllByDateResponse.builder().message(Message.FIND_BOARD).findAllBoardDtos(allByDate).build(), HttpStatus.OK); // 조회 게시글 리턴
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 게시글 좋아요 순으로 조회
    @GetMapping("/findAllByHeart")
    public ResponseEntity<FindAllByHeartResponse> findAllByHeart(@RequestParam("page") Integer page, @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size); // 페이징 객체 생성
        try {
            List<FindAllBoardDto> allByHeart = boardService.findAllByHeart(pageRequest);

            if (allByHeart.size() == 0) {
                return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.FIND_BOARD_FAILED).build(), HttpStatus.OK); // 조회한 게시글이 없을 때
            } else {
                return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.FIND_BOARD).findAllBoardDtos(allByHeart).build(), HttpStatus.OK); // 조회 게시글 리턴
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.INTERNAL_SERVER_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 사용자가 좋아요 누른 게시글 조회
    @GetMapping("/findAllByUserHeart")
    public ResponseEntity<FindAllByHeartResponse> findAllByUserHeart(@RequestParam("page") Integer page, @RequestParam(value = "size", defaultValue = "20", required = false) Integer size, Principal principal) {
        PageRequest pageRequest = PageRequest.of(page, size); // 페이징 객체 생성
        long userKey = Long.parseLong(principal.getName());
        try {
            List<FindAllBoardDto> allByUserHeart = boardService.findAllByUserHeart(pageRequest, userKey);

            if (allByUserHeart.size() == 0) {
                return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.FIND_BOARD_FAILED).build(), HttpStatus.OK); // 조회한 게시글이 없을 때
            } else {
                return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.FIND_BOARD).findAllBoardDtos(allByUserHeart).build(), HttpStatus.OK); // 조회 게시글 리턴
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(FindAllByHeartResponse.builder().message(Message.INTERNAL_SERVER_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/report")
    public ResponseEntity report(@RequestBody BoardReportRequest boardReportRequest, Principal principal) {
        try {
            Long userKey = Long.valueOf(principal.getName());
            boolean report = boardService.report(boardReportRequest, userKey);
            if (report) {
                return new ResponseEntity(Message.REPORT_REGIST_SUCCESS, HttpStatus.OK);
            } else {
                return new ResponseEntity(Message.REPORT_REGIST_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
