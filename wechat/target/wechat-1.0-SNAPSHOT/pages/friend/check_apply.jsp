<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/13
  Time: 20:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查看申请</title>
    <meta charset="utf-8">
    <base href="http://localhost:8080/wechat/">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>
<%--
    用一个表格来表示申请的信息
        前端：
            表格上显示username，还有申请msg的一部分，还有同意、拒绝、查看申请信息按钮
            同意:将表格中的信息删除，还有后台relation改为0,备注，设置黑名单
            拒绝：将后台改申请删除，
            查看信息：
--%>


<div class="layui-border-box">

    <i class="layui-icon layui-icon-form" style="font-size: 50px; color: #0000FF;"></i>
    <label class="label" style="font-size: 30px; color: #0000FF ">好友列表</label>
</div>

<br/>
<br/>

<table class="layui-table" id="check" lay-data="{height:315, url:'FriendServlet?action=showApply',method:'post', id:'apply' }" lay-filter="apply">
    <thead>
    <tr>
        <th lay-data="{field:'user_id', width:80}">用户名</th>
        <th lay-data="{field:'msg', width:160}">申请信息</th>
        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#agree'}"></th>
        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#refuse'}"></th>
<%--        <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#view'}"></th>--%>
    </tr>
    </thead>
</table>

<script type="text/html" id="agree">
    <a class="layui-btn layui-btn-xs" lay-event="agree">同意</a>
</script>

<script type="text/html" id="refuse">
    <a class="layui-btn layui-btn-xs" lay-event="refuse">拒绝</a>
</script>

<%--<script type="text/html" id="view">--%>
<%--    <a class="layui-btn layui-btn-xs" lay-event="detail">查看详情</a>--%>
<%--</script>--%>

<script src="static/layui/layui.js"></script>

<script>

    $(function () {
        layui.use(['table', 'layer'], function() {
            var table = layui.table;
            var layer = layui.layer;
            var user_id;

            table.on('tool(apply)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

                // txt = tr.values();
                // alert(txt);
                user_id = data.user_id;
                if (layEvent === 'detail') {


                } else if (layEvent === 'agree') {
                    //弹出窗口，输入对其的备注
                    layer.prompt({
                        formType: 2,
                        value: '',
                        title: '请输入对他的爱称',
                        area: ['235px', '70px'] //自定义文本域宽高
                    }, function (value, index, elem) {
                        alert(value); //得到value
                        //异步请求，修改数据库中的值
                        //将备注、获取到的行user_id传过去
                        $.ajax({
                            url: "FriendServlet",
                            data: "action=agreeApply&petName=" + value + "&user_id=" + user_id,
                            type: 'post',
                            dataType: 'test',
                            success: function (msg) {
                                if (msg == 1) {
                                    layer.msg("同意成功");
                                    reloadTable(table);
                                } else {
                                    layer.msg("同意失败");
                                }
                            }

                        })

                        layer.close(index);
                    });

                    //提交后写入数据库

                    //同意成功提示

                    //同意成功后当前行数据删除==刷新页面

                } else if (layEvent === 'refuse') {
                    //确认是否拒绝
                    layer.confirm('确认删除本条记录吗?', {
                        btn: ['是', '否'], btn1: function () {
                            $.ajax({
                                url: 'FriendServlet',
                                type: "POST",
                                data: {"action": "refuseApply","user_id" : user_id},
                                success: function () {
                                    layer.msg("拒绝成功");
                                    reloadTable(table);
                                }
                            })
                        },
                        btn2: function () {
                        }
                    })
                }
            })
        })
    })



    function reloadTable(table) {
        table.reload('apply',{
            elem: '#check' //指定原始表格元素选择器（推荐id选择器）
            ,height: 315 //容器高度
            ,url: `FriendServlet?action=showApply`,
            title:"好友申请列表"
        });
    }

</script>


</body>
</html>
