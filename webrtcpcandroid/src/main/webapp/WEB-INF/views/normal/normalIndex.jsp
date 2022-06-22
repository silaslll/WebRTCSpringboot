<%--
  Created by IntelliJ IDEA.
  User: yl1757
  Date: 2018/3/9
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>视频会议系统</title>
    <link rel="stylesheet" href="../../../static/bootstrap/css/bootstrap.min.css"/>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">

            <ul class="nav nav-tabs">
                <li class="navbar-header">
                    <a class="navbar-brand" href="#">视频会议系统</a>
                </li>
                <li class="pull-right">
                    <a href="/login/unlogin">退出</a>
                </li>
                <li class="pull-right">
                    <a href="#" id="modifyUserPassword">修改密码</a>
                </li>
                <li class="pull-right">
                    <a href="#" id="userDetailInform">用户信息</a>
                </li>
                <li class="pull-right">
                    <a href="#"> 姓名：${sessionScope.staffName} </a>
                </li>
                <li class="pull-right">
                    <a href="#" id="staffUsername">${sessionScope.username}</a>
                </li>
            </ul>


            <div class="row clearfix">
                <div class="col-md-9 column">
                    <%--视频会议列表--%>
                        <br/>
                        <div class="row">
                            <div class="col-md-8 col-md-push-8">
                                <form class="form-inline" id="vcs_search_form">
                                    <div class="form-group">
                                        <label class="sr-only">主题</label>
                                        <input type="text" name="vcsTitle" class="form-control" placeholder="请输入会议主题">
                                    </div>
                                    <button type="button" class="btn btn-default" id="vcs_search_btn">查询</button>
                                </form>
                            </div>
                        </div>
                        <!-- 显示表格数据 -->
                        <div class="row">
                            <div class="col-md-12">
                                <table class="table table-hover" id="vcss_table">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>会议主题</th>
                                        <th>创建人</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="col-md-6" id="page_info_area"></div>
                                <div class="col-md-6" id="page_nav_area"></div>
                            </div>
                        </div>
                </div>

                <div class="col-md-3 column">
                    <h4>在线人员</h4>
                    <%--上线相关列表--%>
                    <div style="width:260px;height:520px; overflow:scroll; border:1px solid;" id="staffOnline">

                    </div>
                    <br/>
                    <div class="col-md-3 col-md-push-8">
                        <button type="button" id="create_VCS_btn" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>创建会议</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--模态框--%>
<%--编辑信息模态框--%>
<%--职员模态框--%>
<div class="modal fade" id="staffModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">职员</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="staffName" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffName" name="name" value="${sessionScope.staffName}" disabled>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffSex" class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffSex" name="sex" value="${sessionScope.staffSex}" disabled>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffAge" name="age" placeholder="请输入年龄1-99" value="${sessionScope.staffAge}" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffPhone" class="col-sm-2 control-label">手机号码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffPhone" name="phone" placeholder="请输入11位手机号码" value="${sessionScope.staffPhone}" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffDeptName" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10">
                            <select id="staffDeptName" class="input-sm" name="dId" class="form-control">
                                <option value="${sessionScope.staffDid}" selected>${sessionScope.staffDeptName}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffPostName" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10">
                            <select id="staffPostName" class="input-sm" name="pId" class="form-control">
                                <option value="${sessionScope.staffPid}" selected>${sessionScope.staffPostName}</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary modifyStaffBtn"  edit_id="${sessionScope.staffId}"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>

<%--修改密码模态框--%>
<div class="modal fade" id="modifyAccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel2">密码修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="oldPassword" class="col-sm-2 control-label">原密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="oldPassword" name="password" placeholder="请输入原密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyPassword" class="col-sm-2 control-label">新密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="modifyPassword" name="password" placeholder="请输入新密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyRePassword" class="col-sm-2 control-label">重复新密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="modifyRePassword" name="" placeholder="请再次输入新密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary modifyAccountBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>



<script src="../../../static/jquery-3.2.1.min.js"></script>
<script src="../../../static/bootstrap/js/bootstrap.min.js"></script>
<script src="../../../static/myjs/normal/normalIndex.js"></script>
</body>
</html>
