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
        private String accessToken;
        private String refreshToken;

        public LoginRespDto(User user, String accessToken, String refreshToken) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreateDate());
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
