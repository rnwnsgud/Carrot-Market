package com.guCoding.carrotMarket.domain.account;

import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "account_tb", indexes = {
        @Index(name = "idx_account_number", columnList = "number") // 특정 컬럼에 대한 검색쿼리 성능향상
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 4) // 수정함
    private Long number; // 계좌번호
    @Column(nullable = false, length = 4)
    private Long password; // 계좌비번
    @Column(nullable = false)
    private Long balance; // 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // user_id

    @Builder
    public Account(Long id, Long number, Long password, Long balance, User user) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
    }

    public void chargeBalance(Long amount) {
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        this.balance -= amount;
    }

    public void checkOwner(Long userId) {
        if (userId.longValue() == user.getId().longValue()) {
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void checkPassword(Long password) {
        if (this.password.longValue() != password.longValue()) {
            throw new CustomApiException("비밀번호가 다릅니다.");
        }
    }

    public void checkBalance(Long amount) {
        if (this.balance < amount) {
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
    }
}
