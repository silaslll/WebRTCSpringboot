<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9 column" id="adminContent">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>职位管理</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8">
            <form class="form-inline" id="post_select_form">
                <div class="form-group">
                    <label class="sr-only">职位名称</label>
                    <input type="text" name="name" class="form-control" placeholder="职位名称">
                </div>
                <div class="form-group">
                    <label class="sr-only">所属部门</label>
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

    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="posts_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>#</th>
                    <th>名称</th>
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

<%--模态框内容--%>
<%--添加职位模态框--%>
<div class="modal fade" id="addPostModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">职位添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="postname" class="col-sm-2 control-label">职位名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="postname" name="name" placeholder="请输入职位名称" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="postDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <select id="postDeptName" name="dId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary savePostBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>

<%--修改职位模态框--%>
<div class="modal fade" id="modifyPostModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel2">职位修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="modifypostname" class="col-sm-2 control-label">职位名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="modifypostname" name="name" placeholder="请输入职位名称" required>
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="modifypostDeptName" class="col-sm-2 control-label">所属部门</label>
                        <div class="col-sm-10">
                            <select id="modifypostDeptName" name="dId" class="form-control"/>
                            <span class="help-block"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary modifyPostBtn"><span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
            </div>
        </div>
    </div>
</div>

<script src="../../../static/myjs/admin/adminPost.js"></script>