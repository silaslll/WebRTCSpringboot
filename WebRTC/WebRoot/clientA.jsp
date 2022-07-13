<%@ page language="java" pageEncoding="UTF-8"%><%@ page isELIgnored="false" %><!DOCTYPE HTML>
<html>
	<head>
		<title>Websoket</title>
		<meta charset="utf-8" />
		<meta name="keywords" content="ACGIST的视频应用,webrtc" />
		<meta name="description" content="使用webrtc实现的网页视频通话。" />
		
		<script src="channel.js"></script>
		
		<style type="text/css">
			*{margin: 0; padding: 0; overflow-y: hidden;}
			body {background-color: rgb(34, 34, 34);}
			#main {display: none; -webkit-transition-property: rotation; -webkit-transition-duration: 2s; text-align: center; -webkit-transform-style: preserve-3d; width: 1200px; margin: 0 auto; padding: 60px 0;}
			#localVideo {box-shadow: 0 0 20px #000; width: 600px; display: inline-block;}
			#remoteVideo {box-shadow: 0 0 20px #000; width: 600px; display: none;}
			#miniVideo {box-shadow: 0 0 20px #000; width: 300px; display: none;}
			#footer {position: absolute; bottom: 0; width: 100%; height: 28px; background-color: #404040; color: #fff; font-size: 13px; font-weight: bold; line-height: 28px; text-align: center;}
			.browser{box-shadow: 0 0 20px #000 inset; width: 400px; margin: 200px auto; padding: 20px; text-align: center; color: #fff; font-weight: bold;}
			@media screen and (-webkit-min-device-pixel-ratio:0) {#main{display: block;} .browser{display: none;}}
		</style>
	</head>
	<body ondblclick="fullScreen()">
		<div class="browser">对不起暂时只支持google chrome浏览器！</div>
		<button onclick="closeSocket();">关闭</button>
		<div id="main">
			<video id="localVideo" autoplay="autoplay"></video>
			<video id="remoteVideo" autoplay="autoplay"></video>
			<video id="remoteVideo1" autoplay="autoplay"></video>
			<video id="miniVideo" autoplay="autoplay"></video>
		</div>
		
		
		发送数据<input type="text" id="sendId">
		接收数据<input type="text" id="reciverId">
		<button onclick="test();">send</button>
		
		
		<div id="footer"></div>
		<script type="text/javascript">
		
		
		var socket = null;
			// 打开websocket
			function openChannel() {
				console.log("打开websocket");
				socket = new WebSocket("ws://192.168.1.118:8080/WebRTC/websoketdemo.video/clientA");
				socket.onopen = onChannelOpened;    
				socket.onmessage = onChannelMessage;
				socket.onclose = onChannelClosed;      
				socket.onerror = onChannelError;  
			}
			
			function test (){
				var value = document.getElementById("sendId").value ;
				socket.send(value);
				alert(value);
			}
			
			// websocket打开
			function onChannelOpened() {
				console.log("websocket打开");
			}
			// 谁知状态
			function setNotice(msg) {
				document.getElementById("footer").innerHTML = msg;
			}
			
			// websocket收到消息
			function onChannelMessage(message) {
				console.log("收到信息 : " + message.data);
				 document.getElementById("reciverId").value =  message.data;
			}
			
			 
			
			// websocket异常
			function onChannelError(event) {
				console.log("websocket异常 ： " + event);
				setTimeout(openChannel, 1); 
			}
			
			// websocket关闭
			function onChannelClosed() {
				console.log("websocket关闭");
			}
			
			if(!WebSocket) {
				errorNotice("你的浏览器不支持WebSocket！建议使用<a href=\"https://www.google.com/intl/zh-CN/chrome/browser/\" target=\"_blank\">google chrome浏览器！</a>");
			}   else {
				if(window.navigator.userAgent.indexOf("Chrome") !== -1)
					setTimeout(openChannel, 1); // 加载完成调用初始化方法
			}
		</script>
	</body>
</html>
