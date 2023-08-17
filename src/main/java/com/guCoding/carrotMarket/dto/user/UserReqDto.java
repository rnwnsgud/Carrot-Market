package com.guCoding.carrotMarket.dto.user;

import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UserReqDto {

    @Getter
    @Setter
    public static class JoinReqDto {

        @Schema(example = "ssar")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,10}$", message = "영문/숫자/한글 2~10자 이내로 작성해주세요.")
        @NotEmpty
        private String nickname;

        @Schema(example = "01012345678")
        @Pattern(regexp = "^[0-9]{9,15}$", message = "전화번호 형식으로 입력해주세요.")
        @NotEmpty
        private String phoneNumber;

        @Schema(example = "1234")
        @NotEmpty
        @Size(min = 4, max = 20)
        private String password;

        public User toEntity(BCryptPasswordEncoder bcryptPasswordEncoder, String identifier) {
            List<TownEnum> townEnums = new ArrayList<>();
            townEnums.add(TownEnum.삼산동);
            return User.builder()
                    .nickname(nickname)
                    .password(bcryptPasswordEncoder.encode(password))
                    .phoneNumber(phoneNumber)
                    .identifier(identifier)
                    .role(UserEnum.USER)
                    .townEnums(townEnums)
//                    .role(UserEnum.ADMIN)
                    .mannerTemp(36.5)
                    .build();
        }
    }

    // 당근 마켓 로그인은 전화번호로
    @Getter
    @Setter
    public static class LoginReqDto {
        @Schema(example = "01012345678")
        private String phoneNumber;
        @Schema(example = "1234")
        private String password;
    }

    @Getter
    @Setter
    public static class EditReqDto {
        @Schema(example = "ssar@nate.com")
        @Email
        private String email;
        @Schema(example = "01099998888")
        @Pattern(regexp = "^[0-9]{9,15}$", message = "전화번호 형식으로 입력해주세요.")
        private String phoneNumber;
    }


}
