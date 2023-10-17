package com.guCoding.carrotMarket.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



// WebSocket 만으로 채팅을 구현하면 해당 메시지가 어떤 요청인지(입장 전,후) 어떻게 처리하는지에 따라
// 채팅방과 세션을 일일이 구현하고 메세지 발송 부분을 관리하는 추가 코드를 구현해야 한다.
// STOMP 를 사용해서 메시지를 효율적으로 구현한다.
//*웹 소켓 메시지 브로커 설정파일* 안해주면 SimpMessagingTemplate 인식못함
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-chat").setAllowedOriginPatterns("*");
        // setAllowedOrigins 말고 이거 쓰면 * 사용가능
        // .withSocketJs(); -> apic, stomp
        // websocket or sockJs가 핸드셰이크 커넥션을 생성할 경로
        // 실무에서는 정확한 도메인주소 지정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setApplicationDestinationPrefixes("/publish");
        registry.enableSimpleBroker("/subscribe");
        // 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅
        // Spring docs에서는 /topic, /queue로 예시를 들음. (1:1, 1:N)
        // SimpleBroker 등록, 해당하는 경로를 SUBSCRIBE하는 Client에게 메시지를 전달함.
        // enableStompBrokerRelay : SimpleBroker의 기능과 외부 message broker(RabbitMQ, ActiveMQ 등)에 메시지를 전달하는 기능

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler); // 인터셉터(중간에 가로채서 로직처리하는 친구) 등록 (JWT 토큰 검증)
    }


}
