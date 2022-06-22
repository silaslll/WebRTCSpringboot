<%--
  Created by IntelliJ IDEA.
  User: yl1757
  Date: 2018/3/9
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>视频会议管理界面</title>
    <link rel="stylesheet" href="../../../static/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <%--最上方的导航栏--%>
            <ul class="nav nav-tabs">
                <li class="navbar-header">
                    <a class="navbar-brand" href="#">视频会议系统</a>
                </li>
                <li class="pull-right">
                    <a href="/login/unlogin">退出</a>
                </li>
            </ul>


            <div class="row clearfix">
                <div class="col-md-2 column" id="guideContent">
                    <div class="list-group">
                        <a class=" list-group-item active" data-toggle="collapse" data-parent="#guideContent" href="#guideContentOne">部门管理</a>

                        <div id="guideContentOne" class="collapse in">
                            <div class="list-group-item">
                                <a href="javascript:void(0);" name="/Page/admin/AdminDept">
                                    <span class="glyphicon glyphicon-triangle-right"></span>部门信息管理</a>
                            </div>
                            <div class="list-group-item">
                                <a href="javascript:void(0);" name="/Page/admin/AdminPost">
                                    <span class="glyphicon glyphicon-triangle-right"></span>职位信息管理</a>
                            </div>
                        </div>
                    </div>

                    <div class="list-group">
                        <a class="list-group-item active" data-toggle="collapse" data-parent="#guideContent" href="#guideContentTwo">人事管理</a>
                        <div id="guideContentTwo" class="collapse in">
                            <div class="list-group-item">
                                <a href="javascript:void(0);" name="/Page/admin/AdminStaff">
                                    <span class="glyphicon glyphicon-triangle-right"></span>职员信息管理</a>
                            </div>
                            <div class="list-group-item">
                                <a href="javascript:void(0);" name="/Page/admin/AdminAccount">
                                    <span class="glyphicon glyphicon-triangle-right"></span>账号信息管理</a>
                            </div>
                        </div>
                    </div>

                    <div class="list-group">
                        <a class=" list-group-item active" data-toggle="collapse" data-parent="#guideContent" href="#guideContentThree">会议管理</a>

                        <div id="guideContentThree" class="collapse in">
                            <div class="list-group-item">
                                <a href="javascript:void(0);" name="/Page/admin/AdminVCS">
                                    <span class="glyphicon glyphicon-triangle-right"></span>会议信息管理</a>
                            </div>
                        </div>
                    </div>

                </div>
                <%--网页加载区域--%>
                <div class="col-md-10 column" id="adminContent">
                    <div class="col-md-9 column">
                        <div class="jumbotron">
                            <h1>
                                你好，管理员！
                            </h1>
                            <p>
                                请您自行选择要进行的操作！谢谢！
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="../../../static/jquery-3.2.1.min.js"></script>
<script src="../../../static/bootstrap/js/bootstrap.min.js"></script>
<script src="../../../static/myjs/admin/adminIndex.js"></script>
</body>
</html>
