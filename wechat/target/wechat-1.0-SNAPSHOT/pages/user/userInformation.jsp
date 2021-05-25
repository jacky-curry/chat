<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/8
  Time: 9:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息</title>
    <meta charset="UTF-8">
    <base href="http://localhost:8080/wechat/">
    <link type="text/css" rel="stylesheet" href="http://localhost:8080/wechat/static/css/style.css" >
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script type="text/javascript" src="static/script/jquery-1.7.2.js"></script>
<script src="static/layui/layui.js"></script>
<script type="text/javascript">
    $(function () {
        var fileObj = "";
        var imgData = "";

        $.ajax({
            type:"GET",
            url:"PortraitServlet?username="+ "${sessionScope.user.username}",
            dataType:'json',
            contentType:"application/json;charset='UTF-8'",
            success:function (res) {
                // alert(res.username);
                $("#username").val(res.username);
                $("#petname").val(res.name);
                $("#email").val(res.email);

            }
        })


        $("#myFile").change(function () {
            // alert("这里时文件上传")
            // 构造一个文件渲染对象
            var reader = new FileReader();
            // 得到文件列表数组
            fileObj = $(this)[0].files[0];
            // 拿到文件数据
            reader.readAsDataURL(fileObj);

            reader.onload = function() {
                // 获取文件信息
                imgData = reader.result;
                // 显示图片
                $("#showImg").attr("src", imgData);
                $("#showImg").show();
            }
        });

    })



</script>


<body>
<div id="content" >


                <form class="layui-form" action="PortraitServlet" method="post" enctype="multipart/form-data" name="infoForm">
                    <div class="portrait">
                        <img src="" width="100" height="100"  alt="未设置头像" align="center" id="showImg">
                    </div>
                    <div class="layui-form-item">
                        <input type="file" accept="image/*" multiple="multiple" id="myFile" name="myFile" formenctype="multipart/form-data" ><br/>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">微信号：</label>
                        <div class="layui-input-block">
                            <input type="text" name="username" id="username" required  lay-verify="required" readonly="readonly" placeholder="请输入标题" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">用户名：</label>
                        <div class="layui-input-block">
                            <input type="text" name="petname" id="petname" required  lay-verify="required" readonly="readonly" placeholder="请输入标题" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">邮箱：</label>
                        <div class="layui-input-block">
                            <input type="text" name="email" id="email" required  lay-verify="required" readonly="readonly" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <input type="hidden" name="action" value="information">
                    <input type="hidden" name="username" value="${sessionScope.user.username}">

<%--                        <label>微信号：</label> <input class="itxt" type="text" id="username" name="username" readonly="readonly" /><br/><br/>--%>
<%--                        <label>用户名：</label> <input class="itxt" type="text" id="petname" name="petname" /><br/><br/>--%>
<%--                        <label>邮箱：</label><input class="itxt" type="text" id="email" /><br/><br/>--%>
                    <div class="layui-form-item">
                        <button class="layui-btn" lay-filter="info">提交</button>
                    </div>

                </form>
</div>

<script>


    layui.use('form', function(){
        var form = layui.form;
        var layer = layui.form;

        //各种基于事件的操作，下面会有进一步介绍

        form.on('submit(info)', function(data){
            console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
            // layer.alert(JSON.stringify(data.field),{title:'提交的信息'
            // });
            layer.closeAll();
            return false;
            // return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });


    });
</script>



</body>
</html>
