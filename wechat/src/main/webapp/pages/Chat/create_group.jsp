<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/21
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="http://localhost:8080/wechat/">
    <title>创建群聊</title>
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>

<form id="create_group" class="layui-form" action="" onsubmit="return false"  lay-filter="create_group">
    <input type="hidden" name="action" value="">
    <input type="hidden" id="friend_id" name="friend_id" value="${sessionScope.user.username}">
    <div class="layui-form-item">
        <label class="layui-form-label">群名： </label>
        <div class="layui-input-inline">
            <input type="text" name="group_name" id="group_name" placeholder="请输入群名称"  class="layui-input">
        </div>
    </div>

    <table class="layui-table" id="group_member" lay-data="{id:'test'}" lay-filter="create">
        <thead>
        <tr>
            <th lay-data="{field:'username', width:80}">好友id</th>
            <th lay-data="{type:'checkbox'}">ID</th>
        </tr>
        </thead>
    </table>

    <div class="layui-form-item">
            <button type="submit" class="layui-btn" lay-filter="pass_sub">立即提交</button>
    </div>
</form>
<%--
1.将好友列表加载进来

2.表单提交时，判断哪些好友被选中了

3.提交后，进行数据库中，创建群聊，还有群和这些用户的联系

--%>



<%--<script type="text/html" id="barDemo">--%>
<%--    <label>--%>
<%--        <input class="layui-btn layui-btn-xs" type="checkbox" value="选中" lay-event="add" >--%>
<%--    </label>--%>
<%--</script>--%>
<script src="static/layui/layui.js"></script>
<%--
创建群聊天要做的事
    1.在数据库的group中创建该群聊

    2.在group_member中，创建群聊里的成员

--%>
<script>
    $(function () {
        layui.use(['table', 'layer','form'], function() {
            var table = layui.table;
            var layer = layui.layer;
            var form = layui.form;

            reloadList(table);
            table.on('checkbox(test)',function (obj) {
                var checkStatus = table.checkStatus('test');
                // console.log(checkStatus.data);
                // console.log(checkStatus.data.length);
            })
            form.on('submit(create_group)', function(data){
                var checkStatus = table.checkStatus('test');
                var owner = "${sessionScope.user.username}";
                var group_name = $("#group_name").val();
                var group_member = JSON.stringify(checkStatus.data);

                //获取群名还有群成员,还有群主，用ajax发送给后台，进行群组的创建
                if($("#group_name").val() != null){
                    layer.confirm('是否确定创建群聊', {icon: 1, title:'提示'}, function(index){
                        //do something
                        $.ajax({
                            url: "chatServlet",
                            type: "post",
                            data: {
                                action:"createGroup",
                                owner:owner,
                                name: group_name,
                                member:group_member
                            },
                            dataType: "json",
                            success: function (data) {
                                layer.msg("创建群组成功");
                                $("#group_name").val("");
                            }
                        })

                        layer.close(index);
                    });
                } else  {
                    layer.msg("群名不能为空");
                }

                return false;
            });
        });
    })



        function reloadList(table){
            table.reload('test',{
                elem: '#group_member' //指定原始表格元素选择器（推荐id选择器）
                ,height: 315 //容器高度
                ,url: `FriendServlet?action=getAllFriends&username=`+"${sessionScope.user.username}",
                title:"成员选择列表"
            });
        }


</script>



</body>
</html>
