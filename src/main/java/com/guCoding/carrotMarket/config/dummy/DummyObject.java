package com.guCoding.carrotMarket.config.dummy;

import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class DummyObject {
    // protected : 상속 받아야 접근 가능
    protected User newUser(Long id, String phoneNumber, String nickname) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        List<TownEnum> townEnums = new ArrayList<>();
        townEnums.add(TownEnum.삼산동);

        return User.builder()
                .id(id)
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .myHometown(townEnums)
                .password(encPassword)
                .identifier(nickname + "#12AB34")
                .role(UserEnum.USER)
                .build();

    }
}
