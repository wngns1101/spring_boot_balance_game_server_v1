package com.example.balanceGame.service;

import com.example.balanceGame.controller.http.request.BoardModifyRequest;
import com.example.balanceGame.controller.http.request.BoardReportRequest;
import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.dto.FindAllBoardDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.BoardReport;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.*;
import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 게시글 등록
    @Transactional
    public boolean regist(BoardRegistRequest boardRegistRequest, Principal principal) {
        User user = findUser(Long.parseLong(principal.getName())); // 사용자 추출

        Board board = Board.createBoard(boardRegistRequest, user); // 게시글 생성

        return boardRepository.regist(board);
    }

    @Transactional
    public boolean delete(Long boardKey, long userKey) {
        Board byBoardKeyAndUserKey = boardRepository.findByBoardKeyAndUserKey(boardKey, userKey);

        if (byBoardKeyAndUserKey == null) {
            throw new FailedFindException();
        }

        log.info(byBoardKeyAndUserKey.toString());

        return boardRepository.delete(byBoardKeyAndUserKey);
    }

    // 게시글 수정
    @Transactional
    public boolean modify(BoardModifyRequest boardModifyRequest, Long userKey) {
        Board byBoardKeyAndUserKey = boardRepository.findByBoardKeyAndUserKey(boardModifyRequest.getBoardKey(), userKey);

        if (byBoardKeyAndUserKey == null) {
            throw new FailedFindException();
        }
        try {
            byBoardKeyAndUserKey.modifyBoard(boardModifyRequest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게시글 상세 조회 메서드
    public BoardDetailDto findBoardProfile(long boardKey) {
        BoardDetailDto boardHeartDto = boardRepository.findBoardAndHeartByBoardKey(boardKey); // 게시글 정보, 좋아요 숫자 조회

        if (boardHeartDto == null) {
            throw new FailedFindException();
        }

        return boardHeartDto;
    }

    // 댓글 조회 메서드
    public List<CommentDto> findComment(long boardkey) {
        List<CommentDto> allComment = commentRepository.findAllComment(boardkey); // 댓글 리스트 조회

        return allComment;
    }

    // 게시글 리스트 조회 날짜순
    public List<FindAllBoardDto> findAllByDate(PageRequest pageRequest) {
        List<FindAllBoardDto> allByDate = boardRepository.findAllByDate(pageRequest); // 날짜 기준으로 20개의 데이터 조회

        return allByDate;
    }

    // 게시글 리스트 조회 좋아요순
    public List<FindAllBoardDto> findAllByHeart(PageRequest pageRequest) {
        List<FindAllBoardDto> allByHeart = boardRepository.findAllByHeart(pageRequest); // 좋아요 기준으로 20개의 데이터 조회

        return allByHeart;
    }

    // 게시글 리스트 조회 사용자가 좋아요 누른 게시글만
    public List<FindAllBoardDto> findAllByUserHeart(PageRequest pageRequest, Long userKey) {
        List<FindAllBoardDto> allByUserHeart = boardRepository.findAllByUserHeart(pageRequest, userKey); // 사용자가 좋아요 누른 기준으로 20개의 데이터 조회

        return allByUserHeart;
    }

    @Transactional
    public boolean report(BoardReportRequest boardReportRequest, Long userKey) {
        Board byBoardKey = boardRepository.findByBoardKey(boardReportRequest.getBoardKey());

        if (byBoardKey == null) {
            throw new FailedFindException();
        }

        BoardReport boardReport = BoardReport.builder()
                .boardKey(boardReportRequest.getBoardKey())
                .userKey(userKey)
                .reportTitle(boardReportRequest.getReportTitle())
                .reportContent(boardReportRequest.getReportContent())
                .reportDate(LocalDateTime.now())
                .build();

        return boardRepository.report(boardReport);
    }

    // 유저 조회 메서드
    private User findUser(Long userKey) {
        User byUserKey = userRepository.findByUserKey(userKey);

        // 조회한 유저가 없으면 예외 throw
        if (byUserKey == null) {
            throw new NotFoundException();
        }

        return byUserKey;
    }
}
