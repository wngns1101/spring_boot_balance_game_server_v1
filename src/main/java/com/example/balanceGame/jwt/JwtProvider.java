package com.example.balanceGame.jwt;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.NotFoundException;
import com.example.balanceGame.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final UserRepository userRepository;
    // 암호키
    @Value("${jwt.secret.key}")
    private String secret;

    // 토큰 생성 메서드
    public String createToken(String userId) {
        // 현재 날짜, 시간
        Date nowDate = new Date();

        // 유효 시간
        Duration durationTime = Duration.ofHours(2);

        // 만료 시간(현재 시간 + 유효 시간)
        Date expirationDate = Date.from(nowDate.toInstant().plus(durationTime));

        // 토큰 생성
        return Jwts.builder()
                // jwt 인증할 식별자
                .setSubject(userId)
                // 발급 시간
                .setIssuedAt(nowDate)
                // 만료 시간
                .setExpiration(expirationDate)
                // 설정한 secret를 사용하여 HS256 암호화 알고리즘으로 header, payload로 signature 생성
                .signWith(SignatureAlgorithm.HS256, secret)
                // jwt 문자열 생성
                .compact();
    }

    // 토큰에 있는 user로 엔티티 조회하는 메서드
    public User getUserFromToken(String token) {
        // 유저 이름 추출
        String userId = getUsernameFromToken(token);

        // 추출한 이름으로 유저 엔티티 조회
        User byUserId = userRepository.findByUserId(userId);

        if (byUserId != null) {
            return byUserId;
        }else{
            throw new NotFoundException();
        }
    }

    // 토큰에 있는 유저 가져오는 메서드
    public String getUsernameFromToken(String token) {
        // parser로 설정된 secret을 통해 토큰을 파싱하여 본문(Claims)에 있는 subject를 추출한다.
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization에 있는 토큰 가져오는 메서드
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // principal에는 유저 id를 저장한다. credentials는 비밀번호를 의미하지만 이미 사용자가 인증된 토큰에 비밀번호를 노출할 필요는 없다고 생각해 null을 전달한다.
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, List.of(new SimpleGrantedAuthority("USER")));
    }
}
