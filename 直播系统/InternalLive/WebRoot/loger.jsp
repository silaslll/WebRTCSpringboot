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

<title>"wǒ乄冭嘽純"直播系统登录界面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" href="css/style.css" type="text/css"></link>
</head>

<body class="login_bg">
	<section class="loginBox"> <header class="loginHeader">
	<h1>"wǒ乄冭嘽純"直播系统</h1>
	</header> <section class="loginCont">
	<form class="loginForm" action="userServlet?action=login"
		name="actionForm" id="actionForm" method="post">
		<div class="info"></div>
		<div class="inputbox">
			<label for="user">用户名：</label> <input type="text" class="input-text"
				id="userCode" name="userCode" placeholder="请输入用户名" required />
		</div>
		<div class="inputbox">
			<label for="mima">密码：</label> <input type="password"
				id="userPassword" name="userPassword" placeholder="请输入密码" required />
		</div>
		<div class="inputbox">
			<label for="mima">验证码：</label> <input type="text" id="yanzhengma"
				name="yanzhengma" placeholder="请输入验证码" required style="width:100px" />
			&nbsp; <img alt="验证码" src="CaptchaServlet" id="imgCode"
				onclick="getCode()" style="width:90px">
		</div>
		<br />
		<div class="subBtn">

			<input type="submit" value="登录" /> <input type="reset" value="重置" />
			<a href="registered.jsp">注册</a>
		</div>
	</form>
	<script type="text/javascript">
		function $(id) {
			return document.getElementById(id);
		}

		function getCode() {
			$("imgCode").src = 'CaptchaServlet?' + Math.random();
		}
	</script> </section> </section>
</body>
</html>
