/**
 * Created by chengjie.wang on 2017/12/29.
 */

$(function(){

    /**
     * 新增节点
     */
    $('#addNode').bind("click",function(){
        addNode();
    });

    /**
     * 刷新节点
     */
    $('#reflashNode').bind("click",function(){
        reflashNode();
    });

    /**
     * 新增（确认）
     */
    $('#confirm').bind("click",function(){
        confirm();
    });

    /**
     * 取消（确认）
     */
    $('#cancel').bind("click",function(){
        cancel();
    });
});

/**
 * 跳到编辑页面
 */
var addNode=function(){
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    var url = BASE_CONTEXT_PATH +"editClusterInfo";
    var hostaddress = window.location;
    window.location.href = hostaddress.protocol+"//"+hostaddress.hostname+":"+hostaddress.port+url;
};

/**
 * 确认
 */
var confirm=function(){
    var masterIp =$.trim($('#masterIp').val());
    var masterPort =$.trim($('#masterPort').val());
    var slaveIp =$.trim($('#slaveIp').val());
    var slavePort =$.trim($('#slavePort').val());
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    var url = BASE_CONTEXT_PATH +"addClusterInfo";
    var hostaddress = window.location;
    var path =  hostaddress.protocol+"//"+hostaddress.hostname+":"+hostaddress.port+url;
    var form = new FormData();
    form.append("masterIp",masterIp);
    form.append("masterPort",masterPort);
    form.append("slaveIp",slaveIp);
    form.append("slavePort",slavePort);
    $.ajax({
        url:path,
        type:"post",
        data:form,
        processData:false,
        contentType:false,
        success:function(data){
            console.log("over..");
        }
    });
};

/**
 * 取消，跳到查询页面
 */
var cancel=function(){
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    var url = BASE_CONTEXT_PATH +"getClusterInfo";
    var hostaddress = window.location;
    window.location.href = hostaddress.protocol+"//"+hostaddress.hostname+":"+hostaddress.port+url;
};
/**
 * 刷新节点
 */
var reflashNode =function(){
    var BASE_CONTEXT_PATH = $('meta[name=context-path]').attr("content");
    var url = BASE_CONTEXT_PATH +"getClusterInfo";
    var hostaddress = window.location;
    window.location.href = hostaddress.protocol+"//"+hostaddress.hostname+":"+hostaddress.port+url;
};