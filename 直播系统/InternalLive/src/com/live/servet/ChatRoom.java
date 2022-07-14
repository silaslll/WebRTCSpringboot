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
 * ·��
 *
 *
 */
@ServerEndpoint(value="/chatroom")
public class ChatRoom {

	//ÿ����������������������ʱ,�ᴴ��videoServer
	//�ͻ����б���   clients -- ��ʾ�ͻ���
	private static Vector<ChatRoom> chats = new Vector<ChatRoom>();
	
	//��ǰ���ӵĻỰ����
	private Session session;
	//�����Ӵ�ʱ(ִ��ÿ������ʱ)
	@OnOpen
	public void onOpen(Session session){
		//��������ʱ
		this.session=session;
		//����ǰ������ӵ��ͻ����б�
		chats.addElement(this);
	}
	
	//�����ӹر�ʱ
	@OnClose
	public void onClose(){
		//����
		chats.remove(this);
	}
	//��Ϣת��(�������������û��б�)
	@OnMessage
	public void onMessage(String message,Session session){
		//�����������߿ͻ���
		for (ChatRoom videoServer : chats) {
			//�Ȼ�ȡ����(session)�ٽ���Զ������(getBasicRemote())�ٷ�����Ϣ(sendText(message))
			try {
				videoServer.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				System.out.println("�����������߿ͻ���������");
			}
		}
		
	}
	
	
}
