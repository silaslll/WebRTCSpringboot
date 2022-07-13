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
			#remoteVideo {box-shadow: 0 0 20px #000; width: 600px; display: inline-block;}
			#remoteVideo1 {box-shadow: 0 0 20px #000; width: 600px; display: inline-block;}
			#miniVideo {box-shadow: 0 0 20px #000; width: 300px; display: inline-block;}
			#footer {position: absolute; bottom: 0; width: 100%; height: 28px; background-color: #404040; color: #fff; font-size: 13px; font-weight: bold; line-height: 28px; text-align: center;}
			.browser{box-shadow: 0 0 20px #000 inset; width: 400px; margin: 200px auto; padding: 20px; text-align: center; color: #fff; font-weight: bold;}
			@media screen and (-webkit-min-device-pixel-ratio:0) {#main{display: block;} .browser{display: none;}}
		</style>
	</head>
	<body ondblclick="fullScreen()">
		<div class="browser">对不起暂时只支持google chrome浏览器！</div>
		
		
		<div id="main">
			<table>
				<tr>
					<td colspan="4">	<button onclick="closePc();">关闭</button> </td>
				</tr>
				<tr>
					<td colspan="4">
						发送数据<input type="text" id="sendId">
						接收数据<input type="text" id="reciverId">
						<button onclick="test();">send</button>
					</td>
				</tr>
				<tr>
						<td>	<video id="localVideo" autoplay="autoplay"></video> local</td>
						<td>    <video id="remoteVideo" autoplay="autoplay"></video> remote</td>
						<td>    <video id="remoteVideo1" autoplay="autoplay"></video>remote1 </td>
						<td>  	<video id="miniVideo" autoplay="autoplay"></video>  </td>
					</tr>
			</table>
		</div>
		
		
		
		
		
		<div id="footer"></div>
		<script type="text/javascript">
		var pc;
		var main; // 视频的DIV
		var socket; // websocket
		var localVideo; // 本地视频
		var miniVideo; // 本地小窗口
		var remoteVideo; // 远程视频
		var remoteVideo1; // 远程视频
		var localStream; // 本地视频流
		var remoteStream; // 远程视频流
		var localVideoUrl; // 本地视频地址
		
	
		
		var PeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
		var mediaConstraints = {
				"has_audio" : true,
				"has_video" : true
			}; // 音频和视频
			
			
		if(!WebSocket) {
			errorNotice("你的浏览器不支持WebSocket！建议使用<a href=\"https://www.google.com/intl/zh-CN/chrome/browser/\" target=\"_blank\">google chrome浏览器！</a>");
		}   else {
			if(window.navigator.userAgent.indexOf("Chrome") !== -1)
				setTimeout(initialize, 1); // 加载完成调用初始化方法
		}
		
		// 初始化
		function initialize() {
			console.log("初始化");
			
			main = document.getElementById("main");
			localVideo = document.getElementById("localVideo");
			miniVideo = document.getElementById("miniVideo");
			remoteVideo = document.getElementById("remoteVideo");
			remoteVideo1 = document.getElementById("remoteVideo1");

			openChannel();   
		}
		
		//关闭
		function closePc(){
			if(pc != null) {
				pc.close();
				remoteVideo.src = "";
			}
		}
		
		/**===============================websocket======================================**/
			// 打开websocket
			function openChannel() {
				console.log("打开websocket");
				socket = new WebSocket("ws://192.168.1.118:8080/WebRTC/rtcwebsoketdemo.video/clientA");
				socket.onopen = onChannelOpened;    
				socket.onmessage = onChannelMessage;
				socket.onclose = onChannelClosed;      
				socket.onerror = onChannelError;  
			}
			function test (){
				var value = document.getElementById("sendId").value ;
				socket.send(value);
				//alert(value);
			}
			
			// websocket打开
			function onChannelOpened() {
				console.log("websocket打开");
			}
			// 谁知状态
			function setNotice(msg) {
				document.getElementById("footer").innerHTML = msg;
			}
			var i = 0;
			// websocket收到消息, 之后，创建连接，并且连接数据
			function onChannelMessage(message) {
				console.log("收到信息 : " + message.data);
				document.getElementById("reciverId").value =  message.data;
				
				if(i == 0){
					 createPeerConnection();
					 getUserMedia();
					 i ++;
				}
				
				processSignalingMessage(message.data);//建立视频连接
				
				
				
			}
			
			 
			
			// websocket异常
			function onChannelError(event) {
				console.log("websocket异常 ： " + event);
				setTimeout(openChannel, 1); 
			}
			
			// websocket关闭
			function onChannelClosed() {
				console.log("websocket关闭");
				openChannel();
			}
			
		 
			
			
			/**=======================webrtc peerconnection==================*/
			
			// 打开连接
			function createPeerConnection() {
				if(pc == null) {
					var server = {"iceServers" : [{"url" : "stun:stun.l.google.com:19302"}]};
					
					pc = new PeerConnection(server);
					
					pc.onicecandidate = onIceCandidate;
					pc.onconnecting = onSessionConnecting;
					pc.onopen = onSessionOpened;  
					pc.onaddstream = onRemoteStreamAdded;
					pc.onremovestream = onRemoteStreamRemoved;
				}
			}
			
			// 邀请聊天：这个不是很清楚，应该是对方进入聊天室响应函数
			function onIceCandidate(event) {
				if (event.candidate) {
					sendMessage({
						type : "candidate",
						label : event.candidate.sdpMLineIndex,
						id : event.candidate.sdpMid,
						candidate : event.candidate.candidate
					});
				} else {
					console.log("End of candidates.");
				}
			}
			
			// 发送信息
			function sendMessage(message) {
				var msgJson = JSON.stringify(message);
				socket.send(msgJson);
			}
			
			// 开始连接
			function onSessionConnecting(message) {
				console.log("开始连接");
			}
			
			// 连接打开
			function onSessionOpened(message) {
				console.log("连接打开");
			}
			
			// 远程视频添加
			function onRemoteStreamAdded(event) {
				
				var url = webkitURL.createObjectURL(event.stream);
				miniVideo.src = localVideo.src;
				
				if(remoteVideo.src != null && remoteVideo.src != "") {
					remoteVideo1.src = url;
 				} else {
					remoteVideo.src = url;
					remoteStream = event.stream;
				}
				
			}
			
			// 远程视频移除
			function onRemoteStreamRemoved(event) {
				console.log("远程视频移除");
			}
			
			function   doCall(){
				pc.addStream(localStream);   
				console.log("开始呼叫");
				pc.createOffer(setLocalAndSendMessage, null);
			}
			
			function setLocalAndSendMessage(sessionDescription) {
				pc.setLocalDescription(sessionDescription);
				sendMessage(sessionDescription);
			}
			// 响应
			function doAnswer() {
				pc.createAnswer(setLocalAndSendMessage, null);
			}
			
			// 远程视频关闭
			function onRemoteClose() {
				pc.close();
			}
			 
			// 处理消息
			function processSignalingMessage(message) {
				var msg = JSON.parse(message);
				//alert("msgtype=======" + msg.type );
				if (msg.type == "offer") {
					//alert("我在offer中");
					pc.setRemoteDescription(new RTCSessionDescription(msg));
					doAnswer();
				} else if (msg.type == "answer" ) {
					pc.setRemoteDescription(new RTCSessionDescription(msg));
				} else if (msg.type == "candidate" ) {
					var candidate = new RTCIceCandidate({
						sdpMLineIndex : msg.label,
						candidate : msg.candidate
					});
					pc.addIceCandidate(candidate);
				} else if (msg.type == "bye" ) {
					onRemoteClose();
				} else if(msg.type == "nowaiting") {
					onRemoteClose();
				}
			}
		/*****-----------------------------摄像头-------------------------------********/
			// 获取用户的媒体
			function getUserMedia() {
				console.log("获取用户媒体");
				
				navigator.webkitGetUserMedia({
					"audio" : true,
					"video" : true
				}, onUserMediaSuccess, onUserMediaError);
			}
		
			// 获取用户媒体失败
			function onUserMediaSuccess(stream) {
				var url = webkitURL.createObjectURL(stream);
				localVideo.src = url;
				localVideoUrl = url;
				localStream = stream;
				
				doCall();
			}
			
			// 获取用户流失败
			function onUserMediaError(error) {
				console.log("获取用户流失败！");
				
				//alert("获取用户流失败！");
			}
			
			
		</script>
	</body>
</html>
