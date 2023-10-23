package com.guCoding.carrotMarket.config.websocket;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import com.guCoding.carrotMarket.config.jwt.JwtProvider;
import com.guCoding.carrotMarket.handler.ex.CustomApiException;
import com.guCoding.carrotMarket.handler.log.SocketLogger;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.token_prefix:null}")
    private String TOKEN_PREFIX;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JwtProvider jwtProvider;
    private final SocketLogger socketLogger;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 로그 찍히나 확인
        log.debug("preSend 진입");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        socketLogger.setRequestURL(destination);
        socketLogger.log("ChannelInterceptor 로그 테스트");

        log.debug("accessor.getCommand {}", accessor);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            log.debug("CONNECT");
            List<String> nativeHeader = accessor.getNativeHeader("access_token");
            log.debug("nativeHeader {}", nativeHeader);
            String accessToken = nativeHeader.get(0).replace(TOKEN_PREFIX, "");
            log.debug("accessToken {}" , accessToken);

            jwtProvider.accessTokenVerify(accessToken);

        }

        return message;
    }
}
