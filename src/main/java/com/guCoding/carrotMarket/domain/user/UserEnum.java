package com.guCoding.carrotMarket.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserEnum {
    USER("사용자"), ADMIN("관리자");
    private String value;
}
