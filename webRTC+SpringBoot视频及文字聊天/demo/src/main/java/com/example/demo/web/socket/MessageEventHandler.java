package com.example.demo.web.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.demo.po.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageEventHandler {

    private static Logger log = LoggerFactory.getLogger(MessageEventHandler.class);

    private final SocketIOServer server;

    private static final int USER_COUNT = 3;

    private static List<String> userNames = new ArrayList<>();

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
        userNames.add("张三");
        userNames.add("李四");
    }

    /**
     * connect事件处理，当客户端发起连接时将调用
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("客户端加入连接。。。");
    }

    /**
     * disconnect事件处理，当客户端断开连接时将调用
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端断开连接。。。");
    }

    /**
     * 消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
     *
     * @param client
     * @param request
     */
    @OnEvent(value = "join")
    public void join(SocketIOClient client, AckRequest request, ClientInfo clientInfo) {
        log.info(clientInfo.toString());
        //加入房间
        client.joinRoom(clientInfo.getRoomId());
        if (clientInfo.getUserName() == null || "".equals(clientInfo.getUserName().trim())) {
            clientInfo.setUserName("测试" + client.getSessionId());
        }
        BroadcastOperations roomOperations = server.getRoomOperations(clientInfo.getRoomId());
        Collection<SocketIOClient> clients = roomOperations.getClients();
        if (clients.size() < USER_COUNT) {
            //给当前客户端发送消息
            client.sendEvent("joined", clientInfo.getRoomId(), clientInfo.getUserName(), client.getSessionId());
            if (clients.size() > 1) {
                roomOperations.sendEvent("otherjoin", client, clientInfo.getRoomId());
            }
        } else {
            //踢出房间
            client.leaveRoom(clientInfo.getRoomId());
            //给当前客户端发送消息
            client.sendEvent("full", clientInfo.getRoomId(), client.getSessionId());
        }
    }

    @OnEvent(value = "leave")
    public void leave(SocketIOClient client, AckRequest request, ClientInfo clientInfo) {
        log.info(clientInfo.toString());
        //踢出房间
        client.leaveRoom(clientInfo.getRoomId());
        BroadcastOperations roomOperations = server.getRoomOperations(clientInfo.getRoomId());
        //给除自己之外的所有人发送消息
        roomOperations.sendEvent("bye", client, clientInfo.getRoomId(), client.getSessionId());
        //给当前客户端发送消息
        client.sendEvent("leaved", clientInfo.getRoomId(), client.getSessionId());
    }

    @OnEvent(value = "message")
    public void message(SocketIOClient client, AckRequest request, ClientInfo clientInfo) {
        log.info(clientInfo.toString());
        BroadcastOperations roomOperations = server.getRoomOperations(clientInfo.getRoomId());
        //给除自己之外的所有人发送消息
        roomOperations.sendEvent("message", client, clientInfo.getRoomId(), clientInfo.getData());
    }

    @OnEvent(value = "msg")
    public void msg(SocketIOClient client, AckRequest request, ClientInfo clientInfo) {
        log.info(clientInfo.toString());
        BroadcastOperations roomOperations = server.getRoomOperations(clientInfo.getRoomId());
        //给当前客户端发送消息
        client.sendEvent("own_msg", clientInfo.getRoomId(), clientInfo.getUserName(), clientInfo.getStrDate(), clientInfo.getMsg());
        //给除自己之外的所有人发送消息
        roomOperations.sendEvent("msg", client, clientInfo.getRoomId(), clientInfo.getUserName(), clientInfo.getStrDate(), clientInfo.getMsg());
    }

}