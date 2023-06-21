package com.guCoding.carrotMarket.dto.user;

import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

public class UserRespDto {

    @Getter
    @Setter
    public static class JoinRespDto {
        private Long id;
        private String nickname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
        }
    }

    @Getter
    @Setter
    public static class LoginRespDto {
        private Long id;
        private String nickname;
        private String createdAt;
        private String jwtToken;

        public LoginRespDto(User user, String jwtToken) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreateDate());
            this.jwtToken = jwtToken;
        }
    }
}
