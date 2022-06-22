jQuery.ajaxSetup ({cache:false})
$(function() {
    //页面跳转功能
    $("#guideContent a").click(function () {
        //alert($(this).attr("name"));
        $("#adminContent").load($(this).attr("name"));
    })
});

