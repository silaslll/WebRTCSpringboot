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
            //选择在加载完部门列表后加载职位列表，避免职位列表的加载因为 需要传递的相应部门ID不存在而使得请求不合理，导致无法将请求顺利传达到后端控制器。
            getPostList();
            getPostList2()
        }
    })
}
//加载职位信息，若未选定部门则全部加载,这儿是添加职员模态框的
function getPostList() {
    var pathParm=$("#staffDeptName").val();
    if(pathParm == null || pathParm == ""){
        pathParm = 0;
    }
    $.ajax({
        url:"/post/"+pathParm,
        type:"get",
        success:function (result) {
            $("#staffPostName").empty();
            $("#staffPostName").append(
                '<option value="0" selected="true">职位</option>'
            )
            $.each(result.extend.posts,function (index,item) {
                $("#staffPostName").append(
                    '<option value="'+item.id+'">'+item.name+'</option>'
                )
            })
        }
    })
}
function getPostList2() {
    var pathParm=$("#modifyStaffDeptName").val();
    if(pathParm == null || pathParm == ""){
        pathParm = 0;
    }
    $.ajax({
        url:"/post/"+pathParm,
        type:"get",
        success:function (result) {
            $("#modifyStaffPostName").empty();
            $("#modifyStaffPostName").append(
                '<option value="0" selected="true">职位</option>'
            )
            $.each(result.extend.posts,function (index,item) {
                $("#modifyStaffPostName").append(
                    '<option value="'+item.id+'">'+item.name+'</option>'
                )
            })
        }
    })
}
//监听部门是否变动
$("#staffDeptName").bind("change",function () {
    getPostList()
});
$("#modifyStaffDeptName").bind("change",function () {
    getPostList2()
});


