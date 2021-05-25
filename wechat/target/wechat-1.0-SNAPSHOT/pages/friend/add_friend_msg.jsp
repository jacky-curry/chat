<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/12
  Time: 20:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加好友时，弹出输入备注、申请信息</title>
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">


</head>

<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>

<body>

<form id="pass_change" class="layui-form" action="FriendServlet" lay-filter="example">
    <input type="hidden" name="action" value="addFriends">
    <input type="hidden" id="friend_id" name="friend_id" value="">
    <div class="layui-form-item">
        <label class="layui-form-label">备注:</label>
        <div class="layui-input-inline">
            <input type="text" name="petName" id="petName" placeholder="请输入爱称"  class="layui-input">
        </div>
    </div>


    <div class="layui-form-item">
        <label class="layui-form-label">申请信息:</label>
        <div class="layui-input-inline">
            <input type="text" name="msg"  placeholder="请输入申请信息" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button type="submit" class="layui-btn" lay-filter="pass_sub">发送申请</button>
        </div>
    </div>
</form>

<script src="static/layui/layui.js"></script>
<script>

    $(function () {
        //获取父页面传来的数据
        var getParentVule = window.parent.toChildValue();
        console.log(getParentVule);

        $("#friend_id").val(getParentVule);


    })
    layui.use(['form', 'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;
        var index;
        form.on('submit(pass_sub)', function(data){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            // console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
            index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
            // return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

    });





</script>

</body>
</html>
