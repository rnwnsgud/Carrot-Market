package com.guCoding.carrotMarket.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class JwtProvider {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.token_prefix:null}")
    private String TOKEN_PREFIX;

    // 토큰 생성
    public String accessTokenCreate(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("accessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) //  1시간
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60)) //  테스트용 : 1분
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole().name())
                .sign(Algorithm.HMAC512(SECRET));
        return TOKEN_PREFIX + jwtToken;
    }
    public String refreshTokenCreate(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("refreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) //  1주일
                .withClaim("id", loginUser.getUser().getId())
                .sign(Algorithm.HMAC512(SECRET));
        return TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public LoginUser accessTokenVerify(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }

    public LoginUser refreshTokenVerify(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();

        String tokenInRedis = redisTemplate.opsForValue().get(id.toString());

        if (token.equals(tokenInRedis.replace(TOKEN_PREFIX, ""))) {
            User userPS = userRepository.findById(id).orElseThrow(() -> new CustomApiException("유저가 존재하지 않습니다."));
            LoginUser loginUser = new LoginUser(userPS);
            return loginUser;
        } else {
            // 강제 로그아웃
            SecurityContextHolder.getContext().setAuthentication(null);
            return null;
        }


    }
}
