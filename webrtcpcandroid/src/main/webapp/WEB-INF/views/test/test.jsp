<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>测试和移动端视频通话</title>
    <script src="../../../static/jquery-3.2.1.min.js"></script>

</head>
<body>
<video width="110%" height="110%" style="margin: -30px;" autoplay id="self">
</video>
<%--<br>对方的视频--%>
<video width="340px" height="320px" autoplay id="10093" hidden></video>
<button id="huJiao" onclick="huJiao()" hidden>呼叫手机</button>
<span style="color:red;font-weight: bold;" id="tips" hidden></span>


</body>

<script>
    jQuery.ajaxSetup({cache: false})
    var Websocket = null;
    //建立流通道的兼容方法
    var PeerConnection = (window.webkitRTCPeerConnection || window.mozRTCPeerConnection || window.RTCPeerConnection || undefined);
    var RTCSessionDescription = (window.webkitRTCSessionDescription || window.mozRTCSessionDescription || window.RTCSessionDescription || undefined);
    //用json方式保存连接
    var pc = {};
    var room = "";
    var currentUserId = "10094";
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        Websocket = new WebSocket("ws://localhost:803/VCSWebsocket/1/1/1");

    } else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    Websocket.onerror = function () {
        alert("与服务器连接出错！");
        //登出
        window.location.href = "/login/unlogin";
    };

    //连接成功建立的回调方法
    Websocket.onopen = function () {
        console.log("成功建立websocket连接！");

    }

    //接收到消息的回调方法
    Websocket.onmessage = function (event) {
        var message = $.parseJSON(event.data);
        if (message.eventName == "__answer") {
            //var send= message.data.userID;
            var send = message.data.fromID;
            //主动发出的offer请求得到回应，将对方的描述填入
            console.log(message.data.sdp);
            var rtcs = new RTCSessionDescription(String.valueOf(message.data.sdp));
            rtcs.type = "answer";
            rtcs.sdp = message.data.sdp;
            console.log("获得answer ：" + send);
            console.log(rtcs);
            console.log(pc[send]);
            pc[send].setRemoteDescription(rtcs);

            document.getElementById("tips").innerHTML = "成功和对面建立视频通话！！";


        } else if (message.eventName == "__peers") {
            console.log("收到消息类型：__peers");
            //发送邀请
            Websocket.send(JSON.stringify({
                'eventName': "__invite",
                'data': {'room': room, "audioOnly": false, "inviteID": currentUserId, "userList": "10093"}
            }));

        } else if (message.eventName == "__new_peer") {
            console.log("收到消息类型：__new_peer");
            //发送offer
            con(message.data.userID);

        } else if (message.eventName == "__offer") {
            console.log("收到消息类型：__offer");
            var send = message.username;
            //被请求连接
            console.log("获得offer：" + send);
            pc[send] = new PeerConnection(stunServers);
            //将请求连接方的媒体描述信息填入
            var rtcs = new RTCSessionDescription(message.data.sdp);
            pc[send].setRemoteDescription(rtcs);

            pc[send].onicecandidate = function (event) {
                if (event.candidate) {
                    Websocket.send(JSON.stringify({
                        "eventName": "__ice_candidate",
                        "data": {
                            "userID": send,
                            "id": event.candidate.sdpMid,
                            "label": event.candidate.sdpMLineIndex,
                            "fromID": currentUserId,
                            "candidate": event.candidate
                        }
                    }));
                    console.log("发送icecandidate给" + send);
                } else {
                    console.log("发送icecandidate给" + send + "完毕");
                }
            };

            pc[send].addStream(locatstream);
            console.log("已将流装载");

            pc[send].onaddstream = function (e) {
                console.log(e.stream);
                try {
                    document.getElementById(send).srcObject = e.stream;
                } catch (error) {
                    document.getElementById(send).src = window.URL.createObjectURL(e.stream);
                }
                console.log("获取远程媒体成功");
            }

            //发送answer
            pc[send].createAnswer(function (desc) {
                console.log(desc);
                console.log("发送answer");
                pc[send].setLocalDescription(desc);
                VCSWebsocket.send(JSON.stringify({
                    'eventName': "__answer",
                    'data': {"sdp": desc, "fromID": currentUserId, "userID": "10093"}
                }));
            }, function () {
                console.log("创建answer失败");
            });

        } else if (message.eventName == "__login_success") {
            console.log(message.eventName);
        } else if (message.eventName == "__ice_candidate") {
            var send = message.data.fromID;
            var sdpMid = message.data.id;
            var sdpMLineIndex = message.data.label;
            console.log(sdpMid + ":" + sdpMLineIndex);
            console.log(currentUserId + ":获得icecandidate");
            var candidateStr = message.data.candidate + "";
            console.log(candidateStr);
            var obj = "{\"sdpMid\":" + sdpMid + ",\"sdpMLineIndex\":" + sdpMLineIndex + ",\"candidate\":\"" + candidateStr + "\"}";
            //var iceCandidate = new RTCIceCandidate(String.valueOf(message.data.candidate));
            var iceCandidate = new RTCIceCandidate(JSON.parse(obj));
            console.log(iceCandidate);
            pc[send].addIceCandidate(iceCandidate);
            console.log(pc[send]);


        }
    }

    //连接关闭的回调方法
    Websocket.onclose = function () {

    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //关闭loginWebsocket连接
    function closeWebSocket() {
        Websocket.close();
    }


    /**
     * 视频相关功能
     */
//对于视频流格式配置，352*320，帧率最高10帧
    var selfStream = {
        video: {mandatory: {maxWidth: 352, maxHeight: 320, maxFrameRate: 10}},
        audio: true
    };
    var locatstream = null;

    function hasUserMedia() {
        return !!(navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);
    }

    //浏览器兼容  获取摄像头
    if (hasUserMedia()) {
        navigator.getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia || navigator.mediaDevices.getUserMedia);
        window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
//将流绑定到Video上
        navigator.getUserMedia(selfStream, getUserStream, function error(error) {
            console.log(error);
        });

//获取绑定自己的媒体流到video上
        function getUserStream(stream) {
            var selfVideo = document.getElementById("self");
            try {
                selfVideo.srcObject = stream;
            } catch (error) {
                selfVideo.src = window.URL.createObjectURL(stream);
            }
            locatstream = stream;
        };
    } else {
        alert("你的浏览器不支持摄像头1！")
    }

    /*
    "iceServers": [{
        "url": "stun:stun.xxxx.xxx:端口号",
        "username": "配置的用户名",//可选
        "credential": "配置的用户密码或者密匙"//可选
    },
        { "url": "turn:turn.xxx.xxx:端口号",
            "username": "配置的用户名",//可选
            "credential": "配置的用户密码或者密匙"//可选
        }
    ]
    */


    //STUN服务器配置，内网可以先不管，外网需要
    var stunServers = {
        iceServers: [
            // { "url": "stun:stun.l.google.com:19302" },//使用google公共测试服务器
            // // {"url":"stun:stun.ideasip.com"},
            // // {"url":"stun:stun.iptel.org"},
            // // {"url":"stun:stun.voxgratia.org"},
            // {"url":'stun:59.110.45.20:3478?transport=udp',
            //     "username": "kurento",
            //     'credential':'kurento'},
            // {"url":'turn:59.110.45.20:3478?transport=udp',
            //     "username": "kurento",
            //     'credential':'kurento'},
            // {"url":'turn:59.110.45.20:3478?transport=tcp',
            //     "username": "kurento",
            //     'credential':'kurento'}
        ]
    };


    function huJiao() {
        document.getElementById("tips").innerHTML = "正在呼叫中。。。 等待对方应答";

        //创建房间
        room = myuuid();
        Websocket.send(JSON.stringify({
            'eventName': "__create",
            'data': {'room': room, "roomSize": 2, "userID": currentUserId}
        }));

        //con(10093);


    }


    //主动要求建立连接的方式
    function con(username) {
        //生成新的PeerConnection
        pc[username] = new PeerConnection(stunServers);
        console.log("发起方的peerConnection" + pc[username]);
        pc[username].onicecandidate = function (event) {
            if (event.candidate) {
                Websocket.send(JSON.stringify({
                    "eventName": "__ice_candidate",
                    "data": {
                        "userID": username,
                        "id": event.candidate.sdpMid,
                        "label": event.candidate.sdpMLineIndex,
                        "fromID": currentUserId,
                        "candidate": event.candidate.candidate
                    }
                }));
                console.log("发送icecandidate给" + username);
            } else {
                console.log("发送icecandidate给" + username + "完毕");
            }
        };

        pc[username].onaddstream = function (e) {
            console.log(e.stream);
            try {
                document.getElementById(username).srcObject = e.stream;
            } catch (error) {
                document.getElementById(username).src = window.URL.createObjectURL(e.stream);
            }
            console.log("获取远程媒体成功：" + username);
            console.log(pc[username]);
        }

        pc[username].addStream(locatstream);
        console.log("发起方绑定本地流");

        //主动连接方创建offer
        pc[username].createOffer(function (offer) {
            console.log(username + "开始创建offer");
            pc[username].setLocalDescription(offer);
            var obj = JSON.stringify({
                "eventName": "__offer",
                "data": {"sdp": offer.sdp, "userID": "10093", "fromID": currentUserId}
            });
            Websocket.send(obj);
            console.log(username + "发送offer");
        }, function () {
            console.log("创建offer失败");
        });
    }

    function myuuid() {
        function S4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }

        return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
    }

</script>
</html>
