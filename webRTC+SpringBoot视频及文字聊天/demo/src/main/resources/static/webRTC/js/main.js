'use strict';

var localVideo = document.querySelector('video#localVideo');
var remoteVideo = document.querySelector('video#remoteVideo');

var btnConn = document.querySelector('button#conn');
var btnLeave = document.querySelector('button#leave');
var sendData = document.querySelector('button#sendData');

var offer = document.querySelector('textarea#offer');
var answer = document.querySelector('textarea#answer');

var shareDeskBox = document.querySelector('input#shareDesk');
var dataMsg = document.querySelector('input#dataMsg');
var userName = document.querySelector('input#userName');

var context = document.querySelector('div#context');

var pcConfig = {
    'iceServers': [{
        'urls': 'turn:www.light-dragon.cn:3478',
        'credential': "light-dragon",
        'username': "light-dragon"
    }]
};

var localStream = null;
var remoteStream = null;

var pc = null;

var roomId;
var socket = null;

var offerdesc = null;
var state = 'init';

// 以下代码是从网上找的
//=========================================================================================

//如果返回的是false说明当前操作系统是手机端，如果返回的是true则说明当前的操作系统是电脑端
function IsPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
    var flag = true;

    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}

//获取url参数
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return "default";
}

//=======================================================================

function sendMessage(roomId, data) {

    console.log('send message to other end', roomId, data);
    if (!socket) {
        console.log('socket is null');
    }
    var msg = {
        'roomId': roomId,
        'data': data
    };
    socket.emit('message', msg);
}

function conn() {

    socket = io.connect(':9090');

    socket.on('joined', function (roomId, name, id) {
        if (isEmpty(userName.value)){
            userName.value = name;
        }
        console.log('receive joined message!', roomId, name, id);
        state = 'joined';
        //如果是多人的话，第一个人不该在这里创建peerConnection
        //都等到收到一个otherjoin时再创建
        //所以，在这个消息里应该带当前房间的用户数
        //
        //create conn and bind media track
        createPeerConnection();
        bindTracks();

        btnConn.disabled = true;
        btnLeave.disabled = false;
        console.log('receive joined message, state=', state);
    });

    socket.on('otherjoin', function (roomId) {
        console.log('receive joined message:', roomId, state);

        //如果是多人的话，每上来一个人都要创建一个新的 peerConnection
        //
        if (state === 'joined_unbind') {
            createPeerConnection();
            bindTracks();
        }

        state = 'joined_conn';
        call();

        console.log('receive other_join message, state=', state);
    });

    socket.on('full', function (roomId, id) {
        console.log('receive full message', roomId, id);
        hangup();
        closeLocalMedia();
        state = 'leaved';
        console.log('receive full message, state=', state);
        alert('the room is full!');
    });

    socket.on('leaved', function (roomId, id) {
        console.log('receive leaved message', roomId, id);
        state = 'leaved';
        socket.disconnect();
        console.log('receive leaved message, state=', state);

        btnConn.disabled = false;
        btnLeave.disabled = true;
    });

    socket.on('bye', function (room, id) {
        console.log('receive bye message', roomId, id);
        //state = 'created';
        //当是多人通话时，应该带上当前房间的用户数
        //如果当前房间用户不小于 2, 则不用修改状态
        //并且，关闭的应该是对应用户的peerconnection
        //在客户端应该维护一张peerconnection表，它是
        //一个key:value的格式，key=userid, value=peerconnection
        state = 'joined_unbind';
        hangup();
        offer.value = '';
        answer.value = '';
        console.log('receive bye message, state=', state);
    });

    socket.on('disconnect', function (socket) {
        console.log('receive disconnect message!', roomId);
        if (!(state === 'leaved')) {
            hangup();
            closeLocalMedia();

        }
        state = 'leaved';

    });

    socket.on('message', function (roomId, data) {
        console.log('receive message!', roomId, data);

        if (data === null || data === undefined) {
            console.error('the message is invalid!');
            return;
        }

        if (data.hasOwnProperty('type') && data.type === 'offer') {
            offer.value = data.sdp;
            pc.setRemoteDescription(new RTCSessionDescription(data));
            //create answer
            pc.createAnswer()
                .then(getAnswer)
                .catch(handleAnswerError);
        } else if (data.hasOwnProperty('type') && data.type == 'answer') {
            answer.value = data.sdp;
            pc.setRemoteDescription(new RTCSessionDescription(data));
        } else if (data.hasOwnProperty('type') && data.type === 'candidate') {
            var candidate = new RTCIceCandidate({
                sdpMLineIndex: data.label,
                candidate: data.candidate
            });
            pc.addIceCandidate(candidate);
        } else {
            console.log('the message is invalid!', data);
        }
    });

    socket.on('msg', function (roomId, userName, date, msg) {
        console.log('send msg success', roomId, userName, msg);
        addLeftContext(userName, date, msg);
    });

    socket.on('own_msg', function (roomId, userName, date, msg) {
        console.log('send own_msg success', roomId, userName, msg);
        addRightContext(userName, date, msg);
    });

    roomId = getQueryVariable('room');
    var data = {
        'roomId': roomId,
        'userName' : userName.value
    };
    socket.emit('join', data);

    return true;
}

