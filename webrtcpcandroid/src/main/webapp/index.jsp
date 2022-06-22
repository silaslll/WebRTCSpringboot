<%--
  Created by IntelliJ IDEA.
  User: yl1757
  Date: 2018/3/8
  Time: 8:17
  To change this template use File | Settings | File Templates.
  用户登录界面
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>视频会议系统</title>
    <link rel="stylesheet" href="static/bootstrap/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <form class="form-signin" id="loginInfo">
            <h2 class="form-signin-heading">视频会议系统</h2>
            <label for="username" >用户名</label>
            <input type="text" id="username" class="form-control" name="username" placeholder="请输入用户名" required autofocus>
            <label for="password" >密码</label>
            <input type="password" id="password" class="form-control" name="password" placeholder="请输入密码" required>
            <button class="btn btn-lg btn-primary btn-block login" type="submit">登录</button>
        </form>
    </div>
<script src="static/jquery-3.2.1.min.js"></script>
<script src="static/bootstrap/js/bootstrap.min.js"></script>
<script src="static/myjs/index.js"></script>
</body>
</html>
