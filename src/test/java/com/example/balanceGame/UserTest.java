package com.example.balanceGame;

import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.SignInRequest;
import com.example.balanceGame.request.SignUpRequest;
import com.example.balanceGame.response.SignInResponse;
import com.example.balanceGame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

@Slf4j
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @Rollback(value = false)
    public void 회원가입테스트() {
        SignUpRequest request = SignUpRequest.builder()
                .userName("테스트")
                .userPw("1234")
                .userEmail("test1@naver.com")
                .build();

        ResponseEntity join = userService.join(request);
    }

    @Test
    public void 로그인_토큰_테스트() {
        SignInRequest request = SignInRequest.builder()
                .userId("test")
                .userPw("zzz")
                .build();

        ResponseEntity<SignInResponse> login = userService.login(request);

        // 토큰 조회
        log.info(login.getBody().getToken());

        // 토큰 복호화
        log.info(jwtProvider.getUsernameFromToken(login.getBody().getToken()));
    }
}
