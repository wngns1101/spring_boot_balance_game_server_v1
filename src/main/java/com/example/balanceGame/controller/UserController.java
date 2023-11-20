package com.example.balanceGame.controller;

import com.example.balanceGame.controller.http.request.*;
import com.example.balanceGame.controller.http.response.FindUserResponse;
import com.example.balanceGame.controller.http.response.ModifyResponse;
import com.example.balanceGame.controller.http.response.SignInResponse;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.InternalServerException;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name="회원")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    // mysql 한글 인코딩 안 되는 이슈 발생 -> docker 내부에서 cnf 파일 만들어서 utf8 세팅 후 mysql 재시작 해야한다.
    // 회원가입 컨트롤러
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody SignUpRequest signUpRequest) {
        log.info("join controller");
        try {
            boolean join = userService.join(signUpRequest); // 회원가입 진행

            if (join) {
                return new ResponseEntity(Message.CREATED_USER, HttpStatus.OK); // 성공하면 200 리턴
            } else {
                return new ResponseEntity(Message.CREATED_USER_FAILED, HttpStatus.INTERNAL_SERVER_ERROR); // 실패하면 500 리턴
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 로그인 컨트롤러
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody SignInRequest signInRequest) {
        log.info("login controller");

        try {
            String token = userService.login(signInRequest); // 로그인 후 jwt 생성

            SignInResponse build = SignInResponse.builder() // response dto 생성
                    .message(Message.LOGIN_SUCCESS)
                    .token(token)
                    .build();

            return new ResponseEntity<>(build, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 회원 정보 조회 컨트롤러
    @GetMapping("/profile")
    public ResponseEntity<FindUserResponse> profile(Principal principal) {
        log.info("findUser controller");
        try {
            User byUserId = userService.profile(principal); // 유저 정보 조회

            FindUserResponse findUserResponse = FindUserResponse.builder() // 유저 정보 반환할 Response 생성
                    .message(Message.READ_USER)
                    .userId(byUserId.getUserId())
                    .userName(byUserId.getUserName())
                    .userEmail(byUserId.getUserEmail())
                    .build();

            return new ResponseEntity<>(findUserResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 회원 정보 수정 컨트롤러
    @PutMapping("/modify")
    public ResponseEntity<ModifyResponse> modify(@RequestBody ModifyRequest modifyRequest, Principal principal) {
        log.info("modify controller");
        try {
            String token = userService.modify(modifyRequest, principal); // 회원 정보 수정

            ModifyResponse response = ModifyResponse.builder() // response 생성
                    .message(Message.UPDATE_USER)
                    .token(token)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }

    // 비밀번호 변경 컨트롤러
    @PostMapping("/modifyPw")
    public ResponseEntity modifyPw(@RequestBody ModifyPwRequest modifyPwRequest, Principal principal) {
        log.info("modify Pw controller");

        try {
            User user = userService.modifyPw(modifyPwRequest, principal);// 회원 비밀번호 수정

            if (passwordEncoder.matches(modifyPwRequest.getModifyPw(), user.getUserPw())) { // 엔티티에서 수정된 비밀번호가 Request에서 수정할 비밀번호랑 같은지 비교
                return new ResponseEntity(Message.UPDATE_PW, HttpStatus.OK);
            }else{
                return new ResponseEntity(Message.UPDATE_PW_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }
    // 회원 탈퇴 컨트롤러
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody DeleteRequest deleteRequest, Principal principal) {
        log.info("delete controller");

        try {
            boolean delete = userService.delete(deleteRequest, principal); // 회원 삭제

            if (delete) {
                return new ResponseEntity(Message.DELETE_USER, HttpStatus.OK); // 성공하면 200 리턴
            } else {
                return new ResponseEntity(Message.DELETE_USER_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InternalServerException("서버 오류입니다.");
        }
    }
}