package com.example.balanceGame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class EncodeTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 비밀번호_암호화_테스트() {
        // 초기 비밀번호
        String pw = "1234";

        // 암호화한 비밀번호
        String encodedPw = passwordEncoder.encode(pw);

        // 암호화한 비밀번호와 초기 비밀번호가 맞는지 확인하는 로직
        if (passwordEncoder.matches(pw, encodedPw)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
