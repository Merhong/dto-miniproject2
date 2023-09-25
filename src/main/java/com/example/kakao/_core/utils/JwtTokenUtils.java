package com.example.kakao._core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao.user.User;

import java.time.Instant;

public class JwtTokenUtils {
    public static String create(User user) {
        String jwt = JWT.create()
                .withSubject("metacoding-key") // 제목, 토큰의 이름
                .withClaim("id", user.getId()) // 토큰에다가 기록하고 싶은 정보 PK를 넣는다. (매우 중요)
                .withClaim("email", user.getEmail()).withExpiresAt(Instant.now().plusMillis(1000 * 60 * 60 * 24 * 7L)) // 만료되는 시간을 적어야한다. (매우 중요)
                .sign(Algorithm.HMAC512("meta")); // 원래는 환경변수로 O/S에 적어둬서 외부접근을 차단해야한다.
        return "Bearer " + jwt;
    }

    // 검증시 오류?
    // 1. 시간 만료
    // 2. 키를 잘못입력
    // 3. 위조된 토큰?
    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {
        // 통신후에 Bearer은 토큰 검증에 필요없으니 없앰
        jwt = jwt.replace("Bearer ", "");
        // JWT를 검증하고 나서 헤더와 페이로드를 base64로 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("meta")).build().verify(jwt);
        return decodedJWT;
    }
}