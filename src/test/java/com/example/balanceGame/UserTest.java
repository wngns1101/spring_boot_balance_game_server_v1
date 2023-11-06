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
        // 회원가입 정보 생성
        SignUpRequest request = SignUpRequest.builder()
                .userId("test")
                .userName("테스트")
                .userPw("zzz")
                .userEmail("test1@naver.com")
                .build();

        // 회원가입 진행
        ResponseEntity join = userService.join(request);
        assertThat(join.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 로그인토큰테스트() {
        // 로그인 정보 생성
        SignInRequest request = SignInRequest.builder()
                .userId("test")
                .userPw("zzz")
                .build();

        // 로그인 진행
        ResponseEntity<SignInResponse> login = userService.login(request);

        // 토큰 조회
        log.info(login.getBody().getToken());

        // 토큰 복호화
        log.info(jwtProvider.getUsernameFromToken(login.getBody().getToken()));
    }

    @Test
    public void 정보수정테스트() {
        // 수정할 정보 생성
        ModifyRequest modify = ModifyRequest.builder()
                .userId("mo")
                .userEmail("di")
                .userName("fy")
                .build();

        // JWT 토큰 있다는 가정하에 사용자 객체 생성
        Principal principal = () -> "test";

        // 수정 진행
        ResponseEntity<ModifyResponse> modify1 = userService.modify(modify, principal);

        assertThat(modify1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 정보조회테스트() {
        // JWT 토큰 있다는 가정하에 사용자 객체 생성
        Principal principal = () -> "mo";

        // 정보 조회 진행
        ResponseEntity<FindUserResponse> profile = userService.profile(principal);

        log.info(String.valueOf(profile.getBody()));
    }

    @Test
    public void 비밀번호변경테스트() {
        // 비밀번호 정보 생성
        ModifyPwRequest test = ModifyPwRequest.builder().modifyPw("change").currentPw("zzz").build();

        // JWT 토큰 있다는 가정하에 사용자 객체 생성
        Principal principal = () -> "mo";

        // 비밀번호 변경 진행
        ResponseEntity responseEntity = userService.modifyPw(test, principal);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 회원삭제() {
        // 비밀번호 정보 생성
        DeleteRequest change = DeleteRequest.builder().pw("change").build();

        // JWT 토큰 있다는 가정하에 사용자 객체 생성
        Principal principal = () -> "mo";

        // 계정 삭제 진행
        ResponseEntity delete = userService.delete(change, principal);

        assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
