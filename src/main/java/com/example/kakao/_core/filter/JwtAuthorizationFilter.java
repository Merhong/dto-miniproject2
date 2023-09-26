package com.example.kakao._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kakao._core.errors.exception.Exception401;
import com.example.kakao._core.utils.JwtTokenUtils;
import com.example.kakao.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/* 책임 : 인가, jwt 토큰 검증 */

/**
 * /carts/**
 * /orders/**
 * /products/**
 * 이 주소만 필터가 동작하면 된다
 */
public class JwtAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        // 다운 캐스팅을 해줘야지 더 많은 메서드를 사용할 수 있다.
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String jwt = request.getHeader("Authorization");

        // 토큰이 없을때
        if (jwt == null || jwt.isEmpty()) {
            onError(response, "토큰이 없습니다.");
            return;
        }

        // 토큰이 있을때 -> 토큰 검증
        try {
            DecodedJWT decodedJWT = JwtTokenUtils.verify(jwt);
            int userId = decodedJWT.getClaim("id").asInt();
            String email = decodedJWT.getClaim("email").asString();

            // Request와 같은 생명주기의 세션에 담는다. -> Controller에서 꺼내 쓰기 쉽게하려고!!!
            User sessionUser = User.builder()
                    .id(userId)
                    .email(email)
                    .build();

            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", sessionUser);

            // 검증이 성공하면 다음 필터를 타게 넘겨준다.
            chain.doFilter(request, response);

            // 검증 실패 예외처리
        } catch (SignatureVerificationException | JWTDecodeException e1) {
            onError(response, "토큰 검증 실패");
            // 토큰 시간만료 예외처리
        } catch (TokenExpiredException e2) {
            onError(response, "토큰 시간 만료");
        }
    }

    // 필터는 Dispatcher Servlet 앞에 있기 때문에 ExceptionHandler를 호출 못한다.
    // 따라서 직접 예외 처리를 구현 해야 한다.
    private void onError(HttpServletResponse response, String msg) {
        Exception401 e401 = new Exception401(msg);

        try {
            // 직접 객체를 JSON 문자열로 바꾼다. -> 필터라서 Message Convert의 도움을 못 받기때문에
            // Message Convert는 MIME 타입을 자동으로 넣어주는데 역시 직접 작성해줘야 함.
            String body = new ObjectMapper().writeValueAsString(e401.body());
            response.setStatus(e401.status().value());
            // response.setHeader("Content-Type", "application/json; charset=utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // MIME 타입 설정
            // 버퍼에 담는다. 버퍼는 모조 스트림, PrinterWriter는 Auto-flush를 해준다.?
            // 양쪽의 PrintWriter 설정을 본다 (디폴트는 utf-8)
            // 휴대폰으로 보면 한글이 다 깨져있음 -> 브라우저는 자동으로 해주지만
            // App은 개발자가 3바이트씩 설정하지 않는 이상 한글이 깨진다.
            PrintWriter out = response.getWriter();
            out.println(body);
        } catch (Exception e) {
            System.out.println("파싱 에러가 날 수 없음");
        }
    }
}
