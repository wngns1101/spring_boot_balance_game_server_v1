package com.example.balanceGame;

import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.request.*;
import com.example.balanceGame.response.FindUserResponse;
import com.example.balanceGame.response.ModifyResponse;
import com.example.balanceGame.response.SignInResponse;
import com.example.balanceGame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void 회원가입테스트() {
        // given
        SignUpRequest request = SignUpRequest.builder() // 회원가입 정보 생성
                .userId("test")
                .userName("테스트")
                .userPw("zzz")
                .userEmail("test1@naver.com")
                .build();

        // when
        ResponseEntity join = userService.join(request); // 회원가입 진행

        // then
        assertThat(join.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 로그인토큰테스트() {
        // given
        SignInRequest request = SignInRequest.builder() // 로그인 정보 생성
                .userId("test")
                .userPw("zzz")
                .build();

        // when
        ResponseEntity<SignInResponse> login = userService.login(request); // 로그인 진행

        // then
        log.info(login.getBody().getToken()); // 토큰 조회
        log.info(jwtProvider.getUsernameFromToken(login.getBody().getToken())); // 토큰 복호화
    }

    @Test
    public void 정보수정테스트() {
        // given
        ModifyRequest modify = ModifyRequest.builder() // 수정할 정보 생성
                .userId("mo")
                .userEmail("di")
                .userName("fy")
                .build();

        Principal principal = () -> "test"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        ResponseEntity<ModifyResponse> modify1 = userService.modify(modify, principal); // 수정 진행

        // then
        assertThat(modify1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 정보조회테스트() {
        // given
        Principal principal = () -> "mo"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        ResponseEntity<FindUserResponse> profile = userService.profile(principal); // 정보 조회 진행

        // then
        log.info(String.valueOf(profile.getBody()));
    }

    @Test
    public void 비밀번호변경테스트() {
        // given
        ModifyPwRequest test = ModifyPwRequest.builder()
                .modifyPw("change")
                .currentPw("zzz")
                .build(); // 비밀번호 정보 생성

        Principal principal = () -> "mo"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        ResponseEntity responseEntity = userService.modifyPw(test, principal); // 비밀번호 변경 진행

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 회원삭제() {
        // given
        DeleteRequest change = DeleteRequest.builder() // 비밀번호 정보 생성
                .pw("change")
                .build();
        Principal principal = () -> "mo"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        ResponseEntity delete = userService.delete(change, principal); // 계정 삭제 진행

        // then
        assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
