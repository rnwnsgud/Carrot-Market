package com.guCoding.carrotMarket.dto.account;

import com.guCoding.carrotMarket.domain.account.Account;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AccountReqDto {

    @Setter
    @Getter
    public static class AccountSaveReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Long password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class AccountChargeReqDto {
        private Long number;
        private Long amount;
    }

    @Setter
    @Getter
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;
        @NotNull
        private Long amount;

    }
}
