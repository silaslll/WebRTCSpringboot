package com.live.servet;

import java.io.IOException;
import java.util.Vector;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
/**
 * 路径
 *
 *
 */
@ServerEndpoint(value="/chatroom")
public class ChatRoom {

	//每次浏览器与服务器建立连接时,会创建videoServer
	//客户端列表集合   clients -- 表示客户端
	private static Vector<ChatRoom> chats = new Vector<ChatRoom>();
	
	//当前连接的会话对象
	private Session session;
	//当连接打开时(执行每件事情时)
	@OnOpen
	public void onOpen(Session session){
		//建立连接时
		this.session=session;
		//将当前对象添加到客户端列表
		chats.addElement(this);
	}
	
	//当连接关闭时
	@OnClose
	public void onClose(){
		//下线
		chats.remove(this);
	}
	//消息转发(遍历所有在线用户列表)
	@OnMessage
	public void onMessage(String message,Session session){
		//遍历所有在线客户端
		for (ChatRoom videoServer : chats) {
			//先获取连接(session)再进行远程连接(getBasicRemote())再发送消息(sendText(message))
			try {
				videoServer.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				System.out.println("遍历所有在线客户出现问题");
			}
		}
		
	}
	
	
}
