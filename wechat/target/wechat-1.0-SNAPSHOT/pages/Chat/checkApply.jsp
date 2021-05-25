<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/25
  Time: 0:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查看群申请</title>
  <base href="http://localhost:8080/wechat/">
  <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>
<table class="layui-table"  id="group" lay-data="{id: 'idTest'}"  lay-filter="test">
  <thead>
  <tr>
    <th lay-data="{field:'group_id', width:80}">群号</th>
    <th lay-data="{field:'username', width:160}">申请的人</th>

    <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}"></th>
            <th lay-data="{fixed: 'right', width:150, align:'center', toolbar: '#refuse'}"></th>
  </tr>
  </thead>
</table>

<script type="text/html" id="barDemo">
  <label>
    <input class="layui-btn layui-btn-xs" type="button" value="同意加入" lay-event="agree" >
  </label>
</script>

<script type="text/html" id="refuse">
  <label>
    <input class="layui-btn layui-btn-xs" type="button" value="拒绝加入" lay-event="refuse">
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


        if(layEvent === 'agree'){
          console.log(data.id);
          layer.confirm('是否同意  '+ data.username + "加入群聊", function(index){
            $.ajax({
              url: "chatServlet",
              type: "post",
              data: {
                action:"AgreeJoinIn",
                username:data.username,
                group_id:data.group_id
              },
              dataType: "text",
              success: function (data) {
                reloadList(table);
                layer.msg("群申请同意成功");
              },
              error: function (){
                layer.msg("群申请失败");
              }
            })
          });
        } else if(layEvent === 'refuse') {
          layer.confirm('是否拒绝  '+ data.username + "加入群聊", function(index){
            $.ajax({
              url: "chatServlet",
              type: "post",
              data: {
                action:"refuseGroupApply",
                username:data.username,
                group_id:data.group_id
              },
              dataType: "text",
              success: function (data) {
                reloadList(table);
                layer.msg("已经拒绝");
              },
              error: function (){
                layer.msg("拒绝失败");
              }
            })
          });

        }
      });



    })

  });

  function reloadList(table){
    table.reload('idTest',{
      elem: '#group' //指定原始表格元素选择器（推荐id选择器）
      ,height: 315 //容器高度
      ,url: `chatServlet?action=showGroupApply&username=`+"${sessionScope.user.username}",
      title:"申请列表"
    });
  }
</script>

</body>
</html>
