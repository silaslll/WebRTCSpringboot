jQuery.ajaxSetup ({cache:false})
var lastPageNumber;
var currentPagenumber;
$(function () {
    getDeptList();
    go_page(1);
})

//加载部门信息
function getDeptList(){
    $.ajax({
        url:"/dept",
        type:"get",
        success:function (result) {
            $("select").append(
                '<option value="0" selected="true">部门</option>'
            )
            $.each(result.extend.deptList,function (index,item) {
                $("select").append(
                    '<option value="'+item.id+'">'+item.name+'</option>'
                )
            })
            getStaffList();
        }
    })
}
function getStaffList(){
    $.ajax({
        url:"/staff/getListByDeptId",
        data:"dId="+$("#accountDeptName").val(),
        type:"get",
        success:function (result) {
            $("#accountStaffName").empty();
            $("#accountStaffName").append(
                '<option value="0" selected="true">职员</option>'
            )
            $.each(result,function (index,item) {
                $("#accountStaffName").append(
                    '<option value="'+item.id+'">'+item.name+'</option>'
                )
            })
        }
    })
}
//监听部门是否变动
$("#accountDeptName").bind("change",function () {
    getStaffList();
});
//按条件前往查看某一页的账号
function go_page(pageNo) {
    $.ajax({
        url:"/account/admin/getAccountPageList",
        data:"pageNo=" + pageNo +"&"+ $("#accountSearchForm").serialize(),
        type:"get",
        success:function (result) {
            //alert(result);
            //首先构建账号信息表
            build_account_table(result);
            //创建分页信息导航栏
            build_page_info(result);
        }
    })
}
//构建职位信息表
function build_account_table(result){
    // 清空表格内容
    $("#accounts_table tbody").empty();
    var accounts = result.extend.pageInfo.list;
    $.each(accounts, function (index, item) {
        $("#accounts_table tbody").append(
            '<tr><td><input type="checkbox" class="check_item" value="'+item.username+'"/> </td>'+
            '<td>'+ item.username + '</td>'+
            '<td>'+ item.password + '</td>'+
            '<td>'+ item.staff.post.dept.name + '</td>'+
            '<td>'+ item.staff.post.name + '</td>'+
            '<td>'+ item.staff.name + '</td>'+
            '<td><button class="btn btn-primary btn-sm edit_btn" edit_username="'+ item.username +'"><span class="glyphicon glyphicon-edit"></span>修改密码</button>'+' '+
            '<button class="btn btn-danger btn-sm delete_btn" delete_username="'+ item.username +'"><span class="glyphicon glyphicon-trash"></span>删除</button></td>'+
            '</tr>'
        );
    });
    //绑定编辑信息与删除信息按钮功能
    $(".delete_btn").click(function () {
        if(confirm("请确认要删除该用户吗？")){
            $.ajax({
                url:"/account/admin/"+$(this).attr("delete_username"),
                type:"delete",
                success:function (result) {
                    if(result == "success"){
                        alert("删除成功！");
                        go_page(currentPagenumber);
                    }else{
                        alert("删除失败！");
                        go_page(currentPagenumber);
                    }
                }
            })
        }
    })

    $(".edit_btn").click(function(){
        clearWaring2();
        $("#modifyPassword").val("");
        $("#modifyRePassword").val("");
        $(".modifyAccountBtn").attr("edit_username",$(this).attr("edit_username"));
        // 弹出模态框
        $("#modifyAccountModal").modal({backdrop: "static"});
    })

}

