var lastPageNumber;
var currentPagenumber;
jQuery.ajaxSetup ({cache:false})
$(function () {
    go_page(1);
})

//前往查看某一页的部门信息
function go_page(pageNo) {
    $.ajax({
        url:"/dept/"+pageNo,
        type:"get",
        success:function (result) {
            //alert(result);
            //首先构建部门信息表
            build_dept_table(result);
            //创建分页信息导航栏
            build_page_info(result);
        }
    })
}
//构建部门信息表
function build_dept_table(result){
    // 清空表格内容
    $("#depts_table tbody").empty();
    var depts = result.extend.pageInfo.list;
    $.each(depts, function (index, item) {
        $("#depts_table tbody").append(
            '<tr><td><input type="checkbox" class="check_item" value="'+item.id+'"/> </td>'+
            '<td>'+ item.id + '</td>'+
            '<td>'+ item.name + '</td>'+
            '<td><button class="btn btn-primary btn-sm edit_btn" edit_id="'+ item.id +'"><span class="glyphicon glyphicon-edit"></span>编辑</button>'+' '+
            '<button class="btn btn-danger btn-sm delete_btn" delete_id="'+ item.id +'"><span class="glyphicon glyphicon-trash"></span>删除</button></td>'+
            '</tr>'
        );
    });
    //绑定编辑信息与删除信息按钮功能
    $(".delete_btn").click(function () {
        if(confirm("确认删除部门【"+$(this).parent().prev().html()+"】？")){
            var deleteId=$(this).attr("delete_id");
            //先检查是否能够删除
            $.ajax({
                url:"/post/admin/checkDeptDelete",
                data:"id="+deleteId,
                type:"get",
                success:function (result) {
                    if(result == "success"){
                        //进行删除
                        $.ajax({
                            url:"/dept/admin/"+deleteId,
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
                    }else{
                        alert("删除失败！该部门下还有职位存在！");
                        go_page(currentPagenumber);
                    }
                }
            })
        }
    })

    $(".edit_btn").click(function(){
        clearWaring2();
        $("#modifydeptname").val($(this).parent().prev().html());
        $(".modifyDeptBtn").attr("edit_id",$(this).attr("edit_id"));
        // 打开模态框
        $("#modifyDeptModal").modal({backdrop: "static"});
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


// 添加模态框弹出
$("#add_modal_btn").click(function () {
    // 清空表单数据
    $("#deptname").val("");
    clearWaring();
    // 弹出模态框
    $("#addDeptModal").modal({backdrop: "static"});
});


//添加部门
$(".saveDeptBtn").click(function () {
    var regName = /^[\u2E80-\u9FFF]{2,10}$/;
    if(!regName.test($("#deptname").val())){
        clearWaring();
        $("#deptname").parent().addClass("has-error");
        $("#deptname").next().append("部门名称必须是2-10位的中文");
        return false;
    }
    //先检查部门名字是否重复，不重复便可以保存
    $.ajax({
        url: "/dept/admin/"+$("#deptname").val(),
        type: "get",
        success:function (result){
            if(result.msg == "success"){
                clearWaring();
                $("#deptname").parent().addClass("has-success");
                //保存部门名称
                saveDept();
            }else{
                clearWaring();
                $("#deptname").parent().addClass("has-error");
                $("#deptname").next().append(result.msg);
            }
        }
    })
})

function clearWaring(){
    $("#deptname").parent().removeClass("has-success has-error");
    $("#deptname").next().empty();
}

function saveDept(){
    $.ajax({
        url: "/dept/admin",
        data: "name=" + $("#deptname").val(),
        type: "post",
        success:function (result){
            if(result == "success"){
                clearWaring();
                alert("保存成功！");
                // 关闭模态框
                $("#addDeptModal").modal('hide');
                go_page(lastPageNumber+1);
            }else{
                clearWaring();
                alert("保存失败！");
                // 关闭模态框
                $("#addDeptModal").modal('hide');
                go_page(lastPageNumber+1);
            }
        }
    })
}


//修改部门
$(".modifyDeptBtn").click(function () {
    var regName = /^[\u2E80-\u9FFF]{2,10}$/;
    if(!regName.test($("#modifydeptname").val())){
        clearWaring2();
        $("#modifydeptname").parent().addClass("has-error");
        $("#modifydeptname").next().append("部门名称必须是2-10位的中文");
        return false;
    }else clearWaring2();
    //先检查部门名字是否重复，不重复便可以保存
    $.ajax({
        url: "/dept/admin/"+$("#modifydeptname").val(),
        type: "get",
        success:function (result){
            if(result.msg == "success"){
                clearWaring();
                $("#modifydeptname").parent().addClass("has-success");
                //修改部门名称
                modifyDept();
            }else{
                clearWaring();
                $("#modifydeptname").parent().addClass("has-error");
                $("#modifydeptname").next().append(result.msg);
            }
        }
    })
})

function modifyDept() {
    $.ajax({
        url:"/dept/admin",
        data:"id="+$(".modifyDeptBtn").attr("edit_id")+"&name="+$("#modifydeptname").val(),
        type:"put",
        success:function (result) {
            if(result == "success"){
                clearWaring2();
                alert("修改成功！");
                // 关闭模态框
                $("#modifyDeptModal").modal('hide');
                go_page(currentPagenumber);
            }else{
                clearWaring2();
                alert("修改失败！");
                // 关闭模态框
                $("#modifyDeptModal").modal('hide');
                go_page(currentPagenumber);
            }
        }
    })
}

function clearWaring2(){
    $("#modifydeptname").parent().removeClass("has-success has-error");
    $("#modifydeptname").next().empty();
}

//批量删除部门功能
$("#delete_batch_btn").click(function () {
    //先检查是否有目标被选中
    if($(".check_item:checked").length == 0){
        alert("请选择要批量删除的对象！");
        return false;
    }
    var ids=new Array();
    var deptNames="";
    $(".check_item:checked").each(function (index,item) {
        deptNames+=$(this).parent().next().next().html()+",";
        ids[index]=$(this).val();
    })
    deptNames=deptNames.substr(0,deptNames.length-1);
    if(confirm("请确认要删除的部门名单如下："+deptNames)){
        $.ajax({
            url: "/post/admin/checkBatchDeptDelete",
            data: {ids: ids},
            type: "get",
            success: function (result) {
                if(result == "success"){
                    $.ajax({
                        url:"/dept/admin",
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
                }else if(result =="error"){
                    alert("有些部门还存在职位！");
                }
            }
        });

    }
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
