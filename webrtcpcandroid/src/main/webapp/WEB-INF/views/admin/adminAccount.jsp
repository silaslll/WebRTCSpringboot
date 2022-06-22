<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9 column" id="adminContent">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>账号管理</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <form class="form-inline" id="accountSearchForm">
                <div class="form-group">
                    <label class="sr-only">所属部门</label>
                    <select name="id" class="form-control">
                    </select>
                </div>
                <button type="button" class="btn btn-default" id="account_search_btn">查询</button>
            </form>
        </div>
        <div class="col-md-4 col-md-push-2">
            <button type="button" id="add_modal_btn" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>添加</button>
            <button type="button" id="delete_batch_btn" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>删除</button>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="accounts_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>账号名</th>
                    <th>密码</th>
                    <th>部门</th>
                    <th>职位</th>
                    <th>姓名</th>
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


<%--以下是模态框--%>
<%--添加账号模态框--%>
<div class="modal fade" id="addAccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">账号添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">账号</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="username" name="username" placeholder="请输入账号" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="repassword" class="col-sm-2 control-label">重复密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="repassword" placeholder="请再次输入密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="accountDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <select id="accountDeptName" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="accountStaffName" class="col-sm-2 control-label">所属职员</label>
                        <div class="col-sm-10">
                            <select id="accountStaffName" name="sId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary saveAccountBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
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
                        <label for="modifyPassword" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="modifyPassword" name="password" placeholder="请输入密码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyRePassword" class="col-sm-2 control-label">重复密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="modifyRePassword" name="" placeholder="请再次输入密码" required>
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

<script src="../../../static/myjs/admin/adminAccount.js"></script>
