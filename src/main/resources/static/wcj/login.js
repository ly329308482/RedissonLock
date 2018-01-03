/**
 * Created by chengjie.wang on 2017/12/29.
 */
$(function(){

    /**
     * 回车登录
     */
    document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){
            $('#login').click();
        }
    };

    /**
     * 登录
     */
    $('#login').bind("click",function(){
        doLogin();
    });

});

/**
 * 登录
 */
var doLogin=function(){
    var username =$.trim($('.username').val());
    var password =$.trim($('.password').val());
    if(username == '') {
        $('.error').fadeOut('fast', function(){
            $('.error').css('top', '27px').show();
        });
        $('.error').fadeIn('fast', function(){
            $('.username').focus();
        });
        return false;
    }
    if(password == '') {
        $('.error').fadeOut('fast', function(){
            $('.error').css('top', '96px').show();
        });
        $(this).find('.error').fadeIn('fast', function(){
            $('.password').focus();
        });
        return false;
    }
    var  data = {password:password,username:username};
    // var load = layer.load();
    $.ajax({
        url:base+"submitLogin",
        type: 'POST',
        data: data,
        dataType: 'JSON',
        success: function(result){
            // layer.close(load);
            if(result && result.status != 200){
                layer.msg(result.message,function(){});
                $('.password').val('');
                return;
            }else{
                // layer.msg('登录成功！');
                setTimeout(function(){
                    //登录返回
                    window.location.href= result.back_url || "${basePath}/";
                },1000)
            }
        }
    });
};