// 构建分页信息（查询信息）
function build_page_info(result) {
    lastPageNumber=result.extend.pageInfo.pages;
    // 清空分页信息
    $("#page_info_area").empty();
    $("#page_info_area").append("当前" + result.extend.pageInfo.pageNum + "页,共" +
        result.extend.pageInfo.pages + "页,总" +
        result.extend.pageInfo.total + "条记录");

    //清空分页导航栏信息
    $("#page_nav_area").empty();
    //添加分页导航栏
    $("#page_nav_area").append(
        '<ul class="pagination"><li class="first_page"><a href="#">首页</a></li>'+
        '<li class="previous_page"><a href="#">&laquo;</a></li></ul>'
    );
    currentPagenumber=result.extend.pageInfo.pageNum;
    $.each(result.extend.pageInfo.navigatepageNums,function (index,item) {
        if(result.extend.pageInfo.pageNum == item)
            $("#page_nav_area ul").append(
                '<li class="number"><a class="active" href="#">'+item+'</a></li>'
            )
        else $("#page_nav_area ul").append(
            '<li class="number"><a href="#">'+item+'</a></li>'
        )
    })
    $("#page_nav_area ul").append(
      '<li class="next_page"><a href="#">&raquo;</a></li>'+
        '<li class="last_page"><a href="#">尾页</a></li>'
    );
    //绑定分页栏相关点击事件
    $(".first_page").click(function () {
        if(result.extend.pageInfo.isFirstPage){
            $(".first_page").addClass("disabled");
        }else go_page(1);
    })

    $(".previous_page").click(function () {
        if(result.extend.pageInfo.hasPreviousPage){
            go_page(result.extend.pageInfo.pageNum - 1);
        }else $(".previous_page").addClass("disabled");
    })

    $(".next_page").click(function () {
        if(result.extend.pageInfo.hasNextPage){
            go_page(result.extend.pageInfo.pageNum + 1);
        }else $(".next_page").addClass("disabled");
    })

    $(".last_page").click(function () {
        if(result.extend.pageInfo.isLastPage){
            $(".last_page").addClass("disabled");
        }else go_page(result.extend.pageInfo.pages);
    })
    
    $(".number").click(function () {
        go_page($(this).children("a").html());
    })
}

//查询按钮功能
$("#account_search_btn").click(function () {
    go_page(1);
})

$("#add_modal_btn").click(function () {
    clearWaring();
    // 清空表单数据
    $("#username").val("");
    $("#password").val("");
    $("#repassword").val("");
    $("#addPostModal select").val("0");
    // 弹出模态框
    $("#addAccountModal").modal({backdrop: "static"});
});

function clearWaring(){
    $("#username").parent().removeClass("has-success has-error");
    $("#username").next().empty();
    $("#password").parent().removeClass("has-success has-error");
    $("#password").next().empty();
    $("#repassword").parent().removeClass("has-success has-error");
    $("#repassword").next().empty();
    $("#accountDeptName").parent().removeClass("has-success has-error");
    $("#accountDeptName").next().empty();
    $("#accountStaffName").parent().removeClass("has-success has-error");
    $("#accountStaffName").next().empty();
}

//保存账号
$(".saveAccountBtn").click(function () {
    clearWaring();
    var regBlank = /^\s*$/;
    if($("#username").val() == null || regBlank.test($("#username").val())){
        $("#username").parent().addClass("has-error");
        $("#username").next().append("请输入账号！");
        return false;
    }
    if($("#password").val() == null || regBlank.test($("#password").val())){
        $("#password").parent().addClass("has-error");
        $("#password").next().append("请输入密码！");
        return false;
    }
    if($("#repassword").val() == null || regBlank.test($("#repassword").val())){
        $("#repassword").parent().addClass("has-error");
        $("#repassword").next().append("请再次输入密码！");
        return false;
    }
    if($("#repassword").val() != $("#password").val()){
        $("#repassword").parent().addClass("has-error");
        $("#repassword").next().append("两次密码请一致！");
        return false;
    }
    if($("#accountDeptName").val() == 0){
        $("#accountDeptName").parent().addClass("has-error");
        $("#accountDeptName").next().append("请选择具体部门");
        return false;
    }
    if($("#accountStaffName").val() == 0){
        $("#accountStaffName").parent().addClass("has-error");
        $("#accountStaffName").next().append("请选择具体职员");
        return false;
    }
    $.ajax({
        url:"/account/admin",
        data:"rePassword="+$("#repassword").val()+"&"+$("#addAccountModal form").serialize(),
        type:"post",
        success:function (result) {
            if(result == "success"){
                clearWaring();
                // 关闭模态框
                $("#addAccountModal").modal('hide');
                alert("保存成功！");
                go_page(lastPageNumber+1);
            }else if(result == "usernameNull"){
                $("#username").parent().addClass("has-error");
                $("#username").next().append("请输入账号！");
            }else if(result == "passwordNull"){
                $("#password").parent().addClass("has-error");
                $("#password").next().append("请输入密码！");
            }else if(result == "passwordWrong"){
                $("#repassword").parent().addClass("has-error");
                $("#repassword").next().append("两次密码请一致！");
            }else if(result == "usernameRepeat"){
                $("#username").parent().addClass("has-error");
                $("#username").next().append("账号不可用！");
            }else if(result == "sidNull"){
                $("#accountStaffName").parent().addClass("has-error");
                $("#accountStaffName").next().append("请选择具体职员");
            }else if(result == "sIdWrong"){
                $("#accountStaffName").parent().addClass("has-error");
                $("#accountStaffName").next().append("该职员已有账号");
            }
            else{
                clearWaring();
                // 关闭模态框
                $("#addAccountModal").modal('hide');
                alert("保存失败！");
            }
        }
    })
})

