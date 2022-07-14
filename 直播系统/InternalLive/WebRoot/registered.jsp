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

<title>"wǒ乄冭嘽純"直播系统注册</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link type="text/css" rel="stylesheet" href="css/sms.css" />
<style type="text/css">
#username,#password,#affirm,#email {
	position: relative;
	bottom: -5px;
}
</style>
</head>
<script type="text/javascript" src="jsbar/jquery-1.12.4.js"></script>
<script type="text/javascript">
	function check(){
		
		var password = $("#password").val();
		var affirm = $("#affirm").val();
		 if(password != affirm){
			alert("两次密码不同！");
			return false;
		}
		return true;
	}
	
	function una(){
		var username = $("#username").val();
		$.post("userServlet?action=unas","username="+username,ca);
		function ca(data){
			if(data == "true"){
				alert("该用户名已被使用,请你重新输入!");
				$("#username").val("");
			}
		}
	}
</script>
<body>
	<div id="regTitle" class="png"></div>
	<div id="regForm" class="userForm png">

		<form action="userServlet?action=regist" onsubmit="return check()"
			method="post">
			<dl>
				<div id="error">${error }</div>
				<dt>用 户 名：</dt>
				<dd>
					<input type="text" name="username" id="username" required
						placeholder="英文开头长度4-9" pattern="[A-Za-z][A-Za-z0-9]{3,8}"
						onblur="una()">
				</dd>
				<dt>密 码：</dt>
				<dd>
					<input type="password" name="password" id="password" required
						placeholder="密码长度为6位" pattern="[A-Za-z0-9]{6}" />
				</dd>
				<dt>确认密码：</dt>
				<dd>
					<input type="password" name="affirm" id="affirm" required
						placeholder="密码长度为6位" pattern="[A-Za-z0-9]{6}" />
				</dd>
				<dt>邮 箱：</dt>
				<dd>
					<input type="text" name="email" id="email" required
						placeholder="该邮箱为163邮箱" />
				</dd>
			</dl>
			<div class="buttons">
				<input class="btn-reg png" type="submit" name="register" value=" " /><input
					class="btn-reset png" type="reset" name="reset" value=" " />
			</div>
			<div class="goback">
				<a href="loger.jsp" class="png">返回登录页</a>
			</div>
		</form>
	</div>
</body>
</html>
