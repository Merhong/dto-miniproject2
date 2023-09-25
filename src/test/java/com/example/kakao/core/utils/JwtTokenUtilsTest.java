package com.example.kakao.core.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao._core.utils.JwtTokenUtils;
import com.example.kakao.user.User;
import org.junit.jupiter.api.Test;

public class JwtTokenUtilsTest {

    @Test
    public void jwtCreateTest() {
        User user = User.builder().id(1).email("ssar@nate.com").build();
        String jwt = JwtTokenUtils.create(user);
        System.out.println("jwtCreateAndVerify 테스트 :" + jwt);
    }

    // 검증시 오류?
    // 1. 시간 만료
    // 2. 키를 잘못입력
    // 3. 위조된 토큰?
    @Test
    public void jwtVerifyTest() {
        String jwt = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtZXRhY29kaW5nLWtleSIsImlkIjoxLCJlbWFpbCI6InNzYXJAbmF0ZS5jb20iLCJleHAiOjE2OTYyMjQ4MDV9.MY6pW3HnItcvPa3tuqOpX72HlzDi-Au1HQP2u-W0o02Vh19FplMWr3OUAnloip8LM_ysFJGPNk523Tl5CkbSlw";
        DecodedJWT decodedJWT = JwtTokenUtils.verify(jwt);

        // 꺼내는 법
        int id = decodedJWT.getClaim("id").asInt();
        String email = decodedJWT.getClaim("email").asString();

        System.out.println("id : " + id);
        System.out.println("eamil : " + email);
    }
}
