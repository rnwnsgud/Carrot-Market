package com.guCoding.carrotMarket.config;

import com.guCoding.carrotMarket.config.oauth.OAuth2DetailsService;
import com.guCoding.carrotMarket.config.jwt.JwtAuthenticationFilter;
import com.guCoding.carrotMarket.config.jwt.JwtAuthorizationFilter;
import com.guCoding.carrotMarket.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final OAuth2DetailsService oAuth2DetailsService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("filterChain 등록");
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.cors().configurationSource(configurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        http.oauth2Login().userInfoEndpoint().userService(oAuth2DetailsService);
        // userInfoEndpoint : 인증코드-> accesstoken 받는대신에 바로 회원정보 받기
        // userService : 회원정보를 받는 곳

        // 필터 적용
        http.apply(new CustomSecurityFilterManager());

        // 인증실패 예외 가로채기
        http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
            log.error("error : " + e.getMessage());
            CustomResponseUtil.fail(response, "로그인을 진행 해주세요 .", HttpStatus.UNAUTHORIZED);
        });

        // 권한실패 예외 가로채기
        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            log.error("error : " + e.getMessage());
            CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
        });

        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .anyRequest().permitAll();

        return http.build();
    }

    // 필터 등록
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception { // 스프링 시큐리티에서 사용되는 구성을 위한 빌더 클래스
            builder.addFilter(jwtAuthenticationFilter);
            builder.addFilter(jwtAuthorizationFilter);
            super.configure(builder);
        }
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*"); // 모든 ip 주소 허용 (프론트 엔드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트의 쿠키 요청 허용
        configuration.addExposedHeader("ACCESS_TOKEN"); // 브라우저에 Authorization 헤더 노출, 클라이언트가 저장하기 위해서
        configuration.addExposedHeader("REFRESH_TOKEN"); // 브라우저에 REFRESH_TOKEN 헤더 노출, 클라이언트가 저장하기 위해서
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 주소요청에 위 설정을 넣어주겠다.
        return source;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Arrays.asList(
                naverClientRegistration(), googleClientRegistration(), kakaoClientRegistration()
        ));
    }

    @Bean
    public ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
                .clientId("oyDyrNgKIKkKPsKzIESp")
                .clientSecret("ztzbpmbqnv")
                .clientName("Naver")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/login/oauth2/code/naver")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .build();
    }

    @Bean
    public ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("1073676359956-206aeun04thjg7kjuf1nt8apgkegkc4h.apps.googleusercontent.com")
                .clientSecret("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")
                .clientName("Google")
                .scope("email", "profile")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .redirectUri("http://localhost:8081/login/oauth2/code/google")
                .clientName("Google")
                .build();
    }

    @Bean
    public ClientRegistration kakaoClientRegistration() {
        return ClientRegistration.withRegistrationId("kakao")
                .clientId("40ac64076eddb6ddb270dce872e19935")
                .clientName("Kakao")
                .scope("profile_nickname", "profile_image", "account_email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .redirectUri("http://localhost:8081/login/oauth2/code/kakao")
                .build();
    }
}



