package com.guCoding.carrotMarket.domain.chatroom;

import com.guCoding.carrotMarket.domain.message.Message;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "chatRoom_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stuff_id")
    private Stuff stuff;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, User seller, User customer, Stuff stuff, List<Message> messageList) {
        this.id = id;
        this.seller = seller;
        this.customer = customer;
        this.stuff = stuff;
        this.messageList = messageList;
    }
}
