package com.guCoding.carrotMarket.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.guCoding.carrotMarket.config.auth.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;


    @Value("${jwt.access_header:null}")
    private String ACCESS_HEADER;

    @Value("${jwt.refresh_header:null}")
    private String REFRESH_HEADER;

    @Value("${jwt.token_prefix:null}")
    private String TOKEN_PREFIX;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, StringRedisTemplate redisTemplate) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            log.debug("디버그 : 토큰이 존재함");
            String accessToken = request.getHeader(ACCESS_HEADER).replace(TOKEN_PREFIX, "");
            String refreshToken = request.getHeader(REFRESH_HEADER).replace(TOKEN_PREFIX, "");
            log.debug("accessToken " + accessToken);
            log.debug("refreshToken " + refreshToken);

            // accessToken 검증이 완료되면 SecurityContext 세션 생성
            try {
                LoginUser loginUser = jwtProvider.accessTokenVerify(accessToken);
                log.debug("디버그 : 토큰이 검증이 완료됨");

                // 임시 세션 (UserDetails 타입 or username) id,role 만 존재
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("디버그 : 임시 세션이 생성됨");
            } catch (TokenExpiredException e) {
                // accessToken 검증에 실패하면 refreshToken 검증 후 재발급
                log.debug("Access Token 오류");
                LoginUser loginUser = jwtProvider.refreshTokenVerify(refreshToken);

                String reissuedAccessToken = jwtProvider.accessTokenCreate(loginUser);
                String reissuedRefreshToken = jwtProvider.refreshTokenCreate(loginUser);

                response.addHeader("ACCESS_TOKEN", reissuedAccessToken);
                response.addHeader("REFRESH_TOKEN", reissuedRefreshToken);

                redisTemplate.opsForValue().set(loginUser.getUser().getId().toString(), reissuedRefreshToken, 1000L * 60 * 60 * 24 * 7, TimeUnit.MILLISECONDS);
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("디버그 : 임시 세션 수정됨");

            }

        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String accessHeader = request.getHeader(ACCESS_HEADER);

        if (accessHeader == null || !accessHeader.startsWith(TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }

}
