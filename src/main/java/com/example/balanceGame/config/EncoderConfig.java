package com.example.balanceGame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// 자동으로 클래스 빈 등록
// 개발자가 직접 제어가 불가능한 라이브러리일 경우 Component를 쓸 수 없음 Configuration으로 수동으로 빈을 등록해야함
@Configuration
public class EncoderConfig {

    // Configuration 안에서 선언해야한다.
    // Configuration이 붙어있는 클래스를 자동으로 빈을 등록한다 -> 클래스를 파싱한다. -> Bean이 붙어있는 메소드를 찾아서 빈을 생성한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
