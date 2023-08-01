package com.guCoding.carrotMarket.dto.account;

import com.guCoding.carrotMarket.domain.account.Account;
import com.guCoding.carrotMarket.domain.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Setter
    @Getter
    public static class AccountChargeRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountChargeRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Setter
    @Getter
    public static class AccountTransferRespDto {
        private Long depositBalance;
        private Long withdrawBalance;
        private TransactionDto transactiondto;

        public AccountTransferRespDto(Long depositBalance, Long withdrawBalance, Transaction transaction) {
            this.depositBalance = depositBalance;
            this.withdrawBalance = withdrawBalance;
            this.transactiondto = new TransactionDto(transaction);
        }

        public class TransactionDto {
            private Long id;
            private String sender;
            private String receiver;
            private Long balance;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.balance = transaction.getDepositAccountBalance();
            }
        }
    }
}
