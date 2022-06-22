jQuery.ajaxSetup ({cache:false})
//一开始先加载部门列表
$(function () {
    go_page(1);
})

function getDeptList(){
    $.ajax({
        url:"/dept",
        type:"get",
        success:function (result) {
            $.each(result.extend.deptList,function (index,item) {
                $("#staffOnline").append(
                    ' <div class="list-group"><a class="list-group-item active" data-toggle="collapse" href="#guideContent'+item.id+'">'+item.name+'</a>'+
                    '<div id="guideContent'+item.id+'" class="collapse in"></div></div>'
                );
            })
            sendLoginMessage();
        }
    })
}


//一开始登陆后与登录服务器建立 webSocket 连接
/**
 * 相关流程如下：
 * 一、建立连接
 * 二、发送自身相关信息
 * 三、由服务器接收登录信息后广播
 * 四、接收服务器广播的相关登录信息并更新在线人员
 * 五、实现挤别人下线功能
 * @type {null}
 */
var loginWebsocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    //loginWebsocket = new WebSocket("ws://192.168.3.8:8080/LoginWebsocket/"+$("#staffUsername").html());
    //loginWebsocket = new WebSocket("ws://192.168.137.1:8080/LoginWebsocket/"+$("#staffUsername").html());
    loginWebsocket = new WebSocket("ws://localhost:803/LoginWebsocket/"+$("#staffUsername").html());
}
else {
    alert('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
loginWebsocket.onerror = function () {
    alert("与登录服务器连接出错！");
    //登出
    window.location.href = "/login/unlogin";
};

//连接成功建立的回调方法
loginWebsocket.onopen = function () {
    getDeptList();
}

//接收到消息的回调方法
loginWebsocket.onmessage = function (event) {
    //alert(event.data);
    //对接到的关于登录的消息进行处理
    var loginMessage=$.parseJSON(event.data);
    if(loginMessage.state == "login"){
        //登录处理
        userLogin(loginMessage);
    }else if(loginMessage.state == "unlogin"){
        //登出处理
        $("input[name='username'][value = "+loginMessage.username+"]").next().remove()
        $("input[name='username'][value = "+loginMessage.username+"]").remove();
    }else if(loginMessage.state == "loginRepeat"){
        //重复登录处理
        alert("您的账号已在别处登录！");
        window.location.href = "/login/unlogin";
    }else if(loginMessage.state == "invite"){
        go_page(lastPageNumber+1);
        //受邀请的人进行页面跳转
        if(confirm(loginMessage.creater+"邀请您加入会议\n会议ID："+loginMessage.vcs_Id+"\n会议主题："+loginMessage.vcs_Title))
            window.location.href = "/Page/normal/VCSIndex?vcsId=" + loginMessage.vcs_Id;
    }
}

//连接关闭的回调方法
loginWebsocket.onclose = function () {

}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}

//关闭loginWebsocket连接
function closeWebSocket() {
    loginWebsocket.close();
}

//发送自身的登录消息
function sendLoginMessage() {
    var obj={};
    obj.username=$("#staffUsername").html();
    obj.staffName=$("#staffName").val();
    obj.staffPostName=$("#staffPostName option").html();
    obj.staffDid=$("#staffDeptName").val();
    obj.state="login";
    var message=JSON.stringify(obj);
    //alert(message);
    loginWebsocket.send(message);
}

function userLogin(result) {
    $.each(result.loginInfo,function (index,item) {
        $("#guideContent"+item.staffDid).append(
            '<input type="checkbox" name="username" value="'+item.username+'"/><span>'+item.staffPostName+' : '+item.staffName+'</br></span>'
        );
    });
}


/**
 * 以下开始是视频会议系统相关函数功能
 */
//按条件前往查看某一页的账号
function go_page(pageNo) {
    $.ajax({
        url:"/vcs/pageList",
        data:"pageNo=" + pageNo +"&"+ $("#vcs_search_form").serialize()+"&vcsState=1",
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
        $("#vcss_table tbody").append(
            '<tr><td>'+ item.vcsId + '</td>'+
            '<td>'+ item.vcsTitle + '</td>'+
            '<td>'+ item.creater + '</td>'+
            '<td><button class="btn btn-primary btn-sm join_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-edit"></span>加入会议</button>'+' '+
            '<button class="btn btn-danger btn-sm closeVCS_btn" vcsId="'+ item.vcsId +'"><span class="glyphicon glyphicon-remove-sign"></span>关闭会议</button>'+' '+
            '</tr>'
        );
    });
    //绑定加入会议按钮功能
    $(".join_btn").click(function () {
        window.location.href = "/Page/normal/VCSIndex?vcsId=" + $(this).attr("vcsId");
    })
    $(".closeVCS_btn").click(function () {
        if(confirm("确定要关闭该会议吗？")){
            if($(this).parent().prev().html().trim() == $("#staffUsername").html()){
                $.ajax({
                    url:"/vcs",
                    data:{username:$("#staffUsername").html(),vcsId:$(this).attr("vcsId")},
                    type:"put",
                    success:function (result) {
                        if(result == "success"){
                            alert("关闭会议成功！");
                            go_page(currentPagenumber);
                        }else {
                            alert("你可能无权限关闭会议，因为你不是创建者！");
                        }
                    }
                })
            }
            else alert("你可能无权限关闭会议，因为你不是创建者！");
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

/**
 * 创建会议相关功能
 * 流程为：创建者先邀请相关的人员，然后输入主题，接着便创建成功。创建成功后webSocket服务器会发起对会议参与者的邀请
 */
$("#create_VCS_btn").click(function () {
    //先检查选中了除了自己以外的会议参与者
    if($(":checkbox:checked").length == 0){
        alert("请选择参加会议的职员！");
        return false;
    }
    var flag=false;
    var invited=[];
    $(":checkbox:checked").each(function (index,item) {
        if($(this).val() == $("#staffUsername").html()){
            alert("请不要选择自己！");
            flag=true;
            return false;
        }
        var invite={};
        //将json对象放进数组中
        invite.vcsId=null;
        invite.username=$(this).val();
        invited.push(invite);
    })
    if(flag) return false;
    //开始创建会议
    //会议主题
    var vcs_Title= prompt("请输入会议主题(4-20个字)","视频会议");
    if(vcs_Title.length < 4 || vcs_Title.length > 20){
        alert("会议主题非法，创建会议失败！");
        return false;
    }
    //将会议信息存入数据库,包括视频会议和被邀请人信息
    var vcs={};
    vcs["invited"]=invited;
    vcs["creater"]=$("#staffUsername").html();
    vcs["vcsTitle"]=vcs_Title;
    $.ajax({
        url:"/vcs",
        data:JSON.stringify(vcs),
        type:"post",
        contentType : "application/json",
        success:function (result) {
            if(result.code == 100){
                //让webSocket发送邀请信息，邀请信息应当包含会议Id、主题、创建人
                vcs["vcs_Id"]=result.extend.vcs_Id;
                sendInviteMessage(vcs);
                go_page(lastPageNumber+1);
                //邀请人进入视频会议
                window.location.href = "/Page/normal/VCSIndex?vcsId=" + result.extend.vcs_Id;
            }
        }
    })


})

function sendInviteMessage(message){
    message["state"]="invite";
    loginWebsocket.send(JSON.stringify(message));
}

$("#userDetailInform").click(function () {
    // 弹出模态框
    $("#staffModal").modal({backdrop: "static"});
})

//修改用户信息功能
function clearWaring() {
    $("#staffAge").parent().removeClass("has-success has-error");
    $("#staffAge").next().empty();
    $("#staffPhone").parent().removeClass("has-success has-error");
    $("#staffPhone").next().empty();
}
//修改职员信息
$(".modifyStaffBtn").click(function () {
    clearWaring();
    //前端表单校验
    var regBlank = /^\s*$/;
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
        url:"/staff",
        data:"name="+$("#staffName").val()+"&sex="+$("#staffSex").val()+"&id="+$(".modifyStaffBtn").attr("edit_id")+"&"+$("#staffModal form").serialize(),
        type:"put",
        success:function (result) {
            if(result == "success"){
                clearWaring();
                // 关闭模态框
                $("#staffModal").modal('hide');
                alert("修改成功！");
                go_page(currentPagenumber);
            }else if(result == "nameNull"){
                clearWaring();
                $("#staffName").parent().addClass("has-error");
                $("#staffName").next().append("请输入姓名！");
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
                $("#staffModal").modal('hide');
                alert("修改失败！")
            }
        }
    })
})
//修改用户密码功能
$("#modifyUserPassword").click(function () {
    // 弹出模态框
    $("#modifyAccountModal").modal({backdrop: "static"});
})

function clearWaring() {
    $("#oldPassword").parent().removeClass("has-success has-error");
    $("#oldPassword").next().empty();
    $("#modifyPassword").parent().removeClass("has-success has-error");
    $("#modifyPassword").next().empty();
    $("#modifyRePassword").parent().removeClass("has-success has-error");
    $("#modifyRePassword").next().empty();
}

$(".modifyAccountBtn").click(function () {
    //校验输入非空，合法
    var regBlank = /^\s*$/;
    if($("#oldPassword").val() == null || regBlank.test($("#oldPassword").val())){
        $("#oldPassword").parent().addClass("has-error");
        $("#oldPassword").next().append("请输入密码！");
        return false;
    }
    if($("#modifyPassword").val() == null || regBlank.test($("#modifyPassword").val())){
        $("#modifyPassword").parent().addClass("has-error");
        $("#modifyPassword").next().append("请输入新密码！");
        return false;
    }
    if($("#modifyRePassword").val() == null || regBlank.test($("#modifyRePassword").val())){
        $("#modifyRePassword").parent().addClass("has-error");
        $("#modifyRePassword").next().append("请再次输入新密码！");
        return false;
    }
    if($("#modifyRePassword").val() != $("#modifyPassword").val()){
        $("#modifyRePassword").parent().addClass("has-error");
        $("#modifyRePassword").next().append("两次密码请一致！");
        return false;
    }
    //先检查原密码是否正确
    $.ajax({
        url:"/account/checkPassword",
        data:"username="+$("#staffUsername").html()+"&password="+$("#oldPassword").val(),
        type:"post",
        success:function (result) {
            if(result == "success"){
                $.ajax({
                    url:"/account",
                    data:"rePassword="+$("#modifyRePassword").val()+"&"+"password="+$("#modifyPassword").val()+"&username="+$("#staffUsername").html(),
                    type:"put",
                    success:function (result) {
                        if(result == "success"){
                            clearWaring();
                            // 关闭模态框
                            $("#modifyAccountModal").modal('hide');
                            alert("保存成功！");
                            go_page(lastPageNumber+1);
                        }else if(result == "passwordNull"){
                            $("#modifyPassword").parent().addClass("has-error");
                            $("#modifyPassword").next().append("请输入密码！");
                        }else if(result == "passwordWrong"){
                            $("#modifyRePassword").parent().addClass("has-error");
                            $("#modifyRePassword").next().append("两次密码请一致！");
                        }else{
                            clearWaring();
                            // 关闭模态框
                            $("#modifyAccountModal").modal('hide');
                            alert("保存失败！");
                        }
                    }
                })
            }else{
                $("#oldPassword").parent().addClass("has-error");
                $("#oldPassword").next().append("原密码错误！");
            }
        }
    })
})

