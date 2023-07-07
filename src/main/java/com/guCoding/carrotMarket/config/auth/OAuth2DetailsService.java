package com.guCoding.carrotMarket.config.auth;

import com.guCoding.carrotMarket.config.jwt.JwtProvider;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.user.UserRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.debug("userRequest : " + userRequest);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.debug("oauth2.getAttributes = " + oAuth2User.getAttributes());

        // {sub=102573487300504256611, name=구준형, given_name=준형, family_name=구,
        // picture=https://lh3.googleusercontent.com/a/AAcHTteV7VG0a4KwuJ46NMCjKXlm-SLUSEadVrBXCDQaY7Wz7wk=s96-c,
        // email=rnwnsgud4036@gmail.com, email_verified=true, locale=ko}

        Map<String, Object> userInfo = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.debug("OAuth2 서비스 id(구글,카카오,네이버) : " + registrationId);
        // google , naver , kakao
        String nickname = "USER_" + (String) userInfo.get("sub");
        String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
        String email = (String) userInfo.get("email");

        Optional<User> userOP = userRepository.findByEmail(email);
        if (userOP.isEmpty()) {
            boolean isPresent = true;
            Random random = new Random();
            String identifier = null;
            while (isPresent) {

                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                int length = 6;  // 조합된 문자열의 길이

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(characters.length());
                    sb.append(characters.charAt(index));
                }
                String randomString = sb.toString();
                identifier = nickname + "#" + randomString;


                Optional<User> userIdentityOP = userRepository.findByIdentifier(identifier); //닉네임중복되면서 태그값 달리할 수 없어서
                if (!userIdentityOP.isPresent()) {
                    isPresent = false;
                }
            }

            List<TownEnum> townEnums = new ArrayList<>();
            townEnums.add(TownEnum.삼산동);
            User user = User.builder()
                    .nickname(nickname)
                    .password(bCryptPasswordEncoder.encode(password))
                    .identifier(identifier)
                    .email(email)
                    .role(UserEnum.USER)
                    .myHometown(townEnums)
                    .build();
            User userPS = userRepository.save(user);
            LoginUser loginUser = new LoginUser(userPS, userInfo);
            String accessToken = jwtProvider.accessTokenCreate(loginUser);
            String refreshToken = jwtProvider.refreshTokenCreate(loginUser);

            redisTemplate.opsForValue().set(loginUser.getUser().getId().toString(), refreshToken, 1000L * 60 * 60 * 24 * 7, TimeUnit.MILLISECONDS);
            log.debug("accessToken " + accessToken);
            log.debug("refreshToken " + refreshToken);
            return loginUser;

        } else {
            User user = userOP.get();
            LoginUser loginUser = new LoginUser(user, userInfo);
            String accessToken = jwtProvider.accessTokenCreate(loginUser);
            String refreshToken = jwtProvider.refreshTokenCreate(loginUser);

            redisTemplate.opsForValue().set(loginUser.getUser().getId().toString(), refreshToken, 1000L * 60 * 60 * 24 * 7, TimeUnit.MILLISECONDS);
            log.debug("accessToken " + accessToken);
            log.debug("refreshToken " + refreshToken);
            return loginUser;
        }

    }
    
}
