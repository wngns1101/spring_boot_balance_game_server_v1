package com.example.balanceGame.controller;

import com.example.balanceGame.request.ModifyPwRequest;
import com.example.balanceGame.request.ModifyRequest;
import com.example.balanceGame.request.SignInRequest;
import com.example.balanceGame.request.SignUpRequest;
import com.example.balanceGame.response.FindUserResponse;
import com.example.balanceGame.response.SignInResponse;
import com.example.balanceGame.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name="회원")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // mysql 한글 인코딩 안 되는 이슈 발생 -> docker 내부에서 cnf 파일 만들어서 utf8 세팅 후 mysql 재시작 해야한다.
    // 회원가입 컨트롤러
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody SignUpRequest signUpRequest) {
        log.info("join controller");
        return userService.join(signUpRequest);
    }

    // 로그인 컨트롤러
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        log.info("login controller");
        return userService.login(signInRequest);
    }

    // 회원 정보 조회 컨트롤러
    @GetMapping("/findUser")
    public ResponseEntity<FindUserResponse> findUser(Principal principal) {
        log.info("findUser controller");
        return userService.findUser(principal);
    }

    // 회원 정보 수정 컨트롤러
    @PutMapping("/modify")
    public ResponseEntity modify(@RequestBody ModifyRequest modifyRequest, Principal principal) {
        log.info("modify controller");
        return userService.modify(modifyRequest, principal);
    }

    // 비밀번호 변경 컨트롤러
    @PostMapping("/modifyPw")
    public ResponseEntity modifyPw(@RequestBody ModifyPwRequest modifyPwRequest, Principal principal) {
        log.info("modify Pw controller");
        return userService.modifyPw(modifyPwRequest, principal);
    }
}