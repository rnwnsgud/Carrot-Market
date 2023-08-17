package com.guCoding.carrotMarket.dto.stuff;

import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffEnum;
import com.guCoding.carrotMarket.domain.stuff.TransactionEnum;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class StuffReqDto {

    @Getter
    @Setter
    public static class StuffSaveReqDto{ // transaction == "기부" ? price == 0 , price == 0 ? transaction == "기부"
        @Schema(example = "삽니다")
        @NotEmpty
        private String title;

        @Schema(example = "판매하기")
        @NotEmpty
        private String transactionEnum;

        @Schema(example = "10000")
        @NotNull
        private int price;

        @Schema(example = "중고 tv 팝니다.")
        @NotEmpty
        private String description;

        @Schema(example = "true")
        @NotNull
        private boolean gettingPriceOffer;

        @Schema(example = "삼산동")
        @NotEmpty
        private String townEnum; // townEnum

        @Schema(example = "전자기기")
        @NotEmpty
        private String stuffEnum; // stuffEnum

        public Stuff toEntity(User user) {

            TransactionEnum findTransactionEnum = findTransactionEnum(transactionEnum);
            TownEnum findTownEnum = findTownEnum(townEnum);
            StuffEnum findStuffEnum = findStuffEnum(stuffEnum);
            List<StuffEnum> stuffEnums = new ArrayList<>();
            stuffEnums.add(findStuffEnum);

            if (isPriceZero(price)) {
                findTransactionEnum = TransactionEnum.DONATING;
            }

            if (isTxDonate(transactionEnum)) {
                price = 0;
            }

            if (title.equals("삽니다")) {
                stuffEnums.add(StuffEnum.BUY);
            }

            return Stuff.builder()
                    .title(title)
                    .transactionEnum(findTransactionEnum)
                    .price(price)
                    .description(description)
                    .gettingPriceOffer(gettingPriceOffer)
                    .townEnum(findTownEnum)
                    .stuffEnums(stuffEnums)
                    .user(user)
                    .build();
        }
        // 열거형은 각각이 객체 이므로 String과 == 비교는 항상 false 가 나온다.

        private TransactionEnum findTransactionEnum(String transactionEnum) {
            for (TransactionEnum transactionEnums : TransactionEnum.values()) {
                if (transactionEnums.getValue().equals(transactionEnum)) {
                    return transactionEnums;
                }
            }
            return null;
        }

        private TownEnum findTownEnum(String townEnum) {
            for (TownEnum townEnums: TownEnum.values()) {
                if (townEnums.name().equals(townEnum)) {
                    return townEnums;
                }
            }
            return null;
        }

        private StuffEnum findStuffEnum(String stuffEnum) {
            for (StuffEnum stuffEnums : StuffEnum.values()) {
                if (stuffEnums.getValue().equals(stuffEnum)) {
                    return stuffEnums;
                }
            }
            return null;
        }

        public boolean isPriceZero(int price) {
            if (price == 0) {
                return true;
            }
            return false;
        }

        public boolean isTxDonate(String transaction) {
            if (transaction.equals("나눔하기")) {
                return true;
            }
            return false;
        }
    }

    @Getter
    @Setter
    public static class StuffInquireReqDto {
        @Schema(example = "삼산동")
        @NotEmpty
        TownEnum stuffName;
    }


}
