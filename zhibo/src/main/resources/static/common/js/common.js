//获取绝对路径
function getRootPath(){

    //获取当前网址，如： http://localhost:8080/itoo-jrkj-evaluate-web/index.jsp

    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： itoo-jrkj-evaluate-web/index.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht);
}


//把表单元素序列化为对象的方法
function serializeObject(form) {
    var o = {};
    $.each(form.serializeArray(),
    function(index) {
        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + ',' + this['value'];
        } else {
            o[this['name']] = this['value'];
        }

    });
    return o;
}