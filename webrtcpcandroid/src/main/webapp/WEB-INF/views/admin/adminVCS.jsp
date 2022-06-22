<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9 column" id="adminContent">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>会议管理</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8">
            <form class="form-inline" id="vcs_search_form">
                <div class="form-group">
                    <label class="sr-only">主题</label>
                    <input type="text" name="vcsTitle" class="form-control" placeholder="请输入会议主题">
                </div>
                <button type="button" class="btn btn-default" id="vcs_search_btn">查询</button>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 col-md-offset-10">
            <button type="button" id="delete_batch_btn" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>删除</button>
        </div>
    </div>

    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="vcss_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>#</th>
                    <th>会议主题</th>
                    <th>创建人</th>
                    <th>会议状态</th>
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
<%--参会者模态框--%>
<div class="modal fade" id="joinerModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">参与者</h4>
            </div>
            <div class="modal-body">
                <table class="table table-hover" id="joins_table">
                    <thead>
                    <tr>
                        <th>用户账号</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

<script src="../../../static/myjs/admin/adminVCS.js"></script>