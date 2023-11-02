package com.example.balanceGame.controller;

import com.example.balanceGame.request.SignInRequest;
import com.example.balanceGame.request.SignUpRequest;
import com.example.balanceGame.response.SignInResponse;
import com.example.balanceGame.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="회원")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    // mysql 한글 인코딩 안 되는 이슈 발생 -> docker 내부에서 cnf 파일 만들어서 utf8 세팅 후 mysql 재시작 해야한다.
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody SignUpRequest signUpRequest) {
        return userService.join(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        return userService.login(signInRequest);
    }

}