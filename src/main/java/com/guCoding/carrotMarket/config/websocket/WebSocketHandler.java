package com.guCoding.carrotMarket.config.websocket;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 서버 : 클라이언트 = 1 : N, 여러 클라이언트가 발송한 메시지를 받아 처리해줄 핸들러
// STOMP 써서 필요가 없으려나

//@RequiredArgsConstructor
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    // 유저마다 각 채팅방의 세션
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    // 연결 후 발동되는 메서드
//
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        JSONObject jsonObject = new JSONObject();
//
//        log.debug("payload : {}" , payload);
//
//
//    }
//
//
//}
