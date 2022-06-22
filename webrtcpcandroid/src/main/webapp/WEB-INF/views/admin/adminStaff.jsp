<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9 column" id="adminContent">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>职员管理</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <form class="form-inline" id="post_select_form">
                <div class="form-group">
                    <label class="sr-only">职位</label>
                    <input type="text" name="name" class="form-control" placeholder="请输入职位名称">
                </div>
                <div class="form-group">
                    <label class="sr-only">部门</label>
                    <select name="dId" class="form-control">
                    </select>
                </div>
                <button type="button" class="btn btn-default" id="post_search_btn">查询</button>
            </form>
        </div>
        <div class="col-md-4 col-md-push-2">
            <button type="button" id="add_modal_btn" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>添加</button>
            <button type="button" id="delete_batch_btn" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>删除</button>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="staffs_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>#</th>
                    <th>名称</th>
                    <th>性别</th>
                    <th>职位</th>
                    <th>部门</th>
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



<%--以下是模态框列表--%>
<%--添加职员模态框--%>
<div class="modal fade" id="addStaffModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">职员添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="staffname" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffname" name="name" placeholder="请输入姓名" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-10">
                            <input type="radio" class="input-group-sm" name="sex" value="男" checked>男</span>
                            <input type="radio" class="input-group-sm" name="sex" value="女">女</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffAge" name="age" placeholder="请输入年龄1-99" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffPhone" class="col-sm-2 control-label">手机号码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="staffPhone" name="phone" placeholder="请输入11位手机号码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <select id="staffDeptName" name="dId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="staffPostName" class="col-sm-2 control-label">所属职位</label>
                        <div class="col-sm-10">
                            <select id="staffPostName" name="pId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary saveStaffBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>

<%--编辑职员模态框--%>
<div class="modal fade" id="modifyStaffModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel2">职员修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="modifyStaffname" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="modifyStaffname" name="name" placeholder="请输入姓名" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-10">
                            <input type="radio" class="input-group-sm" name="sex" value="男" checked>男</span>
                            <input type="radio" class="input-group-sm" name="sex" value="女">女</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyStaffAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="modifyStaffAge" name="age" placeholder="请输入年龄1-99" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyStaffPhone" class="col-sm-2 control-label">手机号码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="modifyStaffPhone" name="phone" placeholder="请输入11位手机号码" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyStaffDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <select id="modifyStaffDeptName" name="dId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifyStaffPostName" class="col-sm-2 control-label">所属职位</label>
                        <div class="col-sm-10">
                            <select id="modifyStaffPostName" name="pId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary modifyStaffBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>

<%--员工详细信息模态框--%>
<div class="modal fade" id="detailStaffModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel3" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel4">职员信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="detailStaffname" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffname" name="name" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffSex" name="sex" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="detailStaffAge" class="col-sm-2 control-label">年龄</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffAge" name="age"  disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="detailStaffPhone" class="col-sm-2 control-label">手机号码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffPhone" name="phone"  disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="detailStaffDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffDeptName" name="phone" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="detailStaffPostName" class="col-sm-2 control-label">所属职位</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="detailStaffPostName" name="phone" disabled>
                        </div>
                    </div>
                </form>

                <h4 class="modal-title">关联账号</h4>
                <form id="account">
                    <div class="form-group">
                        <label for="StaffUsername"  class="col-sm-2 control-label">账号</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="StaffUsername" name="username" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="StaffPassword"  class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="StaffPassword" name="password" disabled>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

<script src="../../../static/myjs/admin/adminStaff.js"></script>
