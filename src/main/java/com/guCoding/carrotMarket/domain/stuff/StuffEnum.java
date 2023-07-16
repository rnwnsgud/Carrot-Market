package com.guCoding.carrotMarket.domain.stuff;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StuffEnum {
    BUY("삽니다"), ELECTRONIC_EQUIPMENT("전자기기"), FURNITURE("가구/인테리어");
    private String value;


}
