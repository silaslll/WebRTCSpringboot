<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>直播观看界面</title>
<style type="text/css">
body {
	
}

.video {
	width: 600px;
	background: rgba(247, 180, 180, 0.1);
	box-shadow: 0px 0px 11px #666;
	margin: 20px auto;
	text-align: center;
}
/*标题样式*/
h1 {
	color: #FF9800;
	text-shadow: 1px 1px 2px #000;
	font-family: "华文彩云";
}
/*消息发送区域样式*/
.msg {
	margin: 0 auto;
	width: 600px;
	height: 200px;
	text-align: center;
	box-shadow: 0px 0px 11px 0px #000;
}
/*消息显示区域样式*/
.content {
	height: 170px;
	overflow: auto;
	text-align: left;
	color: blue;
}
/*消息输入框样式*/
input {
	height: 28px;
	width: 99%;
	background: rgba(0, 0, 0, 0.12);
	border: 1px solid #fff;
	border-radius: 3px;
	color: #FFEB3B;
	margin-bottom: 45px;
}

.video,.msg {
	border-radius: 10px;
}

img {
	border-radius: 5px;
	margin-bottom: 8px;
}
/*消息显示 区域*/
#content {
	background: rgba(0, 0, 100, 0.12);
	color: white;
	padding-left: 10px;
}
/*消息发送区域*/
#sendMsg {
	padding-left: 10px;
	color: black;
}
</style>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<!--
        	作者：秦伟
        	时间：2018-07-23
        	描述：直播视频区域
        -->
	<div class="video">
		<h1>欢迎使用"wǒ乄冭嘽純"直播系统</h1>
		<img id="receiver" style="width: 400px;height: 300px;" />
	</div>

	<!--
        	作者：秦伟
        	时间：2018-07-23
        	描述：观众聊天评论区域
        -->
	<div class="msg">
		<div class="content" id="content"></div>
		<input type="text" name="sendMsg" id="sendMsg" value="" />
	</div>
	<script type="text/javascript" charset="UTF-8">
		function $(id) {
			return document.getElementById(id);

		}
		//第一步:连接服务器的WebSocket(建立连接包括视频,聊天服务)
		//聊天服务
		var chatSocket = new WebSocket(
				"ws://localhost:8080/InternalLive/chatroom");
		//视频服务
		var videoSocket = new WebSocket("ws://localhost:8080/InternalLive/video");

		//功能一: 将接受到的视频图片展示在img控件内
		//获取图片控件
		var img = $("receiver");
		//当它有消息时要处理
		videoSocket.onmessage = function(msg) {
			img.src = msg.data;
		}
		//功能二: 将接受到的聊天消息,追加到消息显示区域
		chatSocket.onmessage = function(msg) {
			var content = $("content");
			//追加
			content.innerHTML += "<br>" + msg.data;
			//滚动条	滚动到顶部
			content.scrollTop = content.scrollHeight;
		}
		//获取消息发送控件
		//监听鼠标事件
		window.onkeydown = function(event) {
			//如果按了回车键就触发
			if (event.keyCode == 13) {
				var msgs = $("sendMsg");
				//获取到输入框的值
				chatSocket.send(msgs.value);
				//清空该控件
				msgs.value = "";
			}

		}
	</script>
</body>
</html>
