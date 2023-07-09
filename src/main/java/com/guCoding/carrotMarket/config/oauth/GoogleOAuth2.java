package com.guCoding.carrotMarket.config.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
@Getter
@RequiredArgsConstructor
public class GoogleOAuth2 implements OAuth2Divider{

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
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
