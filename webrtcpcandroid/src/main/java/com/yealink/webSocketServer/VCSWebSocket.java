package com.yealink.webSocketServer;

import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/VCSWebsocket/{vcsId}/{staffName}/{username}")
public class VCSWebSocket {

    //concurrent包的线程安全Map，用来存放每个客户端对应的LoginWebsocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<String,VCSWebSocket> webSocketMap = new ConcurrentHashMap<String, VCSWebSocket>();

    //会议用户列表
    private static ConcurrentHashMap<String,HashMap<String,String>> VCSList = new ConcurrentHashMap<String, HashMap<String,String>>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "vcsId")String vcsId ,@PathParam(value = "staffName") String staffName,@PathParam(value = "username") String username, Session session){
        this.session = session;
        //同账号不能同时参加两个视频会议,若已经参加一个会议，则无法参加下一个会议
        if(webSocketMap.containsKey(username)){
            //向后面建立的连接发送消息
            JSONObject messageJson=new JSONObject();
            messageJson.put("type","loginRepeat");
            try {
                webSocketMap.get(username).sendMessage(messageJson.toString());
                this.sendMessage(messageJson.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(new Date() + "会议服务器有新的连接：会议ID" + vcsId + " 用户名" + staffName + " 账号" +username);
            //将该用户连接信息存入
            webSocketMap.put(username, this);
            //与服务器建立连接后，获取相关集合包，
            if(VCSList.containsKey(vcsId)){
                HashMap<String,String> list=VCSList.get(vcsId);
                JSONObject message=new JSONObject();
                message.put("type","text");
                message.put("text",staffName+"加入会议");
                JSONObject message2=new JSONObject();
                message2.put("type","login");
                message2.put("username",username);
                message2.put("staffName",staffName);
                //根据集合包发送自己参加会议的消息
                for(String joiner:list.keySet()){
                    try {
                        webSocketMap.get(joiner).sendMessage(message.toString());
                        webSocketMap.get(joiner).sendMessage(message2.toString());
                        //给客户端发送之前已经参加会议的人的集合包
                        JSONObject message3=new JSONObject();
                        message3.put("type","login");
                        message3.put("username",joiner);
                        message3.put("staffName",list.get(joiner));
                        this.sendMessage(message3.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                list.put(username,staffName);
            }else{
                System.out.println(new Date() +" "+ staffName +"创建会议"+vcsId+" 用户列表");
                HashMap<String,String> list=new HashMap<String ,String>();
                list.put(username,staffName);
                VCSList.put(vcsId,list);
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "vcsId")String vcsId ,@PathParam(value = "staffName") String staffName,@PathParam(value = "username") String username){
        HashMap<String,String> list=VCSList.get(vcsId);
        JSONObject message=new JSONObject();
        message.put("type","text");
        message.put("text",staffName+"离开会议");
        JSONObject message2=new JSONObject();
        message2.put("type","unlogin");
        message2.put("username",username);
        list.remove(username);
        webSocketMap.remove(username);
        //根据集合包发送自己离开会议的消息
        for(String joiner:list.keySet()){
            try {
                webSocketMap.get(joiner).sendMessage(message.toString());
                webSocketMap.get(joiner).sendMessage(message2.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(new Date() +" "+ staffName +"离开会议 会议ID："+vcsId);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam(value = "vcsId")String vcsId ,@PathParam(value = "staffName") String staffName,@PathParam(value = "username") String username) {
        //首先对消息进行解析，判断消息的类型
        JSONObject data=JSONObject.fromObject(message);
        if(data.getString("type").equals("text")) {
            //生成相应消息
            JSONObject messages=new JSONObject();
            messages.put("type","text");
            messages.put("text",staffName+":"+data.getString("text"));
            //寻找正确的对象集合转发信息
            HashMap<String,String> list=VCSList.get(vcsId);
            for(String joiner:list.keySet()){
                try {
                    webSocketMap.get(joiner).sendMessage(messages.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(data.getString("type").equals("offer") || data.getString("type").equals("answer")||data.getString("type").equals("icecandidate")){
            //对建立视频连接的相关包的处理
            try {
                webSocketMap.get(data.getString("send")).sendMessage(data.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(data.getString("type").equals("paintOne") || data.getString("type").equals("paintTwo")){
            //处理画图相关信息包
            //寻找正确的对象集合转发信息
            HashMap<String,String> list=VCSList.get(vcsId);
            for(String joiner:list.keySet()){
                try {
                    //发送给除了自己之外在线的人
                    if(!joiner.equals(username))
                        webSocketMap.get(joiner).sendMessage(data.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(data.getString("type").equals("picture")){
            //寻找正确的对象集合转发信息
            HashMap<String,String> list=VCSList.get(vcsId);
            JSONObject info=new JSONObject();
            info.put("type","text");
            info.put("text",staffName+" 发送了自己的白板图片");
            for(String joiner:list.keySet()){
                try {
                    //发送给除了自己之外在线的人
                    if(!joiner.equals(username)) {
                        webSocketMap.get(joiner).sendMessage(info.toString());
                        webSocketMap.get(joiner).sendMessage(data.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(data.getString("type").equals("fileShare")){
            //寻找正确的对象集合转发信息
            HashMap<String,String> list=VCSList.get(vcsId);
            JSONObject info=new JSONObject();
            info.put("type","text");
            info.put("text",staffName+" 共享了文件:" + data.getString("fileName"));
            for(String joiner:list.keySet()){
                try {
                    //发送给除了自己之外在线的人
                    if(!joiner.equals(username)) {
                        webSocketMap.get(joiner).sendMessage(info.toString());
                        webSocketMap.get(joiner).sendMessage(data.toString());
                    }
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
    public void onError(@PathParam(value = "staffName") String staffName,Session session, Throwable error){
        System.out.println(new Date() +" " +staffName +"发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

}