jQuery.ajaxSetup ({cache:false})
var lastPageNumber;
var currentPagenumber;
$(function () {
    getDeptList()
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
        }
    })
}

//按条件前往查看某一页的职位信息
function go_page(pageNo) {
    var pathParm2=$("#post_select_form :input[name='dId']").val();
    var pathParm3=$("#post_select_form :input[name='name']").val();
    if(pathParm2 == null || pathParm2 == ""){
        pathParm2 = 0;
    }
    if(pathParm3 == null || pathParm3 == ""){
        pathParm3 = 0;
    }
    $.ajax({
        url:"/post/"+pageNo+"/"+pathParm2+"/"+pathParm3,
        //data:$("#post_select_form").serialize(),
        type:"get",
        success:function (result) {
            //alert(result);
            //首先构建职位信息表
            build_post_table(result);
            //创建分页信息导航栏
            build_page_info(result);
        }
    })
}
//构建职位信息表
function build_post_table(result){
    // 清空表格内容
    $("#posts_table tbody").empty();
    var posts = result.extend.pageInfo.list;
    $.each(posts, function (index, item) {
        $("#posts_table tbody").append(
            '<tr><td><input type="checkbox" class="check_item" value="'+item.id+'"/> </td>'+
            '<td>'+ item.id + '</td>'+
            '<td>'+ item.name + '</td>'+
            '<td>'+ item.dept.name + '</td>'+
            '<td><button class="btn btn-primary btn-sm edit_btn" edit_id="'+ item.id +'" dept_id="'+ item.dId +'"><span class="glyphicon glyphicon-edit"></span>编辑</button>'+' '+
            '<button class="btn btn-danger btn-sm delete_btn" delete_id="'+ item.id +'"><span class="glyphicon glyphicon-trash"></span>删除</button></td>'+
            '</tr>'
        );
    });
    //绑定编辑信息与删除信息按钮功能
    $(".delete_btn").click(function () {
        var delete_id=$(this).attr("delete_id");
        if(confirm("确认删除职位名称："+$(this).parent().prev().prev().html())){
            $.ajax({
                url:"/staff/admin/checkDeleteStaff",
                data:"id="+delete_id,
                type:"get",
                success:function (result) {
                    if(result == "success"){
                        $.ajax({
                            url:"/post/admin/"+delete_id,
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
                        alert("该职位还有职员担任！");
                    }
                }
            })
        }
    })

    $(".edit_btn").click(function(){
        //clearWaring2();
        $("#modifypostname").val($(this).parent().prev().prev().html());
        $("#modifyPostModal select").val($(this).attr("dept_id"));
        $(".modifyPostBtn").attr("edit_id",$(this).attr("edit_id"));
        // 打开模态框
        $("#modifyPostModal").modal({backdrop: "static"});
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
    $("#postname").val("");
    $("#addPostModal select").val("0");
    // 弹出模态框
    $("#addPostModal").modal({backdrop: "static"});
});

function clearWaring(){
    $("#postname").parent().removeClass("has-success has-error");
    $("#postname").next().empty();
    $("#postDeptName").parent().removeClass("has-success has-error");
    $("#postDeptName").next().empty();
}

//添加职位
$(".savePostBtn").click(function () {
    clearWaring();
    var regName = /^[\u2E80-\u9FFF]{2,10}$/;
    if(!regName.test($("#postname").val())){
        $("#postname").parent().addClass("has-error");
        $("#postname").next().append("职位名称必须是2-10位的中文");
        return false;
    }
    if($("#postDeptName").val() == 0){
        $("#postDeptName").parent().addClass("has-error");
        $("#postDeptName").next().append("请选择具体部门");
        return false;
    }
    //后端检查，主要检查是否重复
    $.ajax({
        url: "/post/admin/checkPost",
        data: $("#addPostModal form").serialize(),
        type: "post",
        success:function (result){
            if(result == "success"){
                clearWaring();
                $("#postname").parent().addClass("has-success");
                $("#postDeptName").parent().addClass("has-success");
                //保存职位名称
                savePost();
            }else{
                clearWaring();
                if(result ="nameNull"){
                    $("#postname").parent().addClass("has-error");
                    $("#postname").next().append("请填入职位名称");
                }else if(result ="dIdNull"){
                    $("#postDeptName").parent().addClass("has-error");
                    $("#postDeptName").next().append("请选择具体部门");
                }else if(result = "nameFormatError"){
                    $("#postname").parent().addClass("has-error");
                    $("#postname").next().append("职位名称必须是2-10位的中文");
                }else if(result ="repeat"){
                    $("#postname").parent().addClass("has-error");
                    $("#postname").next().append("职位名称在所在部门重复");
                }
            }
        }
    })
})

function savePost() {
    $.ajax({
        url:"/post/admin",
        data:$("#addPostModal form").serialize(),
        type:"post",
        success:function (result) {
            if(result == "success"){
                clearWaring();
                alert("保存成功！");
                // 关闭模态框
                $("#addPostModal").modal('hide');
                go_page(lastPageNumber+1);
            }else{
                clearWaring();
                alert("保存失败！");
                // 关闭模态框
                $("#addPostModal").modal('hide');
                go_page(lastPageNumber+1);
            }
        }
        
    })
}

function clearWaring2(){
    $("#modifypostname").parent().removeClass("has-success has-error");
    $("#modifypostname").next().empty();
    $("#modifypostDeptName").parent().removeClass("has-success has-error");
    $("#modifypostDeptName").next().empty();
}

//修改职位
$(".modifyPostBtn").click(function () {
    clearWaring2();
    var regName = /^[\u2E80-\u9FFF]{2,10}$/;
    if(!regName.test($("#modifypostname").val())){
        $("#modifypostname").parent().addClass("has-error");
        $("#modifypostname").next().append("职位名称必须是2-10位的中文");
        return false;
    }
    if($("#modifypostDeptName").val() == 0){
        $("#modifypostDeptName").parent().addClass("has-error");
        $("#modifypostDeptName").next().append("请选择具体部门");
        return false;
    }
    //后端检查，主要检查是否重复
    $.ajax({
        url: "/post/admin/checkPost",
        data: $("#modifyPostModal form").serialize(),
        type: "post",
        success:function (result){
            if(result == "success"){
                clearWaring2();
                $("#modifypostname").parent().addClass("has-success");
                $("#modifypostDeptName").parent().addClass("has-success");
                //保存职位名称
                updatePost();
            }else{
                clearWaring2();
                if(result ="nameNull"){
                    $("#modifypostname").parent().addClass("has-error");
                    $("#modifypostname").next().append("请填入职位名称");
                }else if(result ="dIdNull"){
                    $("#modifypostDeptName").parent().addClass("has-error");
                    $("#modifypostDeptName").next().append("请选择具体部门");
                }else if(result = "nameFormatError"){
                    $("#modifypostname").parent().addClass("has-error");
                    $("#modifypostname").next().append("职位名称必须是2-10位的中文");
                }else if(result ="repeat"){
                    $("#modifypostname").parent().addClass("has-error");
                    $("#modifypostname").next().append("职位名称在所在部门重复");
                }
            }
        }
    })
})

function updatePost() {
    $.ajax({
        url:"/post/admin",
        data:"id="+$(".modifyPostBtn").attr("edit_id")+"&"+$("#modifyPostModal form").serialize(),
        type:"put",
        success:function (result) {
            if(result == "success"){
                clearWaring();
                alert("保存成功！");
                // 关闭模态框
                $("#modifyPostModal").modal('hide');
                go_page(currentPagenumber);
            }else{
                clearWaring();
                alert("保存失败！");
                // 关闭模态框
                $("#modifyPostModal").modal('hide');
                go_page(currentPagenumber);
            }
        }

    })
}

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
    var ids=new Array();
    var postNames="";
    $(".check_item:checked").each(function (index,item) {
        postNames+=$(this).parent().next().next().html()+":"+$(this).parent().next().next().next().html()+",";
        ids[index]=$(this).val();
    })
    postNames=postNames.substr(0,postNames.length-1);
    if(confirm("请确认要删除的部门职位如下："+postNames)){
        $.ajax({
            url:"/staff/admin/checkBatchDeleteStaff",
            data:{ids:ids},
            type:"get",
            success:function (result) {
                if(result == "success"){
                    $.ajax({
                        url:"/post/admin",
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
                }else{
                    alert("某职位还有职员担任！");
                }
            }
        })
    }
})