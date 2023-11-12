package com.example.balanceGame.service;

import com.example.balanceGame.dto.BoardDetailDto;
import com.example.balanceGame.dto.CommentDto;
import com.example.balanceGame.dto.FindAllByDateDto;
import com.example.balanceGame.entity.Board;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.FailedFindException;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.BoardRepository;
import com.example.balanceGame.repository.CommentRepository;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.controller.http.request.BoardRegistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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

    // 게시글 상세 조회 메서드
    public BoardDetailDto findBoardProfile(long boardKey) {
        BoardDetailDto boardHeartDto = boardRepository.findBoardAndHeart(boardKey); // 게시글 정보, 좋아요 숫자 조회

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
    public List<FindAllByDateDto> findAllByDate(PageRequest pageRequest) {
        List<FindAllByDateDto> allByDate = boardRepository.findAllByDate(pageRequest); // 날짜 기준으로 20개의 데이터 조회

        return allByDate;
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
