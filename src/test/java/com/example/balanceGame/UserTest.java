package com.example.balanceGame;

import com.example.balanceGame.controller.http.request.*;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입테스트() {
        // given
        SignUpRequest request = SignUpRequest.builder() // 회원가입 정보 생성
                .userId("test5")
                .userName("테스트")
                .userPw("zzz")
                .userEmail("test1@naver.com")
                .build();

        // when
        boolean join = userService.join(request);// 회원가입 진행

        // then
        assertThat(join).isEqualTo(true);
    }

    @Test
    public void 로그인토큰테스트() {
        // given
        SignInRequest request = SignInRequest.builder() // 로그인 정보 생성
                .userId("test5")
                .userPw("zzz")
                .build();

        // when
        String token = userService.login(request);// 로그인 진행
        String usernameFromToken = jwtProvider.getUsernameFromToken(token); // 토근에서 key 값 조회
        User byUserKey = userRepository.findByUserKey(Long.parseLong(usernameFromToken)); // Long 타입으로 바꾼 키로 User 엔티티 조회

        // then
        assertThat(byUserKey.getUserId()).isEqualTo(request.getUserId()); // 조회한 User 엔티티에 있는 UserId와 Request로 보낸 UserId가 일치한지 검증
    }

    @Test
    public void 정보수정테스트() {
        // given
        ModifyRequest modify = ModifyRequest.builder() // 수정할 정보 생성
                .userId("mo")
                .userEmail("di")
                .userName("fy")
                .build();

        Principal principal = () -> "1"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        String modify1 = userService.modify(modify, principal);// 수정 진행
        String usernameFromToken = jwtProvider.getUsernameFromToken(modify1); // 토근에서 key 값 조회
        User user = userRepository.findByUserKey(Long.parseLong(usernameFromToken)); // Long 타입으로 바꾼 키로 User 엔티티 조회

        // then
        assertThat(user.getUserId()).isEqualTo(modify.getUserId());
    }

    @Test
    public void 정보조회테스트() {
        // given
        Principal principal = () -> "1"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        User profile = userService.profile(principal);// 정보 조회 진행

        // then
        assertThat(Long.parseLong(principal.getName())).isEqualTo(profile.getUserKey());
    }

    @Test
    public void 비밀번호변경테스트() {
        // given
        ModifyPwRequest test = ModifyPwRequest.builder()
                .modifyPw("change")
                .currentPw("zzz")
                .build(); // 비밀번호 정보 생성

        Principal principal = () -> "1"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        User user = userService.modifyPw(test, principal);// 비밀번호 변경 진행

        // then
        assertThat(passwordEncoder.matches(test.getModifyPw(), user.getUserPw())).isTrue();
    }

    @Test
    public void 회원삭제() {
        // given
        DeleteRequest change = DeleteRequest.builder() // 비밀번호 정보 생성
                .pw("change")
                .build();
        Principal principal = () -> "1"; // JWT 토큰 있다는 가정하에 사용자 객체 생성

        // when
        boolean delete = userService.delete(change, principal);// 계정 삭제 진행

        // then
        assertThat(delete).isEqualTo(true);
    }
}
