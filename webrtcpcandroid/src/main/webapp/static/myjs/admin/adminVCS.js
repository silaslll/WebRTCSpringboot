jQuery.ajaxSetup ({cache:false})
var lastPageNumber;
var currentPagenumber;
$(function () {
    go_page(1);
})
function go_page(pageNo) {j
    $.ajax({
        url:"/vcs/pageList",
        data:"pageNo=" + pageNo +"&"+ $("#vcs_search_form").serialize(),
        type:"get",
        success:function (result) {
            //alert(result);
            //首先构建视频会议信息表
            build_vcs_table(result);
            //创建分页信息导航栏
            build_page_info(result);
        }
    })
}
//构建职位信息表
function build_vcs_table(result){
    // 清空表格内容
    $("#vcss_table tbody").empty();
    var vcss = result.extend.pageInfo.list;
    $.each(vcss, function (index, item) {
        var str= '<tr><td><input type="checkbox" class="check_item" value="'+item.vcsId+'"/> </td>'+
            '<td>'+ item.vcsId + '</td>'+
            '<td>'+ item.vcsTitle + '</td>'+
            '<td>'+ item.creater + '</td>';
        if(item.vcsState == 1){
            str +='<td>进行中</td>'+
                '<td><button class="btn btn-primary btn-sm details_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-edit"></span>参会者</button>'+' '+
                '<button class="btn btn-danger btn-sm deleteVCS_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-trash"></span>删除</button>'+' '+
                '</tr>'
        }else{
            str +='<td>结束</td>'+
                '<td><button class="btn btn-primary btn-sm details_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-edit"></span>参会者</button>'+' '+
                '<button class="btn btn-danger btn-sm deleteVCS_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-trash"></span>删除</button>'+' '+
                '</tr>'
        }
        $("#vcss_table tbody").append(str);
    });


    //绑定查看会议详情按钮功能
    $(".details_btn").click(function () {
        $("#joinerModal").modal({backdrop: "static"});
        $("#joins_table tbody").empty();
        $.ajax({
            url:"/vcs/"+$(this).attr("vcsId"),
            type:"get",
            success:function (result) {
                var joins = result.extend.temps;
                $.each(joins,function (index,item) {
                    $("#joins_table tbody").append(
                        '<tr><td>'+item.username+'</td></tr>'
                    )
                })
            }
        })
    })
    
    $(".deleteVCS_btn").click(function () {
        //先检查会议是否进行中
        if($(this).parent().prev().html() == "进行中"){
            alert("会议进行中，不能删除！")
            return false;
        }
        if(confirm("确定要删除该会议吗？")){
            $.ajax({
                url:"/vcs/admin/"+$(this).attr("vcsId"),
                type:"delete",
                success:function (result) {
                    if(result == "success"){
                        alert("删除成功！");
                        go_page(currentPagenumber);
                    }else{
                        alert("删除失败......");
                    }
                }
            })
        }
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
$("#vcs_search_btn").click(function () {
    go_page(1);
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


//批量删除会议功能
$("#delete_batch_btn").click(function () {
    //先检查是否有目标被选中
    if($(".check_item:checked").length == 0){
        alert("请选择要批量删除的对象！");
        return false;
    }
    var ids=new Array();
    var VCSTitles="";
    $(".check_item:checked").each(function (index,item) {
        VCSTitles+=$(this).parent().next().next().html()+",";
        ids[index]=$(this).val();
    })
    VCSTitles=VCSTitles.substr(0,VCSTitles.length-1);
    if(confirm("请确认要删除的会议如下："+VCSTitles)){
        $.ajax({
            url: "/vcs/admin/checkBatchVCSDelete",
            data: {ids: ids},
            type: "get",
            success: function (result) {
                if(result == "success"){
                    $.ajax({
                        url:"/vcs/admin",
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
                    alert("有些会议还在进行！");
                }
            }
        });

    }
})



