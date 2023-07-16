package com.guCoding.carrotMarket.domain.stuff;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionEnum {
    SELLING("판매하기"), DONATING("나눔하기");
    private String value;
}
