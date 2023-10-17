package com.guCoding.carrotMarket.dto.chatroom;

import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.Getter;

public class ChatRoomDto {

    @Getter
    public static class ChatRoomReq {
        private Long sellerId;
        private Long customerId;
        private Long stuffId;

        public ChatRoom toEntity(User seller, User customer, Stuff stuff) {


            return ChatRoom.builder()
                    .seller(seller)
                    .customer(customer)
                    .stuff(stuff)
                    .build();
        }
    }

    @Getter
    public static class ChatRoomResp {
        private Long id;
        private Long sellerId;
        private Long customerId;
        private Long stuffId;

        public ChatRoomResp(Long id, Long sellerId, Long customerId, Long stuffId) {
            this.id = id;
            this.sellerId = sellerId;
            this.customerId = customerId;
            this.stuffId = stuffId;
        }
    }
}
