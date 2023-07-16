package com.guCoding.carrotMarket.dto.stuff;

import com.guCoding.carrotMarket.domain.stuff.Stuff;
import lombok.Getter;
import lombok.Setter;

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
}
