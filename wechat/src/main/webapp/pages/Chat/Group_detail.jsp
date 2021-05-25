<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/22
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改群名称和踢人，禁言</title>
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>

<div class="layui-border-box">

    <i class="layui-icon layui-icon-search" style="font-size: 30px; color: #0000FF;"></i>
    <input class="layui-input-inline" name="searchInput" id="">
    <button id="searchUsers" class="layui-btn layui-btn-radius layui-btn-normal">修改群名称</button>
</div>
<br/>
<br/>

<table class="layui-table"  id="member" lay-data="{id: 'idTest'}"  lay-filter="test">
    <thead>
    <tr>
        <th lay-data="{field:'username', width:80}">群成员</th>
        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}"></th>
        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#ban'}"></th>
    </tr>
    </thead>
</table>

<%--判断当前用户能否修改群名称，即当前群的onwer是否是当前登录的用户
    


--%>
<script type="text/html" id="barDemo">
    <label>
        <input class="layui-btn layui-btn-xs" type="button" value="踢出群聊" lay-event="out" >
    </label>
</script>
<script type="text/html" id="ban">
    <label>
        <input class=""  type="checkbox" value="禁言"  lay-event="banned" >
    </label>
</script>

<script src="static/layui/layui.js"></script>
<script type="text/javascript">
    $(function () {

        layui.use(['table', 'layer'], function() {
            var table = layui.table;
            var layer = layui.layer;

            reloadList(table);
            //按钮点击时，修改群名称
            $("#searchUsers").click(function () {
                $.ajax({
                    url: "chatServlet",
                    type: "post",
                    data: {
                        action:"changeGroupName",
                        group_id:$('.layui-input-inline').attr('id'),
                        group_name:$('.layui-input-inline').val()
                    },
                    dataType: "text",
                    success: function (data) {
                        layer.msg("修改群名称成功");
                    },
                    error: function (){
                        layer.msg("发生错误，修改失败");
                    }
                })

            })
            table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

                var member = data.username;

                if(layEvent === 'out'){
                    layer.confirm('是否踢出'+member+'成员', function(index){
                        $.ajax({
                            url: "chatServlet",
                            type: "post",
                            data: {
                                action:"GetOutOfGroup",
                                group_id:$('.layui-input-inline').attr('id'),
                                member:member
                            },
                            dataType: "text",
                            success: function (data) {
                                layer.msg("成员以起飞");
                                reloadList(table);
                            },
                            error: function (){
                                layer.msg("飞机票有误");
                            }
                        })
                        layer.close(index);
                    });
                }else if(layEvent === "banned"){

                }
            });
        })
        
        
    })

    function reloadList(table){
        table.reload('idTest',{
            elem: '#member' //指定原始表格元素选择器（推荐id选择器）
            ,height: 315 //容器高度
            ,url: `chatServlet?action=GroupMember&group_id=`+$('.layui-input-inline').attr('id'),
            title:"群成员"
        });
    }

    function checkLimits() {
        
    }


    
    
</script>


</body>

</html>
