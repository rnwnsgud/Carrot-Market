package com.guCoding.carrotMarket.domain.message;

import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;




@DynamicInsert //  변경하지 않은 필드에 대한 INSERT 문을 생성하지 않고, 변경된 필드에 대해서만 INSERT 문을 생성
@Getter
@Entity
@Table(name = "message_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Builder
    public Message(Long id, String message, User sender, ChatRoom chatRoom) {
        this.id = id;
        this.message = message;
        this.sender = sender;

        this.chatRoom = chatRoom;
    }
}
