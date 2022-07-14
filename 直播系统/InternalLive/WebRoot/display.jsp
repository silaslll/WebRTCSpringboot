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

<title>主播显示画面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
.view {
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
	text-align: center;
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
</style>

</head>

<body>
	<!--
    	作者：秦伟
    	时间：2018-07-01
    	描述：视频播放画面
    -->
	<h1>欢迎使用"wǒ乄冭嘽純"直播系统</h1>
	<p class="view"><video id="view" autoplay width="400px" height="300px"> </video></p>

	<!--
    	作者：秦伟
    	时间：2018-07-01
    	描述：向服务器定时发送视频截图
    -->
	<canvas id="output" style="display: none;"></canvas>

	<!--
        	作者：秦伟
        	时间：2018-07-23
        	描述：观众聊天评论区域
        -->
	<div class="msg">
		<div class="content" id="content"></div>
		<input type="text" name="sendMsg" id="sendMsg" value="" />
	</div>
	<script type="text/javascript">
		function $(id) {
			return document.getElementById(id);
		}
		var video = $("view"); //视频播放界面
		var backContext; //要发送给服务器的后台图像内容
		var videoSocket; //视频通信的套接字
		var timer; //定时器
		var canvas; //画布 (临时 存放截图)发送给后台用

		//[第一步] 通过浏览器对象获取摄像头的视频源,放入video中实时展示
		var success = function(stream) {
			video.src = window.URL.createObjectURL(stream);
		}
		//设置各种浏览器兼容
		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
				|| navigator.msGetUserMedia;
		navigator.getUserMedia({
			video : true,
			audio : false
		}, success, console.log);

		//[第二步] 定时监测连接状态,进行图片发送
		//定时截图
		setTimeout("init()", 100);
		//初始化发送截图
		function init() {
			//获取画布
			canvas = $("output");
			//获取2d图像
			backContext = $("output").getContext("2d");
			//获取地址,最后一位是 视频符 如果要部署到服务器上一定要把localhost改成自己的域名
			videoSocket = new WebSocket(
					"ws://localhost:8080/InternalLive/video");
			//打开
			videoSocket.onopen = onOpen;
			//关闭
			videoSocket.onclose = onClose;
		}
		//如果连接成功,清除之前的定时器,定时截图发送给服务器
		function onOpen() {
			clearInterval(timer);
			//每10毫秒绘制一副图像(从video中截取)
			timer = setInterval(function() {
				draw();
			}, 10)
		}

		//如果连接中断,继续尝试初始化,重新建立连接
		function onClose() {
			init();

		}
		//定期截图,转换图像(jpeg)
		function draw() {
			//从左上角0,0开始 
			backContext.drawImage(video, 0, 0, canvas.width, canvas.height);
			//发送50%的像素
			videoSocket.send(canvas.toDataURL("image/jpeg", 0.5));
		}

		//聊天服务
		var chatSocket = new WebSocket(
				"ws://localhost:8080/InternalLive/chatroom");
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
