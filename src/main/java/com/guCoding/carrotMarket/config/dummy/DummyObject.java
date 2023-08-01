package com.guCoding.carrotMarket.config.dummy;

import com.guCoding.carrotMarket.domain.account.Account;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffEnum;
import com.guCoding.carrotMarket.domain.stuff.TransactionEnum;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
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
                .townEnums(townEnums)
                .password(encPassword)
                .identifier(nickname + "#12AB34")
                .role(UserEnum.USER)
                .build();

    }

    protected Stuff newMockStuff(Long id, String title, int price, String description, boolean gettingPriceOffer, User user) {

        List<StuffEnum> stuffEnums = new ArrayList<>();
        stuffEnums.add(StuffEnum.FURNITURE);

        return Stuff.builder()
                .id(id)
                .title(title)
                .transactionEnum(TransactionEnum.SELLING)
                .townEnum(TownEnum.삼산동)
                .stuffEnums(stuffEnums)
                .price(price)
                .description(description)
                .gettingPriceOffer(gettingPriceOffer)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .build();
    }

}
