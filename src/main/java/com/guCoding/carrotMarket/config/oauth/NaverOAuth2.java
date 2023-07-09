package com.guCoding.carrotMarket.config.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class NaverOAuth2 implements OAuth2Divider{

    private final Map<String, Object> attributes;

    public String getPhoneNumber() {
        return (String) attributes.get("mobile");
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