function connSignalServer() {

    //开启本地视频
    start();

    return true;
}

function getMediaStream(stream) {

    if (localStream) {
        stream.getAudioTracks().forEach(function (track) {
            localStream.addTrack(track);
            stream.removeTrack(track);
        });

    } else {
        localStream = stream;
    }

    localVideo.srcObject = localStream;

    //这个函数的位置特别重要，
    //一定要放到getMediaStream之后再调用
    //否则就会出现绑定失败的情况
    //
    //setup connection
    conn();

    //btnStart.disabled = true;
    //btnCall.disabled = true;
    //btnHangup.disabled = true;
}

function getDeskStream(stream) {
    localStream = stream;
}

function handleError(err) {
    console.error('Failed to get Media Stream!', err);
}

function shareDesk() {

    if (IsPC()) {
        navigator.mediaDevices.getDisplayMedia({video: true})
            .then(getDeskStream)
            .catch(handleError);

        return true;
    }

    return false;

}

function start() {

    if (!navigator.mediaDevices ||
        !navigator.mediaDevices.getUserMedia) {
        console.error('the getUserMedia is not supported!');
        return false;
    } else {

        var constraints;

        if (shareDeskBox.checked && shareDesk()) {

            constraints = {
                video: false,
                audio: {
                    echoCancellation: true,
                    noiseSuppression: true,
                    autoGainControl: true
                }
            }

        } else {
            constraints = {
                video: true,
                audio: {
                    echoCancellation: true,
                    noiseSuppression: true,
                    autoGainControl: true
                }
            }
        }

        navigator.mediaDevices.getUserMedia(constraints)
            .then(getMediaStream)
            .catch(handleError);
    }

}

function getRemoteStream(e) {
    remoteStream = e.streams[0];
    remoteVideo.srcObject = e.streams[0];
}

function handleOfferError(err) {
    console.error('Failed to create offer:', err);
}

function handleAnswerError(err) {
    console.error('Failed to create answer:', err);
}

function getAnswer(desc) {
    pc.setLocalDescription(desc);
    answer.value = desc.sdp;

    //send answer sdp
    sendMessage(roomId, desc);
}

function getOffer(desc) {
    pc.setLocalDescription(desc);
    offer.value = desc.sdp;
    offerdesc = desc;

    //send offer sdp
    sendMessage(roomId, offerdesc);

}

