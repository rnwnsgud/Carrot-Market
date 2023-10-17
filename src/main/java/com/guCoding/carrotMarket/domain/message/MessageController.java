package com.guCoding.carrotMarket.domain.message;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.domain.chatroom.ChatRoom;
import com.guCoding.carrotMarket.dto.ResponseDto;
import com.guCoding.carrotMarket.dto.chatroom.ChatRoomDto;
import com.guCoding.carrotMarket.dto.chatroom.ChatRoomDto.ChatRoomReq;
import com.guCoding.carrotMarket.dto.message.MessageDto;
import com.guCoding.carrotMarket.dto.message.MessageDto.MessageSend;
import com.guCoding.carrotMarket.service.ChatRoomService;
import com.guCoding.carrotMarket.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
//@CrossOrigin
@RestController
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate; // 특정 브로커로 메시지 전달
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 클라이언트는 prefix를 붙여서 /publish/chat/message 로 send // 기존의 WebSocketHandler 역할을 대신 해준다.
    @MessageMapping("/chat/message/{id}")
    public void sendMassage(@DestinationVariable("id") Long id, MessageSend messageSend, @Header("access_token") String accessToken) {
        log.debug("AccessToken {}", accessToken);
        messageService.sendMessage(messageSend);
        simpMessagingTemplate.convertAndSend("/subscribe/chatroom/" + id, messageSend.getMessage());
    }

    @PostMapping("/chat/room")
    public ResponseEntity<?> chatEnter(@RequestBody ChatRoomReq chatRoomReq) {
        Long roomId = chatRoomService.joinChatRoom(chatRoomReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방생성성공", roomId), HttpStatus.CREATED);
    }

    @GetMapping("/chat/room")
    public ResponseEntity<?> getChatRoomList(@AuthenticationPrincipal LoginUser loginUser) {
        List<ChatRoom> myRoomList = chatRoomService.getMyRoomList(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방리스트조회성공", myRoomList), HttpStatus.OK);
    }
}
