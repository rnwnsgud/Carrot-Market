package com.guCoding.carrotMarket.config.oauth;

public interface OAuth2Divider {
    String getProviderId(); //공급자 아이디 ex) google, facebook
    String getProvider(); //공급자 ex) google, facebook
    String getName(); //사용자 이름 ex) ssar
    String getEmail(); //사용자 이메일 ex) ssar@nate.com

}