function clearWaring2(){
    $("#modifyPassword").parent().removeClass("has-success has-error");
    $("#modifyPassword").next().empty();
    $("#modifyRePassword").parent().removeClass("has-success has-error");
    $("#modifyRePassword").next().empty();
}

//修改账号密码
$(".modifyAccountBtn").click(function () {
    clearWaring2();
    var regBlank = /^\s*$/;
    if($("#modifyPassword").val() == null || regBlank.test($("#modifyPassword").val())){
        $("#modifyPassword").parent().addClass("has-error");
        $("#modifyPassword").next().append("请输入密码！");
        return false;
    }
    if($("#modifyRePassword").val() == null || regBlank.test($("#modifyRePassword").val())){
        $("#modifyRePassword").parent().addClass("has-error");
        $("#modifyRePassword").next().append("请再次输入密码！");
        return false;
    }
    if($("#modifyRePassword").val() != $("#modifyPassword").val()){
        $("#modifyRePassword").parent().addClass("has-error");
        $("#modifyRePassword").next().append("两次密码请一致！");
        return false;
    }
    $.ajax({
        url:"/account",
        data:"rePassword="+$("#modifyRePassword").val()+"&"+"password="+$("#modifyPassword").val()+"&username="+$(this).attr("edit_username"),
        type:"put",
        success:function (result) {
            if(result == "success"){
                clearWaring2();
                // 关闭模态框
                $("#modifyAccountModal").modal('hide');
                alert("保存成功！");
                go_page(currentPagenumber);
            }else if(result == "passwordNull"){
                $("#modifyPassword").parent().addClass("has-error");
                $("#modifyPassword").next().append("请输入密码！");
            }else if(result == "passwordWrong"){
                $("#modifyRePassword").parent().addClass("has-error");
                $("#modifyRePassword").next().append("两次密码请一致！");
            }else{
                clearWaring2();
                // 关闭模态框
                $("#modifyAccountModal").modal('hide');
                alert("保存失败！");
            }
        }
    })
})



//全选功能
// 全选、全不选
$("#check_all").click(function () {
    $(".check_item").prop("checked", $(this).prop("checked"));
});

//判断当前选择中的元素是否是全部，如果是全部，就将全选框勾上
$(document).on("click", ".check_item", function () {
    var flag = $(".check_item:checked").length == $(".check_item").length;
    $("#check_all").prop("checked", flag);
});

//批量删除职位功能
$("#delete_batch_btn").click(function () {
    //先检查是否有目标被选中
    if($(".check_item:checked").length == 0){
        alert("请选择要批量删除的对象！");
        return false;
    }
    var usernames=new Array();
    $(".check_item:checked").each(function (index,item) {
        usernames[index]=$(this).val();
    })
    if(confirm("请确认要删除这些用户吗？")){
        $.ajax({
            url:"/account/admin",
            data:{_method:"delete",usernames:usernames},
            type:"post",
            success:function (result) {
                if(result == "success"){
                    alert("删除成功！");
                    go_page(currentPagenumber);
                }else{
                    alert("删除失败！");
                    go_page(currentPagenumber);
                }
            }
        })
    }
})
