<%--
  Created by IntelliJ IDEA.
  User: yl1757
  Date: 2018/3/19
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>视频会议</title>
    <link rel="stylesheet" href="../../../static/bootstrap/css/bootstrap.min.css"/>
</head>
<body>
<div class="row clearfix">
    <div class="col-md-12 column">

        <ul class="nav nav-tabs">
            <li class="navbar-header">
                <a class="navbar-brand" href="#">视频会议</a>
            </li>
            <li class="navbar-header">
                <a  href="#" id="vcs_Id">${sessionScope.vcs_Id}</a>
            </li>
            <li class="pull-right">
                <a href="/Page/normal/NormalIndex">退出会议</a>
            </li>
            <li class="pull-right">
                <a href="#" id="staffName"> ${sessionScope.staffName} </a>
            </li>
            <li class="pull-right">
                <a href="#" id="staffUsername">${sessionScope.username}</a>
            </li>
        </ul>


        <div class="row clearfix">
            <div class="col-md-8 column" id="VCSListContent">
                <%--在线视频列表--%>
                <br/>
                <div class="col-md-6 column">
                    <h6>本地</h6>
                    <video height="320" width="340" autoplay id="self">

                    </video>
                </div>

            </div>

            <div class="col-md-4 column">
                <%--文字聊天--%>
                <textarea class="form-control" name="textChat" id="textChat" style="overflow:scroll; overflow-x:hidden; " cols="30" rows="10" disabled></textarea><br/>
                    输入：<input type="text" class="form-control" id="textOfSend" placeholder="请输入要发送的内容"><br/>
                    <input type="checkbox" id="shareWhite">共享白板
                    <button type="button" id="sendTextBtn" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-edit">发送信息</span></button>
                    <button type="button" id="clearWhiteBtn" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-edit">清空白板</span></button>
                    <button type="button" id="sendWhiteBtn" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-edit">发送白板</span></button>
                    <br/>
                <br/><canvas id="selfCanvas"style="border: 1px solid;height:320px;width:100% "></canvas>
                <br/>
                <h6>文件共享</h6>
                <input type="file" id="userUploadFile" name="userUploadFile" >
                <span class="help-block">支持['jpg','png','txt','doc','docx','ppt','pptx','xls','xlsx','pdf']，若出现表单上传错误则说明你未选择上传文件或文件类型错误</span>
                <br/>
                <h6>文件列表</h6>
                <div class="col-md-12 column" id="shareFileList">

                </div>
            </div>
        </div>
    </div>
</div>





<link rel="stylesheet" href="../../../static/bootstrap/fileinput/css/fileinput.css"/>
<script src="../../../static/jquery-3.2.1.min.js"></script>
<script src="../../../static/bootstrap/js/bootstrap.min.js"></script>
<script src="../../../static/bootstrap/fileinput/fileinput.js"></script>
<script src="../../../static/bootstrap/fileinput/locales/zh.js"></script>
<script src="../../../static/myjs/normal/VCSindex.js"></script>
</body>
</html>
