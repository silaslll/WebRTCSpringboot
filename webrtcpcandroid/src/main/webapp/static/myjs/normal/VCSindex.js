jQuery.ajaxSetup ({cache:false})
var VCSWebsocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    //VCSWebsocket = new WebSocket("ws://192.168.3.8:8080/VCSWebsocket/"+$("#vcs_Id").html()+"/"+$("#staffName").html()+"/"+$("#staffUsername").html());
    //VCSWebsocket = new WebSocket("ws://192.168.137.1:8080/VCSWebsocket/"+$("#vcs_Id").html()+"/"+$("#staffName").html()+"/"+$("#staffUsername").html());
    VCSWebsocket = new WebSocket("ws://localhost:8080/VCSWebsocket/"+$("#vcs_Id").html()+"/"+$("#staffName").html()+"/"+$("#staffUsername").html());
}
else {
    alert('当前浏览器 Not support websocket')
}
//连接发生错误的回调方法
VCSWebsocket.onerror = function () {
    alert("与服务器连接出错！");
    //登出
    window.location.href = "/login/unlogin";
};

//连接成功建立的回调方法
VCSWebsocket.onopen = function () {

}

//接收到消息的回调方法
VCSWebsocket.onmessage = function (event) {
    //对接到的关于登录的消息进行处理
    var message=$.parseJSON(event.data);
    //重复登录处理
    if(message.type == "loginRepeat"){
        alert("您的账号已参加了一个会议！");
        window.location.href = "/Page/normal/NormalIndex";
    }else if(message.type == "login"){
        var videoId=message.username;
        $("#VCSListContent").append(
            '<div class="col-md-6 column"> <h6>'+message.staffName+'</h6>'+
            '<video height="320" width="340" autoplay id="'+videoId+'"> </video>'+
            '<button type="button" conUsername="'+message.username+'"class="btn btn-primary pull-right connBtn"><span class="glyphicon glyphicon-facetime-video">连接</span></button></div>'
        );
        //先解绑，然后再集体绑定，以避免重复绑定问题
        $(".connBtn").unbind('click');
        //绑定事件
        $(".connBtn").on("click",function () {
            //alert("开始与"+$(this).attr("conUsername")+"建立连接！");
            con($(this).attr("conUsername"));
        })
    }else if(message.type == "unlogin"){
        $("#"+message.username).parent().remove();
    }
    else if(message.type == "text"){
        var str=$("#textChat").val();
        $("#textChat").val(str + "\n" + message.text);
        var doc=document.getElementById("textChat");
        doc.scrollTop=doc.scrollHeight;
    }//以下内容为建立peerConnection
    else if(message.type == "offer"){
        var send= message.username;
        //被请求连接
        console.log($("#staffUsername").html()+"获得offer："+send);
        pc[send] = new PeerConnection(stunServers);
        //将请求连接方的媒体描述信息填入
        var rtcs =new RTCSessionDescription(message.data);
        pc[send].setRemoteDescription(rtcs);

        pc[send].onicecandidate =function(event){
            if(event.candidate) {
                VCSWebsocket.send(JSON.stringify({
                    'type': "icecandidate",
                    'username': $("#staffUsername").html(),
                    'staffname': $("#staffName").html(),
                    'send': send,
                    'state': "con",
                    'data': {'candidate': event.candidate}
                }));
                console.log($("#staffUsername").html() + "发送icecandidate给" + send);
            }else{
                console.log($("#staffUsername").html() + "发送icecandidate给" + send + "完毕");
            }
        };

        if(locatstream){
            pc[send].addStream(locatstream);
            console.log($("#staffUsername").html()+"已将流装载");
        }
        pc[send].onaddstream=function(e){
            console.log(e.stream);
            try{
                document.getElementById(send).srcObject = e.stream;
            }catch(error){
                document.getElementById(send).src=window.URL.createObjectURL(e.stream);
            }
            console.log($("#staffUsername").html()+"获取远程媒体成功");
        }

        //发送answer
        pc[send].createAnswer(function(desc){
            console.log(desc);
            console.log($("#staffUsername").html()+"发送answer");
            pc[send].setLocalDescription(desc);
            VCSWebsocket.send(JSON.stringify({
                'type':"answer",
                'username':$("#staffUsername").html(),
                'staffname':$("#staffName").html(),
                'send':send,
                'state':"con",
                'data':desc
            }));
        },function () {
            console.log("创建answer失败");
        });
    }else if(message.type == "answer"){
        var send= message.username;
        //主动发出的offer请求得到回应，将对方的描述填入
        var rtcs =new RTCSessionDescription(message.data);
        console.log($("#staffUsername").html()+"获得answer ："+send);
        pc[send].setRemoteDescription(rtcs);
    }else if(message.type == "icecandidate"){
        var send= message.username;
        console.log($("#staffUsername").html()+"获得icecandidate:"+send);
        console.log(message.data.candidate);
        console.log(new RTCIceCandidate(message.data.candidate))
        pc[send].addIceCandidate(new RTCIceCandidate(message.data.candidate));
        console.log(pc[send]);
    }else if(message.type == "paintOne"){
        if($("#shareWhite").is(':checked')){
            tempContext.moveTo(message.data.x,message.data.y);
        }
    }else if(message.type == "paintTwo"){
        if($("#shareWhite").is(':checked')){
            tempContext.lineTo(message.data.x,message.data.y);
            tempContext.stroke();
        }
    }else if(message.type =="picture"){
        var image = new Image();
        image.onload=function () {
            tempContext.drawImage(image, 0, 0)
        };
        image.src=message.data;
    }else if(message.type == "fileShare"){
        //收到文件信息
        var fileUrl=message.fileUrl.replace(/\\/g,"/");
        console.log(fileUrl);
        $("#shareFileList").append(
            '<a href="/file/download?fileUrl='+fileUrl+'" target="_blank">'+message.fileName+'</a>'
        )
    }
}

