package com.example.balanceGame.exception;

// ResponseEntity 리턴할 메시지 정보
public class Message {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAILED = "로그인 실패";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String DUPLICATE_USERID = "중복된 아이디 입니다.";
    public static final String DUPLICATE_USEREMAIL = "중복된 이메일 입니다.";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String CREATED_USER_FAILED = "회원 가입 실패";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴를 성공했습니다.";
    public static final String DELETE_USER_FAILED = "회원 탈퇴 성공";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
    public static final String UPDATE_PW = "비밀번호 수정에 성공했습니다.";
    public static final String UPDATE_PW_FAILED = "비밀번호 수정에 실패했습니다.";
    public static final String REGIST_BOARD = "게시글 등록에 성공했습니다.";
    public static final String REGIST_BOARD_FAILED = "게시글 등록에 실패했습니다.";
    public static final String FIND_BOARD = "게시글 조회에 성공했습니다.";
    public static final String FIND_BOARD_FAILED = "게시글이 존재하지 않습니다.";
    public static final String REGIST_COMMENT = "댓글 등록에 성공했습니다.";
    public static final String REGIST_COMMENT_FAILED = "댓글 등록에 실패했습니다.";
    public static final String FAILED_FIND_ERROR = "조회에 실패했습니다.";
    public static final String HEART_SUCCESS = "좋아요에 성공했습니다.";
    public static final String HEART_FAILED = "좋아요에 실패했습니다.";
    public static final String HEART_REMOVE = "좋아요 취소에 성공했습니다.";
    public static final String HEART_REMOVE_FAILED = "좋아요 취소에 실패했습니다.";
    public static final String DELETE_COMMENT = "댓글 삭제에 성공했습니다.";
    public static final String DELETE_COMMENT_FAILED = "댓글 삭제에 실패했습니다.";
    public static final String DELETE_BOARD = "댓글 삭제에 성공했습니다.";
    public static final String DELETE_BOARD_FAILED = "댓글 삭제에 실패했습니다.";
}
