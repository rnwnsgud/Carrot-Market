package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.config.dummy.DummyObject;
import com.guCoding.carrotMarket.domain.account.Account;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.dto.account.AccountReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountTransferReqDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {

    @Test
    public void 판매하기_test() throws Exception {
        //given
        Long userId = 1L;

        User ssar = newUser(1L, "01012345678", "ssar");
        User cos = newUser(2L, "010999900000", "cos");
        Account depositAccount = newMockAccount(1L, 1234L, 0L, ssar);
        Account withdrawAccount = newMockAccount(2L, 9999L, 10000L, cos);

        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setAmount(5000L);
        accountTransferReqDto.setDepositNumber(depositAccount.getNumber());
        accountTransferReqDto.setWithdrawNumber(withdrawAccount.getNumber());
        accountTransferReqDto.setWithdrawPassword(1234L);
        //when
        if (accountTransferReqDto.getDepositNumber().longValue() == accountTransferReqDto.getWithdrawNumber().longValue()) {
            throw new CustomApiException("출입금 계좌가 동일 할 수 없습니다.");
        }

        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        withdrawAccount.checkOwner(userId);
        withdrawAccount.checkPassword(withdrawAccount.getPassword());
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

        depositAccount.chargeBalance(accountTransferReqDto.getAmount());
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());

        //then
        assertThat(depositAccount.getBalance()).isEqualTo(5000L);
        assertThat(withdrawAccount.getBalance()).isEqualTo(5000L);
    }
}
