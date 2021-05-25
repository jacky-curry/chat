<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/10
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主界面</title>
  <base href="http://localhost:8080/wechat/">
  <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-1.7.2.js"></script>

<body>
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
    <div class="layui-logo layui-hide-xs layui-bg-black">
      <i class="layui-icon layui-icon-login-wechat" style="font-size: 40px; color: #5FB878;"></i>
    </div>
    <!-- 头部区域（可配合layui 已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
      <!-- 移动端显示 -->
      <li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm" lay-header-event="menuLeft">
        <i class="layui-icon layui-icon-spread-left"></i>
      </li>

<%--      导航部分    --%>
      <li class="layui-nav-item layui-hide-xs" ><a href="pages/Chat/chat_page.jsp" target="myFrameName"><i class="layui-icon layui-icon-dialogue" style="font-size: 20px; color: #5FB878;"></i></a></li>
      <li class="layui-nav-item layui-hide-xs"><a href="pages/friend/friend.jsp" target="myFrameName"><i class="layui-icon layui-icon-user" style="font-size: 20px; color: #5FB878;"></i></a></li>
      <li class="layui-nav-item layui-hide-xs"><a href="" target="myFrameName"><i class="layui-icon layui-icon-find-fill" style="font-size: 20px; color: #5FB878;"></i></a></li>
    </ul>
<%--                               个人信息部分                               --%>
    <ul class="layui-nav layui-layout-right">
      <li class="layui-nav-item layui-hide layui-show-md-inline-block">
        <a href="javascript:;">
          <img src="//tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg" id="portrait" class="layui-nav-img">
          ${sessionScope.user.username}
        </a>
        <dl class="layui-nav-child">
<%--          <dd><a href="">个人信息</a></dd>--%>
          <dd><button data-method="setTop" class="layui-btn layui-btn-primary" id="info">个人信息</button></dd>
          <dd><a href="pages/user/change_password.jsp" target="_blank">修改密码</a></dd>
          <dd><a href="userServlet?action=logout">注销</a></dd>
        </dl>
      </li>
      <li class="layui-nav-item" lay-header-event="menuRight" lay-unselect>
        <a href="javascript:;">
          <i class="layui-icon layui-icon-more-vertical"></i>
        </a>
      </li>
    </ul>
  </div>

<%--  <div class="layui-side layui-bg-black">--%>
<%--    <div class="layui-side-scroll">--%>
<%--      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->--%>
<%--          <iframe id="side_frame" name="side_frame" class="layui-side"></iframe>--%>
<%--    </div>--%>
<%--  </div>--%>
  <div class="layui-body" style="width: 1360px "   >
    <!--                     内容主体区域                       -->
    <div style="">　
      　<iframe id="myFrameId" name="myFrameName" frameborder="0" marginheight="0" class="layui-fluid" style="top: 0px;left: 0px;padding: 0" width="1360" height="550"></iframe>
    </div>
  </div>


</div>

<script src="static/layui/layui.js"></script>
<script>
  //JS
  layui.use(['element', 'layer', 'util'], function(){
    var element = layui.element
            ,layer = layui.layer
            ,util = layui.util
            ,$ = layui.$;

    //头部事件
    util.event('lay-header-event', {
      //左侧菜单事件
      menuLeft: function(othis){
        layer.msg('展开左侧菜单的操作', {icon: 0});
      }
      ,menuRight: function(){
        layer.open({
          type: 1
          ,content: '<div style="padding: 15px;">处理右侧面板的操作</div>'
          ,area: ['260px', '100%']
          ,offset: 'rt' //右上角
          ,anim: 5
          ,shadeClose: true
        });
      }
    });
    element.on('nav(filter)', function(elem){
      console.log(elem); //得到当前点击的DOM对象
    });

  });

  layui.use('layer', function() { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

    //触发事件
  });

  $('#info').on('click', function(){
      layer.open({
        type: 2,
        content: 'pages/user/userInformation.jsp' ,
        area: ['500px', '600px']
      });
    });


$(function () {
    //加载头像
    $.ajax({
        type:"Post",
        url:"userServlet?username="+ "${sessionScope.user.username}",
        dataType:'text',
        success:function (res) {

        }
    })
})


</script>
</body>
</html>
