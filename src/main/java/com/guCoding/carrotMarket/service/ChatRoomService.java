package com.guCoding.carrotMarket.service;

import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.domain.chatroom.ChatRoomRepository;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.stuff.StuffRepository;
import com.guCoding.carrotMarket.domain.user.User;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.chatroom.ChatRoomDto;
import com.guCoding.carrotMarket.dto.chatroom.ChatRoomDto.ChatRoomReq;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final StuffRepository stuffRepository;

    public Long joinChatRoom(ChatRoomReq chatRoomReq) {
        if (chatRoomReq.getCustomerId().equals(chatRoomReq.getSellerId())) {
            throw new CustomApiException("자신과의 채팅방은 만들 수 없습니다.");
        }
        Optional<ChatRoom> chatOP = chatRoomRepository.findUsersAndStuff(chatRoomReq.getCustomerId(), chatRoomReq.getSellerId(), chatRoomReq.getStuffId());
        if (chatOP.isPresent()) {
            return chatOP.get().getId();
        } else {// 나중에 수정
            Optional<User> sellerOP = userRepository.findById(chatRoomReq.getSellerId());
            Optional<User> customerOP = userRepository.findById(chatRoomReq.getCustomerId());
            Optional<Stuff> stuffOP = stuffRepository.findById(chatRoomReq.getStuffId());
            ChatRoom chatRoomPS = chatRoomRepository.save(chatRoomReq.toEntity(sellerOP.get(), customerOP.get(), stuffOP.get()));
            return chatRoomPS.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getMyRoomList(Long userId) {
        return chatRoomRepository.findMyChat(userId);
    }
}
