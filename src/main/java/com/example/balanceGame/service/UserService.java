package com.example.balanceGame.service;

import com.example.balanceGame.controller.http.request.*;
import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.*;
import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.controller.http.response.FindUserResponse;
import com.example.balanceGame.controller.http.response.ModifyResponse;
import com.example.balanceGame.controller.http.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.FailedLoginException;
import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원가입 메서드
    @Transactional
    public boolean join(SignUpRequest signUpRequest) {
        validateDuplicateUserId(signUpRequest.getUserId()); // 아이디 중복 조회

        validateDuplicateUserEmail(signUpRequest.getUserEmail()); // 이메일 중복 조회

        String encodePw = passwordEncoder.encode(signUpRequest.getUserPw()); // 비밀번호 암호화

        validatePassword(signUpRequest.getUserPw(), encodePw);

        User joinUser = User.builder() // User 엔티티 생성
                .userId(signUpRequest.getUserId())
                .userName(signUpRequest.getUserName())
                .userPw(encodePw)
                .userEmail(signUpRequest.getUserEmail())
                .userStatus(false)
                .createDate(LocalDateTime.now())
                .build();

        return userRepository.join(joinUser);
    }

    // 로그인 메서드
    public String login(SignInRequest signInRequest) {

        Long userKey = userRepository.findByUserId(signInRequest.getUserId()); // userId로 userKey 조회하는 메서드

        if (userKey == null) {
            throw new NotFoundException(); // 조회된 userKey가 없으면 throw
        }

        User findUser = findUser(userKey); // 아이디로 유저 조회

        validatePassword(signInRequest.getUserPw(), findUser.getUserPw()); // 비밀번호가 저장된 것과 일치하지 않으면 throw

        String token = jwtProvider.createToken(findUser.getUserKey());// jwt 생성

        if (token == null) {
            throw new FailedCreateTokenException(); // 토큰이 없으면 throw
        }

        return token;
    }

    // 유저 정보 상세 조회 메서드
    public User profile(Principal principal) {
        return findUser(Long.parseLong(principal.getName())); // Principle 객체로 가져온 키값으로 User 엔티티 조회
    }

    // 수정 메서드
    @Transactional
    public boolean modify(ModifyRequest modifyRequest, Principal principal) {

        validateDuplicateUserId(modifyRequest.getUserId()); // 수정할 아이디 중복 조회


        User byUserId = findUser(Long.parseLong(principal.getName())); // 유저 정보 조회

        try {
            byUserId.modifyUser(modifyRequest); // 유저 정보 수정
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 비밀번호 수정 메서드
    @Transactional
    public User modifyPw(ModifyPwRequest modifyPwRequest, Principal principal) {

        User user = findUser(Long.parseLong(principal.getName())); // 유저 정보 조회

        validatePassword(modifyPwRequest.getCurrentPw(), user.getUserPw()); // 비빌번호가 데이터베이스에 저장된 비밀번호와 같은지 조회

        user.modifyPw(passwordEncoder.encode(modifyPwRequest.getModifyPw())); // 비밀번호 변경

        return user;
    }

    // 회원 삭제 메서드
    @Transactional
    public boolean delete(DeleteRequest deleteRequest, Principal principal) {
        // 유저 정보 조회
        User user = findUser(Long.parseLong(principal.getName()));

        // 비빌번호가 데이터베이스에 저장된 비밀번호와 같은지 조회
        validatePassword(deleteRequest.getPw(), user.getUserPw());

        return userRepository.delete(user);
    }

    // 중복 아이디 조회 메서드
    private void validateDuplicateUserId(String userId) {
        // 유저 정보 조회
        Long userKey = userRepository.findByUserId(userId);

        if (userKey != null) {
            throw new DuplicateUserIdException();
        }
    }

    // 중복 이메일 조회 메서드
    private void validateDuplicateUserEmail(String email) {
        // 유저 정보 조회
        User byUserEmail = userRepository.findByUserEmail(email);

        if (byUserEmail != null) {
            throw new DuplicateUserEmailException();
        }
    }

    private void validatePassword(String signInRequest, String findUser) {
        if (!passwordEncoder.matches(signInRequest, findUser)) {
            throw new PasswordMismatchException();
        }
    }

    // 유저 조회 메서드
    private User findUser(Long userKey) {
        // 유저 정보 조회
        User byUserId = userRepository.findByUserKey(userKey);

        // 조회한 유저가 없으면 예외 throw
        if (byUserId == null) {
            throw new NotFoundException();
        }

        return byUserId;
    }
}
