$("#loginInfo").submit(function (){
    $.ajax({
        url: "./login",
        data:$("#loginInfo").serialize(),
        type:"POST",
        success: function (result) {
            //result为后端返回结果信息
            //alert(result);
            if(result == 'admin'){
                window.location.href = "./Page/admin/AdminIndex";
            }else if(result == 'normal'){
                window.location.href = "./Page/normal/NormalIndex";
            }else if(result == 'passwordWrong'){
                alert("密码错误！");
            }else if(result == 'usernameWrong'){
                alert("账号错误！");
            }else{
                alert("未知错误！！！");
            }
        }
    });
    return false;
})
