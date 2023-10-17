package com.guCoding.carrotMarket.dto.message;

import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.domain.message.Message;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class MessageDto {

    @Getter
    public static class MessageSend {
        private String message;
        private Long senderId;

        public Message toEntity(User sender) {
            return Message.builder()
                    .message(message)
                    .sender(sender)
                    .build();
        }
    }

    @Getter
    public static class MessageResponse {
        private String message;
        private Long senderId;
        private LocalDateTime sendTime;

        public MessageResponse(String message, Long senderId, LocalDateTime sendTime) {
            this.message = message;
            this.senderId = senderId;
            this.sendTime = sendTime;
        }
    }
}
