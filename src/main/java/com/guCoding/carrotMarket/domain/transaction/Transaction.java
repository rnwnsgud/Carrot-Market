package com.guCoding.carrotMarket.domain.transaction;

import com.guCoding.carrotMarket.domain.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "transaction_tb")
@Getter
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account withdrawAccount; // null 허용됨

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account depositAccount; // null 허용됨

    @Column(nullable = false)
    private Long amount;

    private Long withdrawAccountBalance; // 1111 계좌 -> 1000원 -> 500원 -> 200원
    private Long depositAccountBalance;

    // 계좌가 사라져도 로그는 남아야 한다.
    private String sender; // 1111계좌 or ATM
    private String receiver; // 2222계좌 or ATM

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance, Long depositAccountBalance, String sender, String receiver) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.sender = sender;
        this.receiver = receiver;
    }
}
