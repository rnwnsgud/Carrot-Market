package com.guCoding.carrotMarket.config.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
public class KakaoOAuth2 implements OAuth2Divider{

    private Map<String, Object> attributes;
    private Map<String, Object> propertiesAttributes;
    private Map<String, Object> kakaoAccountAttributes;

    public KakaoOAuth2(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.propertiesAttributes = (Map<String, Object>) attributes.get("properties");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return (String) propertiesAttributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccountAttributes.get("email");
    }
}