//按条件前往查看某一页的员工信息
function go_page(pageNo) {
    $.ajax({
        url:"/staff/pageList",
        data:"pageNo=" + pageNo +"&"+ $("#post_select_form").serialize(),
        type:"get",
        success:function (result) {
            //alert(result);
            //首先构建职位信息表
            build_staff_table(result);
            //创建分页信息导航栏
            build_page_info(result);
        }
    })
}
//构建职位信息表
function build_staff_table(result){
    // 清空表格内容
    $("#staffs_table tbody").empty();
    var staffs = result.extend.pageInfo.list;
    $.each(staffs, function (index, item) {
        $("#staffs_table tbody").append(
            '<tr><td><input type="checkbox" class="check_item" value="'+item.id+'"/> </td>'+
            '<td>'+ item.id + '</td>'+
            '<td>'+ item.name + '</td>'+
            '<td>'+ item.sex + '</td>'+
            '<td style="display:none;">'+ item.age + '</td>'+
            '<td style="display:none;">'+ item.phone + '</td>'+
            '<td>'+ item.post.name + '</td>'+
            '<td>'+ item.post.dept.name + '</td>'+
            '<td><button class="btn btn-primary btn-sm details_btn" details_id="'+ item.id +'"><span class="glyphicon glyphicon-list-alt"></span>详细</button>'+' '+
            '<button class="btn btn-primary btn-sm edit_btn" edit_id="'+ item.id +'" pId="'+item.pId+'" dId="'+ item.post.dId +'"><span class="glyphicon glyphicon-edit"></span>编辑</button>'+' '+
            '<button class="btn btn-danger btn-sm delete_btn" delete_id="'+ item.id +'"><span class="glyphicon glyphicon-trash"></span>删除</button></td>'+
            '</tr>'
        );
    });
    //绑定详细信息、编辑信息与删除信息按钮功能
    $(".details_btn").click(function () {
        $("#detailStaffname").val($(this).parent().prev().prev().prev().prev().prev().prev().html());
        $("#detailStaffSex").val($(this).parent().prev().prev().prev().prev().prev().html());
        $("#detailStaffAge").val($(this).parent().prev().prev().prev().prev().html());
        $("#detailStaffPhone").val($(this).parent().prev().prev().prev().html());
        $("#detailStaffDeptName").val($(this).parent().prev().prev().html());
        $("#detailStaffPostName").val($(this).parent().prev().html());
        $("#detailStaffModal").modal({backdrop:"static"});
        $.ajax({
            url:"/account/admin/"+$(this).attr("details_id"),
            type:"get",
            success:function (result) {
                if(result.code == 100){
                    //存在关联账号
                    $("#StaffUsername").val(result.extend.account.username);
                    $("#StaffPassword").val(result.extend.account.password);
                }else if(result.code == 200){
                    //不存在关联账号
                    $("#StaffUsername").val("无");
                    $("#StaffPassword").val("无");
                }
            }
        })
    })

    $(".delete_btn").click(function () {
        if(confirm("确认删除员工："+$(this).parent().prev().prev().prev().prev().prev().prev().html()+"?")){
            $.ajax({
                url:"/staff/admin/"+$(this).attr("delete_id"),
                type:"delete",
                success:function (result) {
                    if(result == "success"){
                        alert("删除成功！");
                        go_page(currentPagenumber);
                    }else{
                        alert("删除失败！");
                    }
                }
            })
        }
    })

    $(".edit_btn").click(function(){
        //将相关信息填入
        var sex = $(this).parent().prev().prev().prev().prev().prev().html();
        $("#modifyStaffname").val($(this).parent().prev().prev().prev().prev().prev().prev().html());
        $("#modifyStaffModal :radio").each(function (index,item) {
            if($(this).val() == sex)  $(this).attr("checked","checked");
        })
        $("#modifyStaffAge").val($(this).parent().prev().prev().prev().prev().html());
        $("#modifyStaffPhone").val($(this).parent().prev().prev().prev().html());
        $("#modifyStaffDeptName").val($(this).attr("dId"));
        $("#modifyStaffPostName").val($(this).attr("pId"));
        $(".modifyStaffBtn").attr("edit_id",$(this).attr("edit_id"));
        $("#modifyStaffModal").modal({backdrop:"static"});
        clearWaring2();
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
$("#post_search_btn").click(function () {
    go_page(1);
})

// 添加模态框弹出
$("#add_modal_btn").click(function () {
    clearWaring();
    // 清空表单数据
    $("#staffname").val("");
    $("#staffAge").val("");
    $("#staffPhone").val("");
    // 弹出模态框
    $("#addStaffModal").modal({backdrop: "static"});
});

//添加职员信息
//清除相关提示信息
function clearWaring() {
    $("#staffname").parent().removeClass("has-success has-error");
    $("#staffname").next().empty();
    $("#staffAge").parent().removeClass("has-success has-error");
    $("#staffAge").next().empty();
    $("#staffPhone").parent().removeClass("has-success has-error");
    $("#staffPhone").next().empty();
    $("#staffDeptName").parent().removeClass("has-success has-error");
    $("#staffDeptName").next().empty();
    $("#staffPostName").parent().removeClass("has-success has-error");
    $("#staffPostName").next().empty();
}
//添加职员
$(".saveStaffBtn").click(function () {
    clearWaring();
    //前端表单校验
    //姓名非空
    var regBlank = /^\s*$/;
    if($("#staffname").val() == null || regBlank.test($("#staffname").val())){
        $("#staffname").parent().addClass("has-error");
        $("#staffname").next().append("请输入姓名！");
        return false;
    }
    //年龄非空
    if($("#staffAge").val() == null || regBlank.test($("#staffAge").val())){
        $("#staffAge").parent().addClass("has-error");
        $("#staffAge").next().append("请输入年龄！");
        return false;
    }
    //手机非空
    if($("#staffPhone").val() == null || regBlank.test($("#staffPhone").val())){
        $("#staffPhone").parent().addClass("has-error");
        $("#staffPhone").next().append("请输入手机号码！");
        return false;
    }
    //部门非空
    if($("#staffDeptName").val() == 0){
        $("#staffDeptName").parent().addClass("has-error");
        $("#staffDeptName").next().append("请选择具体部门");
        return false;
    }
    //职位非空
    if($("#staffPostName").val() == 0){
        $("#staffPostName").parent().addClass("has-error");
        $("#staffPostName").next().append("请选择具体职位");
        return false;
    }
    var regAge = /^[0-9]{1,2}$/;
    if(!regAge.test($("#staffAge").val()) || $("#staffAge").val() == "0" || $("#staffAge").val() == "00" ){
        $("#staffAge").parent().addClass("has-error");
        $("#staffAge").next().append("年龄范围：1-99！");
        return false;
    }
    var regPhone = /^[1][3,4,5,7,8][0-9]{9}$/;
    if(!regPhone.test($("#staffPhone").val())){
        $("#staffPhone").parent().addClass("has-error");
        $("#staffPhone").next().append("请输入正确的11位手机号码！");
        return false;
    }
    //发起请求，通过后端校验后可以直接保存
    $.ajax({
        url:"/staff/admin",
        data:$("#addStaffModal form").serialize(),
        type:"post",
        success:function (result) {
            if(result == "success"){
                clearWaring();
                // 关闭模态框
                $("#addStaffModal").modal('hide');
                alert("保存成功！");
                go_page(lastPageNumber+1);
            }else if(result == "nameNull"){
                clearWaring();
                $("#staffname").parent().addClass("has-error");
                $("#staffname").next().append("请输入姓名！");
            }else if(result == "ageNull"){
                clearWaring();
                $("#staffAge").parent().addClass("has-error");
                $("#staffAge").next().append("请输入年龄！");
            }else if(result == "phoneNull"){
                clearWaring();
                $("#staffPhone").parent().addClass("has-error");
                $("#staffPhone").next().append("请输入手机号码！");
            }else if(result == "postNull"){
                clearWaring();
                $("#staffPostName").parent().addClass("has-error");
                $("#staffPostName").next().append("请选择具体职位");
            }else if(result == "ageError"){
                clearWaring();
                $("#staffAge").parent().addClass("has-error");
                $("#staffAge").next().append("年龄范围：1-99！");
            }else if(result == "phoneError"){
                clearWaring();
                $("#staffPhone").parent().addClass("has-error");
                $("#staffPhone").next().append("请输入正确的11位手机号码！");
            }else if(result == "error"){
                clearWaring();
                // 关闭模态框
                $("#addStaffModal").modal('hide');
                alert("保存失败！")
            }
        }
    })
})

//修改职员信息
//清除相关提示信息
function clearWaring2() {
    $("#modifyStaffname").parent().removeClass("has-success has-error");
    $("#modifyStaffname").next().empty();
    $("#modifyStaffAge").parent().removeClass("has-success has-error");
    $("#modifyStaffAge").next().empty();
    $("#modifyStaffPhone").parent().removeClass("has-success has-error");
    $("#modifyStaffPhone").next().empty();
    $("#modifyStaffDeptName").parent().removeClass("has-success has-error");
    $("#modifyStaffDeptName").next().empty();
    $("#modifyStaffPostName").parent().removeClass("has-success has-error");
    $("#modifyStaffPostName").next().empty();
}
//修改职员信息
$(".modifyStaffBtn").click(function () {
    clearWaring2();
    //前端表单校验
    //姓名非空
    var regBlank = /^\s*$/;
    if($("#modifyStaffname").val() == null || regBlank.test($("#modifyStaffname").val())){
        $("#modifyStaffname").parent().addClass("has-error");
        $("#modifyStaffname").next().append("请输入姓名！");
        return false;
    }
    //年龄非空
    if($("#modifyStaffAge").val() == null || regBlank.test($("#modifyStaffAge").val())){
        $("#modifyStaffAge").parent().addClass("has-error");
        $("#modifyStaffAge").next().append("请输入年龄！");
        return false;
    }
    //手机非空
    if($("#modifyStaffPhone").val() == null || regBlank.test($("#modifyStaffPhone").val())){
        $("#modifyStaffPhone").parent().addClass("has-error");
        $("#modifyStaffPhone").next().append("请输入手机号码！");
        return false;
    }
    //部门非空
    if($("#modifyStaffDeptName").val() == 0){
        $("#modifyStaffDeptName").parent().addClass("has-error");
        $("#modifyStaffDeptName").next().append("请选择具体部门");
        return false;
    }
    //职位非空
    if($("#modifyStaffPostName").val() == 0){
        $("#modifyStaffPostName").parent().addClass("has-error");
        $("#modifyStaffPostName").next().append("请选择具体职位");
        return false;
    }
    var regAge = /^[0-9]{1,2}$/;
    if(!regAge.test($("#modifyStaffAge").val()) || $("#modifyStaffAge").val() == "0" || $("#modifyStaffAge").val() == "00" ){
        $("#modifyStaffAge").parent().addClass("has-error");
        $("#modifyStaffAge").next().append("年龄范围：1-99！");
        return false;
    }
    var regPhone = /^[1][3,4,5,7,8][0-9]{9}$/;
    if(!regPhone.test($("#modifyStaffPhone").val())){
        $("#modifyStaffPhone").parent().addClass("has-error");
        $("#modifyStaffPhone").next().append("请输入正确的11位手机号码！");
        return false;
    }
    //发起请求，通过后端校验后可以直接保存
    $.ajax({
        url:"/staff",
        data:"id="+$(".modifyStaffBtn").attr("edit_id")+"&"+$("#modifyStaffModal form").serialize(),
        type:"put",
        success:function (result) {
            if(result == "success"){
                clearWaring2();
                // 关闭模态框
                $("#modifyStaffModal").modal('hide');
                alert("修改成功！");
                go_page(currentPagenumber);
            }else if(result == "nameNull"){
                clearWaring2();
                $("#modifyStaffname").parent().addClass("has-error");
                $("#modifyStaffname").next().append("请输入姓名！");
            }else if(result == "ageNull"){
                clearWaring2();
                $("#modifyStaffAge").parent().addClass("has-error");
                $("#modifyStaffAge").next().append("请输入年龄！");
            }else if(result == "phoneNull"){
                clearWaring2();
                $("#modifyStaffPhone").parent().addClass("has-error");
                $("#modifyStaffPhone").next().append("请输入手机号码！");
            }else if(result == "postNull"){
                clearWaring2();
                $("#modifyStaffPostName").parent().addClass("has-error");
                $("#modifyStaffPostName").next().append("请选择具体职位");
            }else if(result == "ageError"){
                clearWaring2();
                $("#modifyStaffAge").parent().addClass("has-error");
                $("#modifyStaffAge").next().append("年龄范围：1-99！");
            }else if(result == "phoneError"){
                clearWaring2();
                $("#modifyStaffPhone").parent().addClass("has-error");
                $("#modifyStaffPhone").next().append("请输入正确的11位手机号码！");
            }else if(result == "error"){
                clearWaring2();
                // 关闭模态框
                $("#modifyStaffModal").modal('hide');
                alert("修改失败！")
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

$("#delete_batch_btn").click(function () {
    //先检查是否有目标被选中
    if($(".check_item:checked").length == 0){
        alert("请选择要批量删除的对象！");
        return false;
    }
    var ids=new Array();
    var staffNames="";
    $(".check_item:checked").each(function (index,item) {
        staffNames+=$(this).parent().next().next().html()+",";
        ids[index]=$(this).val();
    })
    staffNames=staffNames.substr(0,staffNames.length-1);
    if(confirm("请确认要删除的职员如下："+staffNames)){
        $.ajax({
            url:"/staff/admin",
            data:{_method:"delete",ids:ids},
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