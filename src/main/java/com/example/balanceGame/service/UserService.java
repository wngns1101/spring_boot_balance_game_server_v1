package com.example.balanceGame.service;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.DuplicateUserException;
import com.example.balanceGame.exception.EncryptionException;
import com.example.balanceGame.exception.Message;
import com.example.balanceGame.jwt.JwtProvider;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.SignInRequest;
import com.example.balanceGame.request.SignUpRequest;
import com.example.balanceGame.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new EncryptionException();
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

    public ResponseEntity<SignInResponse> login(SignInRequest signInRequest) {
        User findUser = userRepository.findByUserId(signInRequest.getUserId());
        String token = jwtProvider.createToken(findUser.getUserId());
        SignInResponse build = SignInResponse.builder()
                .message(Message.LOGIN_SUCCESS)
                .token(token)
                .build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    // 중복 아이디 조회 메서드
    private void validateDuplicateUserId(String userId) {
        User byUserId = userRepository.findByUserId(userId);
        if (byUserId != null) {
            throw new DuplicateUserException();
        }
    }

    // 중복 이메일 조회 메서드
    private void validateDuplicateUserEmail(String email) {
        User byUserEmail = userRepository.findByUserEmail(email);

        if (byUserEmail != null) {
            throw new DuplicateUserException();
        }
    }
}
