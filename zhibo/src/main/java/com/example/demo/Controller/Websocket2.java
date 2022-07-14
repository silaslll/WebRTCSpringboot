package com.example.demo.Controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
 
@ServerEndpoint("/webSocket2")
@Component
public class Websocket2 {
 
    private Session session;
    
    private static Vector<Websocket2> clients = new Vector<Websocket2>();
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        clients.add(this);
        System.out.println("有新的连接");
    }
 
    @OnClose
    public void onClose(){
    	clients.remove(this);
        System.out.println("有新的断开");
    }
 
    @OnMessage
    public void onMessage(String message){
 
        System.out.println(message);
 
        sendAll(message);
    }
 
    
	/**      * 给所有人发消息      * @param message      */     
	private void sendAll(String message) {
		//遍历HashMap         
		for (Websocket2 client:clients) {   
			try {                 //判断接收用户是否是当前发消息的用户               
				client.session.getBasicRemote().sendText(message);  
//				}
			} catch (IOException e) {  
				e.printStackTrace();         
				}        
			}      
		}
	}
