package com.korit.post_mini_project_back.jwt;

import com.korit.post_mini_project_back.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtTokenProvider {


    // Key 객체
    private final SecretKey key;


    // 토큰 제공자
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    // 토큰 생성
    public String createAccessToken(User userEntity) {


        // 토큰이 유효한 시간을 세팅
        Date now = new Date();
        long expiredTime = now.getTime() + (1000l * 60l * 60l * 24l);
        Date expiredDate = new Date(expiredTime);


        // jwt 토근을 builder로 만들기
        return Jwts.builder()
                .subject("server token")
                .issuer("JINHYUN")
                .issuedAt(new Date())
                .expiration(expiredDate) // 필수
                .claim("userId", userEntity.getUserId()) // 몇번 userId인지
                .signWith(key, SignatureAlgorithm.HS256) // 서명에는 key 객체 필요
                .compact();

    }


    // 토큰 활성화
    public boolean validateToken(String token) {
//        Claims claims = null;
        try {
            JwtParser jwtParser = Jwts.parser()
                    // 토큰 감별을 위한 (토큰이 유효한가) 키를 넘겨주기
                    .setSigningKey(key)
                    .build();
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public int getuserId(String token) {
        // parser
        return (int) Jwts.parser()
                .setSigningKey(key)
                .build()
                // 토큰 꺼내기
                .parseClaimsJws(token)
                .getPayload()
                // object 형태
                .get("userId");
    }

}
