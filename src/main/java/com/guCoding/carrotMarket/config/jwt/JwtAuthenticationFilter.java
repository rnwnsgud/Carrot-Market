package com.guCoding.carrotMarket.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.dto.user.UserReqDto.LoginReqDto;
import com.guCoding.carrotMarket.dto.user.UserRespDto.LoginRespDto;
import com.guCoding.carrotMarket.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, JwtProvider jwtProvider, StringRedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
        setFilterProcessesUrl("/api/users/login");
        this.authenticationManager = authenticationManager1;
        this.jwtProvider = jwtProvider;
    }

    // post : setFilterProcessesUrl -> /api/login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // req 의 json 데이터 꺼내기
            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

            // 사용자 인증 정보 담고있는 토큰
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getPhoneNumber(), loginReqDto.getPassword());

            // UserDetailsService loadByUsername 호출해서 사용자 조회 후 임시로 세션을 만듬 -> successfulAuthentication 에 임시세션 전달(리턴되면사라짐)
            // authenticationManager 내부에서 PasswordEncoder 를 호출하니 rawPassword 로 인증처리가 됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;

        } catch (Exception e) {
            // unSuccessfulAuthentication 간접적으로 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String accessToken = jwtProvider.accessTokenCreate(loginUser);
        String refreshToken = jwtProvider.refreshTokenCreate(loginUser);
//        log.debug("accessToken " + accessToken);
        response.addHeader("ACCESS_TOKEN", accessToken);
        response.addHeader("REFRESH_TOKEN", refreshToken);
        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser(), accessToken, refreshToken);

        redisTemplate.opsForValue().set(loginUser.getUser().getId().toString(), refreshToken, 1000L * 60 * 60 * 24 * 7, TimeUnit.MILLISECONDS);


        CustomResponseUtil.success(response, loginRespDto);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);
    }
}
