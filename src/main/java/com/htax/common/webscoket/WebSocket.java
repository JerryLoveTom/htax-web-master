package com.htax.common.webscoket;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/webSocket")
@Component
public class WebSocket {

    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocket> clients = new CopyOnWriteArraySet<>();
    private Session session;
    private String username;
    private Logger logger = LoggerFactory.getLogger(WebSocket.class);


    @OnOpen
    public void onOpen(Session session){
        this.username = "admin";
        this.session = session;
        clients.add(this);
        logger.info("webSocketInfo --- onOpen ==> "+this.username+" connected");
    }

    @OnClose
    public void onClose(){
        logger.info("webSocketInfo --- onClose ==> "+this.username+" closed");
        clients.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("webSocketInfo --- onMessage ==> msg:" + message);
        sendMessage("接收到消息了----" + message);
    }

    // 指定用户发送消息
    public void sendMessage(String message, String username) throws IOException {
        for (WebSocket socket : clients) {
            if (socket.username.equals(username)) {
                socket.session.getBasicRemote().sendText(message);
            }
        }
    }

    // 群送消息
    public synchronized void sendMessage(String message) throws IOException {
        for (WebSocket socket : clients) {
            socket.session.getBasicRemote().sendText(message);
        }
    }
    // 群送消息
    public synchronized void sendMessage(WsMessage message) throws IOException {
        logger.info("webSocketInfo --- sendMessage ==> msg:" + message.getCode() +", data:" + message.getData());
        for (WebSocket socket : clients) {
            socket.session.getBasicRemote().sendText(JSONObject.toJSONString(message));
        }
    }
}
