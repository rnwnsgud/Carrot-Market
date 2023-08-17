package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.account.Account;
import com.guCoding.carrotMarket.domain.account.AccountRepository;
import com.guCoding.carrotMarket.domain.stuff.TransactionEnum;
import com.guCoding.carrotMarket.domain.transaction.Transaction;
import com.guCoding.carrotMarket.domain.transaction.TransactionRepository;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.account.AccountReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountChargeReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountSaveReqDto;
import com.guCoding.carrotMarket.dto.account.AccountReqDto.AccountTransferReqDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountChargeRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountSaveRespDto;
import com.guCoding.carrotMarket.dto.account.AccountRespDto.AccountTransferRespDto;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountSaveRespDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {

        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("해당 id에 해당하는 유저가 존재하지 않습니다."));

        Account account = accountSaveReqDto.toEntity(userPS);

        Account accountPS = accountRepository.save(account);

        return new AccountSaveRespDto(accountPS);
    }

    // 계좌 충전 (ATM -> 당근마켓계좌)

    public AccountChargeRespDto 계좌충전(AccountChargeReqDto accountChargeReqDto) {
        Long number = accountChargeReqDto.getNumber();
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(() -> new CustomApiException("해당 번호의 계좌는 존재하지 않습니다."));
        accountPS.chargeBalance(accountChargeReqDto.getNumber());
        return new AccountChargeRespDto(accountPS);
    }

    // 계좌 전송
    public AccountTransferRespDto 계좌전송(AccountTransferReqDto accountTransferReqDto, Long userId) {

        if (accountTransferReqDto.getDepositNumber().longValue() == accountTransferReqDto.getWithdrawNumber().longValue()) {
            throw new CustomApiException("출입금 계좌가 동일 할 수 없습니다.");
        }

        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber()).orElseThrow(() -> new CustomApiException("해당 번호의 계좌는 존재하지 않습니다."));
        Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber()).orElseThrow(() -> new CustomApiException("해당 번호의 계좌는 존재하지 않습니다."));

        // 출금 소유자 확인
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkPassword(accountTransferReqDto.getWithdrawPassword());

        // 출금잔액 확인
        withdrawAccountPS.checkBalance(accountTransferReqDto.getAmount());

        depositAccountPS.chargeBalance(accountTransferReqDto.getAmount());
        withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .sender(accountTransferReqDto.getWithdrawNumber() + "")
                .receiver(accountTransferReqDto.getDepositNumber() + "")
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountTransferRespDto(depositAccountPS.getBalance() , withdrawAccountPS.getBalance(), transactionPS);
    }
}