package com.guCoding.carrotMarket.dto.stuff;

import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StuffRespDto {

    @Getter
    @Setter
    public static class StuffSaveRespDto {
        private String title;
        private String transaction;
        private int price;
        private String description;
        private String txPlace;

        public StuffSaveRespDto(Stuff stuff, String txPlace) {
            this.title = stuff.getTitle();
            this.transaction = stuff.getTransactionEnum().getValue();
            this.price = stuff.getPrice();
            this.description = stuff.getDescription();
            this.txPlace = txPlace;
        }
    }

    @Getter
    @Setter
    public static class StuffInquireRespDto{
        List<StuffInquireDto> stuffInquireDtoList;

        public StuffInquireRespDto(List<Stuff> stuffList) {
            this.stuffInquireDtoList = stuffList.stream().map(StuffInquireDto::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class StuffInquireDto {
            private Long id;
            private String title;
            private TownEnum townEnum;
            private LocalDateTime createdAt;
            private int price;

            public StuffInquireDto(Stuff stuff) {
                this.id = stuff.getId();
                this.title = stuff.getTitle();
                this.townEnum = stuff.getTownEnum();
                this.createdAt = stuff.getCreateDate();
                this.price = stuff.getPrice();
            }
        }
    }
}
