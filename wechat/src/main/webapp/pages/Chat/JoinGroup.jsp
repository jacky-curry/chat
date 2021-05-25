<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/24
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查找并加入群聊</title>
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>

<div class="layui-border-box">

    <i class="layui-icon layui-icon-search" style="font-size: 30px; color: #0000FF;"></i>
    <input class="layui-input-inline" name="searchInput" id="group_search">
    <button id="searchUsers" class="layui-btn layui-btn-radius layui-btn-normal">搜索群聊</button>
</div>
<br/>
<br/>

<table class="layui-table"  id="group" lay-data="{id: 'idTest'}"  lay-filter="test">
    <thead>
    <tr>
        <th lay-data="{field:'id', width:80}">群号</th>
        <th lay-data="{field:'name', width:160}">群名称</th>
        <th lay-data="{field:'owner', width:80}">群主</th>

        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}"></th>
<%--        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#ban'}"></th>--%>
    </tr>
    </thead>
</table>

<script type="text/html" id="barDemo">
    <label>
        <input class="layui-btn layui-btn-xs" type="button" value="加入群聊" lay-event="join" >
    </label>
</script>


<script src="static/layui/layui.js"></script>
<script type="text/javascript">
    $(function () {
        layui.use(['table', 'layer'], function() {
            var table = layui.table;
            var layer = layui.layer;

            reloadList(table);

            table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）


                if(layEvent === 'join'){
                    console.log(data.id);
                    layer.confirm('是否加入群： '+ data.name, function(index){
                        $.ajax({
                            url: "chatServlet",
                            type: "post",
                            data: {
                                action:"JoinInGroup",
                                group_id:data.id,
                                member:"${sessionScope.user.username}"
                            },
                            dataType: "text",
                            success: function (data) {
                                layer.msg("发送申请成功")
                            },
                            error: function (){
                                layer.msg("加入群失败，用户已经在群里");
                            }
                        })
                    });
                }
            });

            $("#searchUsers").click(function () {
                if($("#group_search").val() != null){
                    console.log("搜索的值为"+$("#group_search").val());
                    SearchList(table);
                } else {
                    layer.msg("群名或群id不能为空");
                }
            })

        })



    });

    function reloadList(table){
        table.reload('idTest',{
            elem: '#group' //指定原始表格元素选择器（推荐id选择器）
            ,height: 315 //容器高度
            ,url: `chatServlet?action=JoinGroupList&username=` + "${sessionScope.user.username}" ,
            title:"群聊列表"
        });
    }

    function SearchList(table){
        table.reload('idTest',{
            elem: '#group' //指定原始表格元素选择器（推荐id选择器）
            ,height: 315 //容器高度
            ,url: `chatServlet?action=JoinGroupList&username=` + "${sessionScope.user.username}"  + "&search=" + $("#group_search").val(),
            title:"群聊列表"
        });
    }


</script>


</body>


</html>
