<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/11
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加好友和建群页面</title>
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-1.7.2.js"></script>
<%--1：自己定义一个单元格格式，用来展示用户信息，还有跳转--%>
<%--
2:建立页面基本模块
    -1.添加好友：
        1.1-弹出窗口，能根据name和username进行模糊搜索
        1.2-用户信息可以用一个表格展示
        1.3-点击添加好友：
            -将发送好友添加请求

    -2:好友申请:
        2.1-从数据库的friend中读取当前已登录用户的申请
        2.2-点击同意     点击拒绝
            同意：修改数据库中friend的relation为0，添加好友成功
            拒绝：

    -3:创建群聊:


3.通过读取数据库中的好友信息动态创建好友列表
    -读取数据库中的user的name、头像
    -用javascript创建单元格
    -给这个单元格绑定上单击事件：点击时，在主内容中展示聊天区域，


--%>

<%--
展示群信息，包括群name，id（隐藏）,群成员,


--%>


<body>
<div class="layui-layout layui-layout-admin">
    <div class="layui-side" style= "top:0px"  id="side">
<%--        <div class="layui-border-box" id="unit" style="">--%>
<%--            <img src="static/img/default.jpg" width="50" height="50">--%>
<%--            <label class="layui-font-black">名字</label>--%>
<%--        </div>--%>

        <div class="layui-border-box" id="addFriend" style="">
            <button data-method="addFriend" class="layui-btn" style="width: 200px">
                <a href="pages/friend/add_friend.jsp" target="childFrame">
                    <i class="layui-icon layui-icon-friends" style="font-size: 30px; color: dodgerblue;"></i>
                    <label class="layui-font-black layui-font-14" style="align-content: center" >添加好友</label>
                </a>
            </button>

        </div>

        <div class="layui-border-box" id="fri_request" style="">
            <button data-method="addFriend" class="layui-btn" style="width: 200px">
                <a href="pages/friend/check_apply.jsp" target="childFrame">
                <i class="layui-icon layui-icon-list" style="font-size: 30px; color:dodgerblue ;"></i>
                <label class="layui-font-black layui-font-14" style="align-content: center" >好友申请</label>
                </a>
            </button>
        </div>

        <div class="layui-border-box" id="create_group" style="">
            <button data-method="create_group" class="layui-btn" style="width: 200px">
                <a href="pages/Chat/create_group.jsp" target="childFrame">
                <i class="layui-icon layui-icon-group" style="font-size: 30px; color: dodgerblue;"></i>
                <label class="layui-font-black layui-font-14" style="align-content: center">创建群聊</label>
                </a>
            </button>
        </div>

    <div class="layui-border-box" id="create_group" style="">
        <button data-method="create_group" class="layui-btn" style="width: 200px">
            <a href="pages/Chat/JoinGroup.jsp" target="childFrame">
                <i class="layui-icon layui-icon-group" style="font-size: 30px; color: dodgerblue;"></i>
                <label class="layui-font-black layui-font-14" style="align-content: center">查找群聊</label>
            </a>
        </button>
    </div>

    <div class="layui-border-box" id="create_group" style="">
        <button data-method="create_group" class="layui-btn" style="width: 200px">
            <a href="pages/Chat/checkApply.jsp" target="childFrame">
                <i class="layui-icon layui-icon-group" style="font-size: 30px; color: dodgerblue;"></i>
                <label class="layui-font-black layui-font-14" style="align-content: center">查看群聊申请</label>
            </a>
        </button>
    </div>

    <div class="layui-border-box" id="show_group" style="">

    </div>

<%--        好友列表
    1.根据后台好友的内容，加载列表中

    2.

--%>

    </div>


    <div class="layui-body" style="left: 200px;top: 0px" >
        <!--                     内容主体区域                       -->
            　<iframe id="myFrameId" name="childFrame" frameborder="0" class="" style="width: 1150px; height: 535px" ></iframe>
    </div>
</div>

    <script src="static/layui/layui.js"></script>
    <script>
<%--
-1.添加好友：
        1.1-弹出窗口，能根据name和username进行模糊搜索
        1.2-用户信息可以用一个表格展示
        1.3-点击添加好友：
            -将发送好友添加请求
--%>
//获取子页面传入的数据
    function GetChildValue(obj){
        console.log(obj)
        console.log("接受到子页面的数据")
    }
//给子页面传入数据
function toChildValue(){
    var txt = '这是父页面给子页面的数据';
    return txt;
}




    </script>




<%--    显示好友列表
    1.页面加载后，发送异步请求，获取好友数据u
    2.打包展示数据，用循环
    3.


  --%>
<script>


    
    



    function pOnclick(){


    }
    
</script>

</body>
</html>
