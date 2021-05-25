<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/9
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>修改密码页面</title>
  <base href="http://localhost:8080/wechat/">
  <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css" media="all">

</head>
<script type="text/javascript" src="static/script/jquery-3.5.1.js"></script>
<body>

<script src="static/layui/layui.js"></script>
<script>





$(function () {



  layui.use(['form', 'layer'], function() {
    var form = layui.form;
    var layer = layui.layer;
    var index;

    //ajax请求获取当前用户的邮箱
    $.ajax({
      type : "post",
      url : "userServlet?action=getEmail&" + "username=" + "${sessionScope.user.username}", //此次url改为真正需要的url
      dateType:'text',

      success : function(msg) {
        $("#email").val(msg);
      },
      error : function () {
        layer.msg("读取邮箱失败");
      }
    });

  <%--  验证码获取按钮事件--%>
  var vCode = null;//用于保存邮箱验证码，在提交表单时，进行判断
  $("#getCode").click(function () {

    //email不为空的时候发送请求，否则提示填写邮箱
    if($("#email").val() != null){

      $.ajax({
        type : "post",
        url : "userServlet?action=changePassword_sendCode&email=" + $("#email").val(), //此次url改为真正需要的url
        dateType:'json',

        success : function(msg) {
            if(msg.result === -1) {
              layer.msg("发送验证码失败，请稍后再试");
            } else {
              console.log(msg);
              console.log(msg.vCode);
               vCode = msg.vCode;
               layer.msg("发送验证码成功，请查收");
            }
        },
        error : function () {
          layer.msg("发生错误");
        }
      });
    } else {
      layer.msg("邮箱不能为空，请填写");
    }
    console.log("验证码获取");
    // layer.msg("这里是按钮");
    // Ajax 请求 发送验证码 并保存到rsq中  并在前端验证
  })


    // form.on('submit(pass_sub)', function(data){
    //
    //
    //
    //     index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    //   parent.layer.close(index); //再执行关闭
    //   // return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    // });

    $("#pass_change").click(function () {
      // 验证密码：必须由字母，数字下划线组成，并且长度为5到12位
      //1 获取用户名输入框里的内容
      var passwordText = $("#password").val();
      //2 创建正则表达式对象
      var passwordPatt = /^\w{5,12}$/;
      //3 使用test方法验证
      if (!passwordPatt.test(passwordText)) {
        //4 提示用户结果
        layer.msg("密码不合法！");
        return false;
      }

      // 验证确认密码：和密码相同
      //1 获取确认密码内容
      var repwdText = $("#confirm_pass").val();
      //2 和密码相比较
      if (repwdText != passwordText) {
        //3 提示用户
        layer.msg("确认密码和密码不一致!");
        return false;
      }


      // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
      // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
      // console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
      //发送ajax请求，修改密码
      if(vCode == $("#code").val()){
        //验证码正确
        $.ajax({
          type : "post",
          url : "userServlet?action=changePassword&password=" + $("#password").val() +"&username=" + "${sessionScope.user.username}"
          , //此次url改为真正需要的url
          dateType:'json',

          success : function(msg) {
          },
          error : function () {
            layer.msg("修改密码失败");
          }
        });
      } else {
        layer.msg("验证码错误");
      }
    })

  });

})



</script>

<h3 style="color: #009688"  >修改密码</h3>

<form id="" class="layui-form" action="" onsubmit="return false" lay-filter="example">
  <div class="layui-form-item">
    <label class="layui-form-label">邮箱</label>
    <div class="layui-input-inline">
      <input type="text" name="email" id="email" lay-verify="email" class="layui-input">
    </div>
  </div>


  <div class="layui-form-item">
    <div class="layui-input-block">
      <button type="button" class="layui-btn layui-btn-primary" name="getCode" id="getCode">获取验证码</button>
    </div>
  </div>

  <div class="layui-form-item">
    <label class="layui-form-label">验证码</label>
    <div class="layui-input-inline">
      <input type="text" id="code" name="code" lay-verify="required" lay-reqtext="验证码，岂能为空？" placeholder="请输入" autocomplete="off" class="layui-input">
    </div>
  </div>




  <div class="layui-form-item">
    <label class="layui-form-label">密码</label>
    <div class="layui-input-inline">
      <input id="password" type="password" name="password" placeholder="请输入密码" lay-verify="required" lay-reqtext="密码不能为空" autocomplete="off" class="layui-input">
    </div>
  </div>

  <div class="layui-form-item">
    <label class="layui-form-label">确认密码</label>
    <div class="layui-input-inline">
      <input id="confirm_pass" type="password" name="com_password" placeholder="请再次输入密码" lay-verify="required" lay-reqtext="确认密码不能为空" autocomplete="off" class="layui-input">
    </div>
  </div>

  <div class="layui-form-item">
    <div class="layui-input-block">
      <button type="submit" id="pass_change" class="layui-btn" lay-filter="pass_sub">立即提交</button>
    </div>
  </div>

</form>




</body>
</html>