function createPeerConnection() {

    //如果是多人的话，在这里要创建一个新的连接.
    //新创建好的要放到一个map表中。
    //key=userid, value=peerconnection
    console.log('create RTCPeerConnection!');
    if (!pc) {
        pc = new RTCPeerConnection(pcConfig);

        pc.onicecandidate = function (e) {
            if (e.candidate) {
                sendMessage(roomId, {
                    type: 'candidate',
                    label: event.candidate.sdpMLineIndex,
                    id: event.candidate.sdpMid,
                    candidate: event.candidate.candidate
                });
            } else {
                console.log('this is the end candidate');
            }
        };
        pc.ontrack = getRemoteStream;
    } else {
        console.warning('the pc have be created!');
    }
    return false;
}

//绑定永远与 peerconnection在一起，
//所以没必要再单独做成一个函数
function bindTracks() {

    console.log('bind tracks into RTCPeerConnection!');

    if (pc === null || pc === undefined) {
        console.error('pc is null or undefined!');
        return;
    }

    if (localStream === null || localStream === undefined) {
        console.error('localstream is null or undefined!');
        return;
    }

    //add all track into peer connection
    localStream.getTracks().forEach(function (track) {
        pc.addTrack(track, localStream);
    });

}

function call() {

    if (state === 'joined_conn') {

        var offerOptions = {
            offerToRecieveAudio: 1,
            offerToRecieveVideo: 1
        };

        pc.createOffer(offerOptions)
            .then(getOffer)
            .catch(handleOfferError);
    }
}

function hangup() {

    if (pc) {

        offerdesc = null;

        pc.close();
        pc = null;
    }

}

function closeLocalMedia() {

    if (localStream && localStream.getTracks()) {
        localStream.getTracks().forEach(function (track) {
            track.stop();
        });
    }
    localStream = null;
}

function leave() {

    if (socket) {
        var data = {
            'roomId': roomId
        };
        socket.emit('leave', data); //notify server
    }

    hangup();
    closeLocalMedia();

    offer.value = '';
    answer.value = '';
    btnConn.disabled = false;
    btnLeave.disabled = true;
}

function sendMessageData() {
    var msg = dataMsg.value;
    if (!isEmpty(msg)) {
        var data = {
            "roomId": roomId,
            "userName": userName.value,
            "msg": msg.trim()
        };
        socket.emit("msg", data);
        dataMsg.value = '';
    } else {
        alert("不可发送空内容！！！")
    }
}

function keySubmit(e) {
    var evt = window.event || e;
    if (evt.keyCode === 13){
        //回车事件
        sendMessageData();
    }
}

//判断字符是否为空的方法
function isEmpty(obj) {
    var b = false;
    if (obj === "undefined" || obj === null || obj.trim() === "") {
        b = true;
    }
    return b;
}

function addLeftContext(userName, date, msg) {
    var html = '<div class="col-sm-12" style="text-align: left; margin-bottom: 10px;"><span class="col-sm-12" style="font-size: 16px; color: #4a89ff; padding: 0; margin-bottom: 5px;">';
    html += userName + '&nbsp;&nbsp;<font style="color: #f8b0ff">' + date + '</font>';
    html += '</span><span style="border-radius: 5px; background-color: #5bc0de; color: black; padding: 5px 10px;">';
    html += msg + '</span>';
    context.innerHTML = context.innerHTML + html;
    context.scrollTop = context.scrollHeight;
}

function addRightContext(userName, date, msg) {
    var html = '<div class="col-sm-12" style="text-align: right; margin-bottom: 10px;"><span class="col-sm-12" style="font-size: 16px; color: #4a89ff; padding: 0; margin-bottom: 5px;">';
    html += userName + '&nbsp;&nbsp;<font style="color: #f8b0ff">' + date + '</font>';
    html += '</span><span style="border-radius: 5px; background-color: #5bc0de; color: black; padding: 5px 10px;">';
    html += msg + '</span>';
    context.innerHTML = context.innerHTML + html;
    context.scrollTop = context.scrollHeight;
}

btnConn.onclick = connSignalServer;
btnLeave.onclick = leave;
sendData.onclick = sendMessageData;
dataMsg.onkeydown = keySubmit;

// connSignalServer();
