package com.tianyl.demo.web;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 配置 configurator = SpringConfigurator.class ,
 * Tomcat会使用SpringConfigurator来获取bean，SpringConfigurator类中依赖了WebApplicationContext导致获取bean失败
 * 默认的configurator每次都会创建一个bean，使@Autowired失效
 */

@Slf4j
@ServerEndpoint(value = "/ws")
@Component
public class WebSocketServer {

    private static final List<Session> ALL_SESSION = new CopyOnWriteArrayList<>();

//    @Autowired
//    private UserService userService;

    @OnOpen
    public void onOpen(Session session) {
        ALL_SESSION.add(session);
        log.info("创建新连接:" + session.getId());
//        System.out.println(userService.getUsers());
    }

    @OnClose
    public void onClose(Session session) {
        ALL_SESSION.remove(session);
        log.info("关闭连接:" + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable e) {
        ALL_SESSION.remove(session);
        log.error("出现异常", e);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        log.info("收到消息:" + msg);
        // 回复消息
        String newMsg = msg.toUpperCase();
        sendMsg(session, newMsg);
    }

    private void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.error("消息发送异常");
            ALL_SESSION.remove(session);
        }
    }
}
