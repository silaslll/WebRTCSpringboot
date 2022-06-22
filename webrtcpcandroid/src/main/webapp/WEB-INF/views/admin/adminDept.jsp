<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9 column" id="adminContent">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>部门管理</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 col-md-offset-10">
            <button type="button" id="add_modal_btn" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>添加</button>
            <button type="button" id="delete_batch_btn" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>删除</button>
        </div>
    </div>

    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="depts_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>#</th>
                    <th>名称</th>
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

<%--以下为模态框内容--%>
<%--模态框一：添加部门--%>
<div class="modal fade" id="addDeptModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">部门添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="deptname" class="col-sm-2 control-label">部门名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="deptname" name="name" placeholder="请输入部门名称" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary saveDeptBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>
<%--模态框二：修改部门--%>
<div class="modal fade" id="modifyDeptModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel2">部门修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="modifydeptname" class="col-sm-2 control-label">部门名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="modifydeptname" name="name" placeholder="请输入部门名称" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary modifyDeptBtn"><span class="glyphicon glyphicon-floppy-disk"></span>修改</button>
            </div>
        </div>
    </div>
</div>


<script src="../../../static/myjs/admin/adminDept.js"></script>