package com.example.balanceGame.service;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.DuplicateUserException;
import com.example.balanceGame.exception.PasswordMismatchException;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.*;
import com.example.balanceGame.response.FindUserResponse;
import com.example.balanceGame.response.ModifyResponse;
import com.example.balanceGame.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity join(SignUpRequest signUpRequest) {
        // 중복 조회
        validateDuplicateUserId(signUpRequest.getUserId());
        validateDuplicateUserEmail(signUpRequest.getUserEmail());

        // 비밀번호 암호화
        String encodePw = passwordEncoder.encode(signUpRequest.getUserPw());

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(signUpRequest.getUserPw(), encodePw)) {
            throw new PasswordMismatchException();
        }

        // User 엔티티 생성
        User joinUser = User.builder()
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
    public ResponseEntity<SignInResponse> login(SignInRequest signInRequest) {
        // 아이디로 유저 조회
        User findUser = findUser(signInRequest.getUserId());

        // 비밀번호가 저장된 것과 일치하지 않으면 예외 리턴
        if (!passwordEncoder.matches(signInRequest.getUserPw(), findUser.getUserPw())) {
            throw new PasswordMismatchException();
        }

        // jwt 생성
        String token = jwtProvider.createToken(findUser.getUserId());

        // response dto 생성
        SignInResponse build = SignInResponse.builder()
                .message(Message.LOGIN_SUCCESS)
                .token(token)
                .build();

        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    // 유저 정보 상세 조회 메서드
    public ResponseEntity<FindUserResponse> profile(Principal principal) {
        // 유저 조회
        User byUserId = findUser(principal.getName());

        // response 생성
        FindUserResponse findUserResponse = FindUserResponse.builder()
                .message(Message.READ_USER)
                .userId(byUserId.getUserId())
                .userName(byUserId.getUserName())
                .userEmail(byUserId.getUserEmail())
                .build();

        return new ResponseEntity<>(findUserResponse, HttpStatus.OK);
    }

    // 수정 메서드
    @Transactional
    public ResponseEntity<ModifyResponse> modify(ModifyRequest modifyRequest, Principal principal) {
        // 유저 정보 조회
        User byUserId = findUser(principal.getName());

        // 유저 정보 수정
        byUserId.modifyUser(modifyRequest);

        // 토큰 재발급
        String token = jwtProvider.createToken(byUserId.getUserId());

        ModifyResponse response = ModifyResponse.builder()
                .message(Message.UPDATE_USER)
                .token(token)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    // 비밀번호 수정 메서드
    @Transactional
    public ResponseEntity modifyPw(ModifyPwRequest modifyPwRequest, Principal principal) {
        // 유저 정보 조회
        User user = findUser(principal.getName());

        // 비빌번호가 데이터베이스에 저장된 비밀번호와 같은지 조회
        if (!passwordEncoder.matches(modifyPwRequest.getCurrentPw(), user.getUserPw())) {
            throw new PasswordMismatchException();
        }

        user.modifyPw(passwordEncoder.encode(modifyPwRequest.getModifyPw()));

        return new ResponseEntity(Message.UPDATE_PW, HttpStatus.OK);
    }

    // 회원 삭제 메서드
    @Transactional
    public ResponseEntity delete(DeleteRequest deleteRequest, Principal principal) {
        // 유저 정보 조회
        User user = findUser(principal.getName());

        // 비빌번호가 데이터베이스에 저장된 비밀번호와 같은지 조회
        if (!passwordEncoder.matches(deleteRequest.getPw(), user.getUserPw())) {
            throw new PasswordMismatchException();
        }

        return userRepository.delete(user);
    }

    // 중복 아이디 조회 메서드
    private void validateDuplicateUserId(String userId) {
        // 유저 정보 조회
        User byUserId = userRepository.findByUserId(userId);

        if (byUserId != null) {
            throw new DuplicateUserException();
        }
    }

    // 중복 이메일 조회 메서드
    private void validateDuplicateUserEmail(String email) {
        // 유저 정보 조회
        User byUserEmail = userRepository.findByUserEmail(email);

        if (byUserEmail != null) {
            throw new DuplicateUserException();
        }
    }

    // 유저 조회 메서드
    private User findUser(String keyword) {
        // 유저 정보 조회
        User byUserId = userRepository.findByUserId(keyword);

        // 조회한 유저가 없으면 예외 throw
        if (byUserId == null) {
            throw new NotFoundException();
        }

        return byUserId;
    }
}
