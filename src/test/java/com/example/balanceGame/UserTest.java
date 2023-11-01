package com.example.balanceGame;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.repository.UserRepository;
import com.example.balanceGame.request.UserRequest;
import com.example.balanceGame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입테스트() {
        UserRequest request = UserRequest.builder()
                .userName("테스트")
                .userPw("1234")
                .userEmail("test1@naver.com")
                .build();

        ResponseEntity join = userService.join(request);
    }
}
