package com.guCoding.carrotMarket.config.oauth;

import com.guCoding.carrotMarket.config.auth.LoginUser;
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

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.debug("oauth2.getAttributes = " + oAuth2User.getAttributes());

        OAuth2Divider userInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        log.debug("OAuth2 서비스 id(구글,카카오,네이버) : " + registrationId);
        if (registrationId.equals("google")) {
            userInfo = new GoogleOAuth2(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            userInfo = new NaverOAuth2((Map) oAuth2User.getAttributes().get("response"));
        }

        String nickname = "USER_" + userInfo.getName();
        String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
        String email = userInfo.getEmail();
        String username = userInfo.getName();
        String mobile = null;

        if (userInfo instanceof NaverOAuth2) {
            NaverOAuth2 naverInfo = (NaverOAuth2) userInfo;
            mobile = naverInfo.getPhoneNumber();
        }

        Optional<User> userOP = userRepository.findByEmail(userInfo.getEmail());

        if (userOP.isEmpty()) {
            String returnIdentifier = randomIdentifier("USER_"+userInfo.getProviderId(), userRepository);

            User user = getUser(nickname, password, email, returnIdentifier, mobile, username);
            User userPS = userRepository.save(user);

            LoginUser loginUser = new LoginUser(userPS, oAuth2User.getAttributes());

            String accessToken = jwtProvider.accessTokenCreate(loginUser);
            String refreshToken = jwtProvider.refreshTokenCreate(loginUser);

            saveTokenAndLogging(loginUser, accessToken, refreshToken);
            return loginUser;

        } else {
            User user = userOP.get();
            LoginUser loginUser = new LoginUser(user, oAuth2User.getAttributes());
            String accessToken = jwtProvider.accessTokenCreate(loginUser);
            String refreshToken = jwtProvider.refreshTokenCreate(loginUser);

            saveTokenAndLogging(loginUser, accessToken, refreshToken);
            return loginUser;
        }

    }

    private User getUser(String nickname, String password, String email, String returnIdentifier, String phoneNumber, String username) {
        List<TownEnum> townEnums = new ArrayList<>();
        townEnums.add(TownEnum.삼산동);
        User user = User.builder()
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .identifier(returnIdentifier)
                .email(email)
                .role(UserEnum.USER)
                .myHometown(townEnums)
                .phoneNumber(phoneNumber)
                .username(username)
                .build();
        return user;
    }

    private void saveTokenAndLogging(LoginUser loginUser, String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set(loginUser.getUser().getId().toString(), refreshToken, 1000L * 60 * 60 * 24 * 7, TimeUnit.MILLISECONDS);
        log.debug("accessToken " + accessToken);
        log.debug("refreshToken " + refreshToken);
    }

    private String randomIdentifier(String nickname, UserRepository userRepository) {
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
        return identifier;
    }

    
}
