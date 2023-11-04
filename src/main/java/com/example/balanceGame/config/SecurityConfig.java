package com.example.balanceGame.config;

import com.example.balanceGame.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // rest api 서버는 stateless하기 때문에 인증 정보를 보관하지 않는다. 따라서 csrf 코드를 작성할 필요가 없기 때문에 disable을 한다
                .csrf().disable()
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/**",
                                        "/user/login",
                                        "/user/join").permitAll()
                                .anyRequest().authenticated()
                )
                // 세션 안 사용하므로 STATELESS 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .build();
    }
}

