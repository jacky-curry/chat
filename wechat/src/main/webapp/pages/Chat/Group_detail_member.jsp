<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/23
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>群成员的群管理页面</title>
  <base href="http://localhost:8080/wechat/">
  <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>

<div class="layui-border-box">

  <i class="layui-icon layui-icon-search" style="font-size: 30px; color: #0000FF;"></i>
  <input class="layui-input-inline" name="searchInput" id="">
</div>

<table class="layui-table"  id="member" lay-data="{id: 'idTest'}"  lay-filter="test">
  <thead>
  <tr>
    <th lay-data="{field:'username', width:80}">群成员</th>
  </tr>
  </thead>
</table>
<button onclick="exitGroup()" type="button" class="layui-btn layui-btn-danger">退出群聊</button>

<script src="static/layui/layui.js"></script>
<script type="text/javascript">

  $(function (){
    layui.use(['table', 'layer'], function() {
      var table = layui.table;
      var layer = layui.layer;

      reloadList(table);

    })


  })

  function exitGroup() {
    layui.use(['layer','table'], function() {
      var layer = layui.layer;
      var table = layui.table;

      layer.confirm('是否退出该群', function(index){
        $.ajax({
          url: "chatServlet",
          type: "post",
          data: {
            action:"ExitGroup",
            group_id:$('.layui-input-inline').attr('id'),
            username:"${sessionScope.user.username}"
          },
          dataType: "text",
          success: function (data) {
            layer.msg("已经退出群聊");
            // reloadList(table);
          },
          error: function (){
            layer.msg("退出失败");
          }
        })


        layer.close(index);
        layer.close(index-1);
      });


    })
  }


  function reloadList(table){
    table.reload('idTest',{
      elem: '#member' //指定原始表格元素选择器（推荐id选择器）
      ,height: 315 //容器高度
      ,url: `chatServlet?action=GroupMember&group_id=`+$('.layui-input-inline').attr('id'),
      title:"群成员"
    });
  }
</script>

</body>
</html>
