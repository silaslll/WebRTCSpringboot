package com.yealink.webSocketServer;

import com.yealink.pojo.LoginInform;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/LoginWebsocket/{param}")
public class LoginWebsocket {

    //已登录人员名单信息
    private static ConcurrentHashMap<String,LoginInform> loginInforms=new ConcurrentHashMap<String, LoginInform>();

    //concurrent包的线程安全Map，用来存放每个客户端对应的LoginWebsocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<String,LoginWebsocket> webSocketMap = new ConcurrentHashMap<String, LoginWebsocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法，只会在每个连接第一次建立时调用
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value="param") String param, Session session){
        this.session = session;
        //同一账号多次登录处理
        if(loginInforms.containsKey(param)){
            //向同时登录的两人都发消息，然后将两人的webSocket都关闭
            JSONObject messageJson=new JSONObject();
            messageJson.put("state","loginRepeat");
            try {
                webSocketMap.get(param).sendMessage(messageJson.toString());
                this.sendMessage(messageJson.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            webSocketMap.get(param).onClose(param);
            this.onClose(param);
        }else {
            webSocketMap.put(param, this);
            System.out.println(new Date() +" "+param + "登录！当前在线人数为" + (loginInforms.size()+1));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value="param") String param){

        //封装消息
        JSONObject messageJson=new JSONObject();
        messageJson.put("state","unlogin");
        messageJson.put("username",param);
        String message=messageJson.toString();
        webSocketMap.remove(param);  //从set中删除
        //遍历并发送消息
        for(LoginWebsocket item : webSocketMap.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        loginInforms.remove(param);
        System.out.println(new Date() +" "+param+"离开！当前在线人数为" + loginInforms.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //首先对消息进行解析，判断消息的类型
        JSONObject user=JSONObject.fromObject(message);
        if(user.getString("state").equals("login")) {
            //第一种类型，这是一个登录消息包
            //给客户端发送之前已经登录的人员名单信息
            if (!loginInforms.isEmpty()) {
                JSONArray loginInfo = JSONArray.fromObject(loginInforms.values());
                JSONObject messageBefore = new JSONObject();
                messageBefore.put("state", "login");
                messageBefore.put("loginInfo", loginInfo);
                try {
                    this.sendMessage(messageBefore.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //对用户信息进行转换
            LoginInform loginInform = new LoginInform();
            loginInform.setUsername(user.getString("username"));
            loginInform.setStaffName(user.getString("staffName"));
            loginInform.setStaffPostName(user.getString("staffPostName"));
            loginInform.setStaffDid(user.getInt("staffDid"));
            //存放入已登录用户信息集合
            loginInforms.put(user.getString("username"), loginInform);
            //封装消息包
            JSONArray loginInfo = new JSONArray();
            loginInfo.add(user);
            JSONObject messageJson = new JSONObject();
            messageJson.put("state", "login");
            messageJson.put("loginInfo", loginInfo);
            message = messageJson.toString();
            //遍历并发送消息
            for (LoginWebsocket item : webSocketMap.values()) {
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }else if(user.getString("state").equals("invite")){
            //第二种类型，邀请消息包
            JSONArray invited=user.getJSONArray("invited");
            //封装好邀请信息包
            JSONObject messageJson = new JSONObject();
            //包类型
            messageJson.put("state", "invite");
           //包数据，如 会议主题，创建人，会议ID
            messageJson.put("creater",user.getString("creater"));
            messageJson.put("vcs_Title",user.getString("vcsTitle"));
            messageJson.put("vcs_Id",user.getString("vcs_Id"));
            message=messageJson.toString();
            for(Object i:invited){
                String username=JSONObject.fromObject(i).getString("username");
                try {
                    webSocketMap.get(username).sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(@PathParam(value="param") String param,Session session, Throwable error){
        System.out.println(new Date() +" "+param + "发生错误!");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的发包方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

}