//连接关闭的回调方法
VCSWebsocket.onclose = function () {

}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}

//关闭loginWebsocket连接
function closeWebSocket() {
    VCSWebsocket.close();
}

/**
 * 文字聊天功能实现
 */
function sendTextMessage(){
    var obj={};
    obj["type"]="text";
    obj["text"]=$("#textOfSend").val();
    var message=JSON.stringify(obj);
    VCSWebsocket.send(message);
}

$("#sendTextBtn").click(function(){
    sendTextMessage();
    $("#textOfSend").val("")
});


/**
 * 视频相关功能
 */
//对于视频流格式配置，352*320，帧率最高10帧
var selfStream ={
    video:{mandatory: {maxWidth: 352,maxHeight: 320,maxFrameRate: 10}},
    audio:true
};
var  locatstream =null;
function hasUserMedia() {
    return !!(navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);
}
//浏览器兼容  获取摄像头
if(hasUserMedia()) {
    navigator.getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia || navigator.mediaDevices.getUserMedia||MediaDevices.getUserMedia());
    window.URL = window.URL || window.webkitURL || window.mozURL || window.msURL;
//将流绑定到Video上
    navigator.getUserMedia(selfStream, getUserStream , function error(error) {
        console.log(error);
    });

//获取绑定自己的媒体流到video上
    function getUserStream(stream) {
        var selfVideo = document.getElementById("self");
        try{
            selfVideo.srcObject = stream;
        }catch(error){
            selfVideo.src = window.URL.createObjectURL(stream);
        }
        locatstream = stream;
    };
}else {
    alert("你的浏览器不支持摄像头！")
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
var stunServers ={iceServers:[
        //{ "url": "stun:stun.l.google.com:19302" },//使用google公共测试服务器
        // {"url":"stun:stun.ideasip.com"},
        // {"url":"stun:stun.iptel.org"},
        // {"url":"stun:stun.voxgratia.org"},
        // {"url":'turn:59.110.45.20:3478',
        //     "username": "kurento",
        //     'credential':'kurento'}
    ]};
//建立流通道的兼容方法
var PeerConnection =(window.webkitRTCPeerConnection || window.mozRTCPeerConnection || window.RTCPeerConnection || undefined);
var RTCSessionDescription = (window.webkitRTCSessionDescription || window.mozRTCSessionDescription || window.RTCSessionDescription || undefined);
//用json方式保存连接
var pc={} ;



//主动要求建立连接的方式
function con(username){
    //生成新的PeerConnection
    pc[username] = new PeerConnection(stunServers);
    console.log("发起方的peerConnection"+pc[username]);
    pc[username].onicecandidate =function(event){
        if(event.candidate) {
            VCSWebsocket.send(JSON.stringify({
                'type': "icecandidate",
                'username': $("#staffUsername").html(),
                'staffname': $("#staffName").html(),
                'send': username,
                'state': "con",
                'data': {'candidate': event.candidate}
            }));
            console.log($("#staffUsername").html() + "发送icecandidate给" + username);
        }else {
            console.log($("#staffUsername").html() + "发送icecandidate给" + username +"完毕");
        }
    };

    pc[username].onaddstream=function(e){
        console.log(e.stream);
        try{
            document.getElementById(username).srcObject = e.stream;
        }catch(error){
            document.getElementById(username).src=window.URL.createObjectURL(e.stream);
        }
        console.log($("#staffUsername").html()+"获取远程媒体成功："+username);
        console.log(pc[username]);
    }

    if(locatstream){
        pc[username].addStream(locatstream);
        console.log($("#staffUsername").html()+"发起方绑定本地流");
    }

    //主动连接方创建offer
    pc[username].createOffer(function(offer){
        console.log(username+"开始创建offer");
        pc[username].setLocalDescription(offer);
        var obj =JSON.stringify({
            'type':"offer",
            'username':$("#staffUsername").html(),
            'staffname':$("#staffName").html(),
            'send':username,
            'state':"con",
            'data':offer
        });
        VCSWebsocket.send(obj);
        console.log($("#staffUsername").html()+"发送offer");
    },function () {
        console.log("创建offer失败");
    });
}


/**
 * 画图相关功能
 */

var tempContext = null; // global variable 2d context
window.onload = function() {
    var canvas=document.getElementById("selfCanvas");
    if (!canvas.getContext) {
        console.log("Canvas not supported. Please install a HTML5 compatible browser.");
        alert("Canvas not supported. Please install a HTML5 compatible browser.");
    }
    tempContext = canvas.getContext("2d");
    canvas.addEventListener("mousedown", doMouseDown, true);
}
//获取鼠标在canvas中的坐标值,还需更加准确一点
function getPointOnCanvas(canvas, x, y) {
    var bbox =canvas.getBoundingClientRect();
    x=(x-bbox.left)*(canvas.width / bbox.width);
    y=(y-bbox.top)*(canvas.height / bbox.height);
    return{x:x,y:y};
}

function doMouseDown(event){
    var event=event?event:window.event;
    var x = event.clientX ;
    var y = event.clientY;
    var pos=getPointOnCanvas(document.getElementById("selfCanvas"),x,y)
    x=pos.x;
    y=pos.y;
    console.log("x:"+x+" y:"+y);
    tempContext.moveTo(x,y);
    //发送画图信息包给别人
    if($("#shareWhite").is(':checked')){
        VCSWebsocket.send(JSON.stringify({
            'type': "paintOne",
            'username': $("#staffUsername").html(),
            'staffname': $("#staffName").html(),
            'data': {'x': x,'y':y }
        }));
    }
    document.onmousemove = function(ev){
        var ev = ev || window.event;
        x = ev.clientX ;
        y = ev.clientY;
        pos=getPointOnCanvas(document.getElementById("selfCanvas"),x,y)
        x=pos.x;
        y=pos.y;
        tempContext.lineTo(x,y);
        tempContext.stroke();
        if($("#shareWhite").is(':checked')){
            VCSWebsocket.send(JSON.stringify({
                'type': "paintTwo",
                'username': $("#staffUsername").html(),
                'staffname': $("#staffName").html(),
                'data': {'x': x,'y':y }
            }));
        }
    };
    document.onmouseup = function(){
        document.onmousemove = null;
        document.onmouseup = null;
    };
}

$("#clearWhiteBtn").click(function () {
    clearCanvas();
})

function clearCanvas()
{
    var c=document.getElementById("selfCanvas");
    tempContext=c.getContext("2d");
    tempContext.fillStyle="#ffffff";
    tempContext.beginPath();
    tempContext.fillRect(0,0,c.width,c.height);
    tempContext.closePath();
}

$("#sendWhiteBtn").click(function () {
    VCSWebsocket.send(JSON.stringify({
        'type': "picture",
        'username': $("#staffUsername").html(),
        'staffname': $("#staffName").html(),
        'data': document.getElementById("selfCanvas").toDataURL()
    }));
})

//文件上传功能
//文件上传框初始化

$("#userUploadFile").fileinput({
    uploadUrl:"/file/upload",
    language : 'zh',
    autoReplace: true,
    uploadAsync: true, //默认异步上传
    maxFileCount: 1,
    showPreview:true,
    dropZoneEnabled: false,//是否显示拖拽区域
    maxFileSize : 20000,//上传文件最大的尺寸,单位为kb
    allowedFileExtensions: ['jpg','png','txt','doc','docx','ppt','pptx','xls','xlsx','pdf'],
    enctype:'multipart/form-data',
    validateInitialCount:true,
    uploadExtraData:{vcsId:$("#vcs_Id").html()},
    showAjaxErrorDetails:false//不显示详细的错误信息
});


//导入文件上传完成之后的事件
$("#userUploadFile").on("fileuploaded", function (event, data) {
    var res = data.response;
    if (res.state == 1) {
        alert('上传成功');
        //将上传文件的绝对路径发送给其他参加会议的人员
        var message={};
        message["type"]="fileShare";
        message["fileUrl"]=res.fileurl;
        message["fileName"]=res.filename;
        VCSWebsocket.send(JSON.stringify(message));
    }
    else if(res.state == 0){
        alert('上传失败,文件为空！')
    }
    else if(res.state == 2){
        alert('上传失败,文件没成功保存！')
    }
});

