package com.guCoding.carrotMarket.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.token_prefix:null}")
    private String TOKEN_PREFIX;

    // 토큰 생성
    public String create(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("todoList")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7  )) // 타입조심
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole().name())
                .sign(Algorithm.HMAC512(SECRET));
        return TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public LoginUser verify(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}
