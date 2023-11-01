package com.example.balanceGame.service;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.DuplicateUserException;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity join(UserRequest userRequest) {
        validateDuplicateUser(userRequest.getUserEmail());

        User joinUser = User.builder()
                .userName(userRequest.getUserName())
                .userPw(userRequest.getUserPw())
                .userEmail(userRequest.getUserEmail())
                .userStatus(false)
                .createDate(LocalDateTime.now())
                .build();

        return userRepository.join(joinUser);
    }

    private void validateDuplicateUser(String email) {
        User byUserEmail = userRepository.findByUserEmail(email);

        if (byUserEmail != null) {
            throw new DuplicateUserException();
        }
    }
}
