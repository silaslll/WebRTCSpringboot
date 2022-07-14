package com.scbd.bdpl.controller.multimedia;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * websocket服务
 * @author Ivan
 *
 */

@ServerEndpoint("/websocket/{username}")
public class SocketServer {
	private static Map<String, Session> userOnlineMap = new HashMap<String,Session>();

	//连接时
	@OnOpen
	public void open(Session session,@PathParam("username")String username) {
		for (String onlineUsername : userOnlineMap.keySet()) {
			if(onlineUsername.equals(username)) {//用户已存在
				System.out.println("用户已存在,或用户名重复");
				return;
			}
		}
		//转发给其他人上线消息
		JSONObject json=new JSONObject();
		json.put("type", "userConnection");
		json.put("data", username);
		forwardMessageExceptMe(session, json.toString());
		System.out.println(username + "open");
		userOnlineMap.put(username,session);//添加用户进入在线列表
	}

	//收到消息
	@OnMessage
	public void OnMessage(String message, Session session,@PathParam("username")String username) {
		//获得消息并转为JSON
		JSONObject json=JSON.parseObject(message);
		String type=(String) json.get("type");
		
		//判断消息
		switch (type) {
		//查询在线用户
		case "getOnlineUserName":
			replyOnlineUserName(session);
			break;
		//转发消息给sendTo
		default:
			String sendBy=(String) json.get("sendBy");
			String sendTo=(String) json.get("sendTo");
			if(sendBy!=null&&sendTo!=null) {
				forwardMessage(userOnlineMap.get(sendTo), message);
			}
			break;
		}

	}

	@OnClose
	public void close(Session session,@PathParam("username")String username) {
		System.out.println(username + "close");
		try {
			userOnlineMap.remove(username, session);
			session.close();
			//转发给其他人下线消息
			JSONObject json=new JSONObject();
			json.put("type", "userClose");
			json.put("data", username);
			forwardMessageExceptMe(session, json.toString());
		} catch (IOException e) {
			//			e.printStackTrace();
		}
	}

	//回复在线用户
	private void replyOnlineUserName(Session session) {
		Set<String> OnlineUserNames=userOnlineMap.keySet();
		JSONObject json=new JSONObject();
		json.put("type", "userOnlineList");
		json.put("data", OnlineUserNames);
		forwardMessage(session,json.toString());
	}

	//转发消息特定目标
	private void forwardMessage(Session session,String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			//			e.printStackTrace();
		}
	}
	//转发消息给所有人
	private void forwardMessage(Collection<Session> sessions,String message) {
			for (Session session : sessions) {
				forwardMessage(session,message);
			}
	}
	//转发消息给除自己以外的所有人
	private void forwardMessageExceptMe(Session session,String message) {
		for (Session sessions : userOnlineMap.values()) {
			if(!sessions.equals(session)) {
					forwardMessage(sessions,message);
			}
		}
	}
	
	
}
