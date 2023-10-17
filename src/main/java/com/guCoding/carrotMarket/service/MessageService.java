package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.domain.chatroom.ChatRoomRepository;
import com.guCoding.carrotMarket.domain.message.MessageRepository;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.message.MessageDto.MessageSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public void sendMessage(MessageSend messageSend) {
        Optional<User> userOP = userRepository.findById(messageSend.getSenderId());
        messageRepository.save(messageSend.toEntity(userOP.get()));
    }
}
