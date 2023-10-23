package com.guCoding.carrotMarket.handler.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Scope(value = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SocketLogger {

    private String uuid;
    private String requestURL;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        log.debug("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        log.debug("["+uuid+"] websocket scope bean create:" + this);
    }

    @PreDestroy
    public void close() {
        log.debug("["+uuid+"] websocket scope bean close:" + this);
    }
}
