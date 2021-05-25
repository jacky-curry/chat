<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/11
  Time: 23:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加好友</title>
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<%--        1.1-弹出窗口，能根据name和username进行模糊搜索
                   在搜索的时候，表格里的内容实时更新
        1.2-用户信息可以用一个表格展示

        1.3-点击添加好友：
            -前端输入对其好友的备注，和申请信息
            -获取所选行的用户名：username，当前的用户username

            -将发送请求给后台

--%>
<body>
<%--1.先写好表格和搜索框--%>
<div class="layui-border-box">

    <i class="layui-icon layui-icon-search" style="font-size: 30px; color: #0000FF;"></i>
    <input class="layui-input-inline" name="searchInput" id="searchInput">
    <button id="searchUsers" class="layui-btn layui-btn-radius layui-btn-normal">搜索</button>
</div>
<br/>
<br/>

<table class="layui-table" lay-data="{height:315, url:'FriendServlet?action=showAllUsers', id:'test'}" lay-filter="test">
    <thead>
    <tr>
        <th lay-data="{field:'username', width:150}">用户名</th>
        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}"></th>
    </tr>
    </thead>
</table>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="detail">添加好友</a>
</script>

<script src="static/layui/layui.js"></script>
<script>

    layui.use(['table', 'layer'], function() {
        var table = layui.table;
        var layer = layui.layer;


        //按钮点击时，重载表中的数据
        $("#searchUsers").click(function () {
            //获取前端input框内的用户名
            /*
                判断是否为空
                    为空：提示不能为空
                    不为空：将用户名传到后台
                        获取数据并显示
             */
            if($("#searchInput").val()){
                table.reload('test', {
                    url: 'FriendServlet?action=searchUsers'+'&&'+"searchName=" + $("#searchInput").val()
                    ,where: {} //设定异步数据接口的额外参数
                    //,height: 300
                });
            } else {
                layer.msg("搜索内容为空");
            }

        })


        table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
            txt = data.username;
            if(layEvent === 'detail'){ //添加好友
                //do somehing
                // console.log(data);//当前行的friendname

                //当前登录的用户
<%--                ${sessionScope.user.username}  --%>
                //将当前的friendname传到另一个页面中
                // console.log(txt);
                // console.log(tr);
                layer.open({
                    type: 2,

                    content : ['pages/friend/add_friend_msg.jsp','no'],

                    area :["540px","270px"],
                    success: function (layer, index) {
                        // var iframe = window['layui-layer-iframe' + index];//拿到iframe元素
                        // iframe.child(JSON.stringify(data))//向此iframe层方法 传递参数
                    }


                })
            }
        });


    });

    //给父页面传数据
    // var txt = '我是子界面主动传值给父界面的数值';
    // parent.GetChildValue(txt); //GetValue是父界面的Js 方法




    var txt;
    function toChildValue(){
        return txt;
    }


</script>
<script src="static/layui/layui.js"></script>

</body>
</html>
