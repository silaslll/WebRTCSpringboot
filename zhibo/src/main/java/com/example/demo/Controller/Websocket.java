package com.example.demo.Controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Vector;
 
@ServerEndpoint("/webSocket/{userno}")
@Component
public class Websocket {
 
    private Session session;
    private String userno = "";
    private static Vector<Websocket> clients = new Vector<Websocket>();
    @OnOpen
    public void onOpen(@PathParam(value = "userno") String param,Session session){
        this.session=session;
        userno = param;
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
		for (Websocket client:clients) {   
			try {                 //判断接收用户是否是当前发消息的用户               
				client.session.getBasicRemote().sendText(userno+"说："+message);  
//				}
			} catch (IOException e) {  
				e.printStackTrace();         
				}        
			}      
		}
	

	 
	
	}

	
