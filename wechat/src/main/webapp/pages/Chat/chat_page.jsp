<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/5/16
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <base href="http://localhost:8080/wechat/">
    <title>Java后端WebSocket的Tomcat实现</title>
    <link rel="stylesheet" href="static/css/main.css">
    <link rel="stylesheet" type="text/css" href="static/layui/css/layui.css">
</head>
<script src="static/script/jquery-1.7.2.js"></script>
<body>
<div id="message"></div>

<div id="chat">

    <div class="sidebar">
        <%--侧边的用户选择框，选到哪个用户就要读取这个用户的username 进行数据的加载，还有聊天websocket的创建，
                点击用户所在的框：

                    1.获取username
                    2.建立websocket连接
                    3.将username通过new websocket后跟的参数传到后台的Map中
                        4.还要在右边的消息框里加载消息记录

                点击下面发送消息的按钮：
                    1.将消息通过send函数发送给后台
                    2.后台通过sendMessageByClientId发送给指定的用户
                    3.并将数据保存起来
                    4.


        --%>
        <div class="m-card">
            <header>
                <img class="avatar" src="static/img/avatar1.png" width="40" height="40">
                <p class="name">我自己</p>
            </header>
        </div>

        <div class="m-list">
            <ul id="f-list">
            </ul>

            <hr class="layui-border-red">

            <ul id="g-list">

            </ul>
        </div>

    </div>

    <div class="main">

        <div class="m-message">
            <ul id="show-message">

            </ul>
        </div>


        <div class="m-text">
            <div class="toolbar">
                <%--            		<a class="select-pic" title="选择图片" onclick="emulateSelectFile()"></a>--%>
                <input type="file" name="sendImage" id="file" style="display:none;" onchange="upload()">
            </div>
            <textarea id="messageInput" name="messageInput" placeholder="按下Ctrl+Enter发送"></textarea>
            <div class="action">
                <input type="button" class="btn_send" value="发送" onclick="send()">
            </div>
        </div>

    </div>

</div>

</body>
<%--
    聊天界面设置步骤：
        核心：背景、聊天信息单元格式、websocket的连接




    聊天功能：
        核心：前端数据和后台数据之间的传递，以及聊天记录的保存

--%>
<script type="text/javascript">
    var current_username;
    let login_username;

    $(function () {//页面完成加载后
        /*
            1.加载好友列表中的好友
                发送Ajax请求，获取friend表中好友信息
            2.建立连接
            3.
         */

        login_username = "${sessionScope.user.username}";
        addFriendList();
        connect();


    })

    //发送消息
    function send() {

        //判断是群聊天还是私聊
        if (current_username != null) {

            var message = $("#messageInput").val();
            // alert(message);
            // alert("这里是发送的");
            var msg_json = {
                "to": current_username,
                "from": login_username,
                "content": message,
                "state": 1,
                "time": new Date()
            }
            var json_toString = JSON.stringify(msg_json);
            $("#messageInput").val("");
            //发送信息
            websocket.send(json_toString);
            //将信息显示在前端
            addMsg(msg_json, 0);

            $('.m-message').scrollTop($('.m-message')[0].scrollHeight);
        } else {
            if (current_group != null) {

                var group_message = $("#messageInput").val();
                var group_msg_json = {//用于前端的展示
                    "room_id": current_group,
                    "user_id": login_username,
                    "content": group_message,
                    "time": new Date()
                }


                $("#messageInput").val("");
                websocket2.send(group_message);
                //显示数据在前端
                addGroupMsg(group_msg_json, 0);

            } else {
                alert("需要选择消息发送的对象");
            }
        }
    }

    function addFriendList() {
        //发送Ajax请求
        $.ajax({
            url: "FriendServlet",
            type: "post",
            data: "action=getMyFriends",
            dataType: "json",
            success: function (data) {
                // console.log(data);
                var dataString = JSON.stringify(data);
                for (let i = 0; i < dataString.length; i++) {
                    //展示好友列表
                    showFriendsList(data[i].username);
                    //展示未读消息的条数
                    //from:data[i].username  to:当前登录的用户 login_
                    getUnreadMsg(data[i].username, login_username);
                }
            }
        })
    }

    function showFriendsList(username) {//图片暂时还不知道要怎么弄
        var friendItem = '<li class="active" >'
            + '<img class="avatar" src="static/img/avatar1.png" width="40" height="40">'
            + ' <p class="name" id="' + username + '" onclick="pOnclick()" >' + username + '<span class="msg_Tip" id="' + username + "_tip" + '" style="display:none" > </span></p>'
            + ' </li>'

        $(friendItem).appendTo('#f-list');
    }

    function pOnclick() {
        alert("好友被点击了")
        $("#chat .sidebar .m-list #f-list li").on("click", "p", function () {
            //获取当前的用户username
            current_username = $(this).prop("id");
            //获取两个人之间的聊天记录
            //先清空聊天区
            $("#show-message").html("");

            // alert("绑定了点击事件");
            //并按顺序显示
            GetHistoryMsg(current_username, "${sessionScope.user.username}");

            //将未读消息改为已读
            ChangeMsgState(current_username, login_username);
            //将前端的红点去掉
            $("#" + current_username + "_tip").css('display', 'none');
            alert($('.m-message')[0].scrollHeight);
            $('.m-message').scrollTop($('.m-message')[0].scrollHeight);
            // alert($(this).text());
        })
    }

    function GetHistoryMsg(from1, to) {
        $.ajax({
            url: "chatServlet",
            type: "post",
            data: {
                action: "getHistoryMsg",
                from: from1,
                to: to
            },
            // contentType:";charset=UTF-8",
            dataType: "json",
            success: function (data) {
                //转化为普通的list集合
                console.log(data);
                var dataString = JSON.stringify(data);
                //遍历集合
                // console.log("当前用户"+current_username);
                if (dataString != null) {

                    for (let i = 0; i < dataString.length; i++) {
                        var from_user = data[i].from.trim();
                        if (from_user == current_username.trim()) {
                            addMsg(data[i], 1);
                        } else {
                            addMsg(data[i], 0);
                        }
                    }
                    $('.m-message').scrollTop($('.m-message')[0].scrollHeight);
                }

            }
        })
    }


</script>


<script type="text/javascript">
    var websocket = null;

    function connect() {
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            var login_user = "${sessionScope.user.username}";
            console.log(login_user)
            // var value = $("#b").val();
            websocket = new WebSocket("ws://localhost:8080/wechat/websocket/" + _user);
            //连接发生错误的回调方法
            websocket.onerror = function (ev) {
                console.log(ev);
                // setMessageInnerHTML("WebSocket连接发生错误");
            };

            //连接成功建立的回调方法
            websocket.onopen = function () {
                // setMessageInnerHTML("WebSocket连接成功");
            }

            //接收到消息的回调方法
            websocket.onmessage = function (event) {
                //收到消息要进行判断，是谁发过来的消息，即from
                // console.log(event);
                alert("收到消息了")
                console.log(event.data);//111
                var msg_obj = JSON.parse(event.data);
                if (msg_obj.from === current_username.trim()) {
                    //添加进消息框
                    addMsg(msg_obj, 1);
                    setMessageInnerHTML(event.data);
                    //将数据库中的消息状态改为已读
                    // alart("在当前聊天框");
                    ChangeMsgState(msg_obj.from, msg_obj.to);
                    $('.m-message').scrollTop($('.m-message')[0].scrollHeight);
                } else {
                    //通过ajax，获取未读消息条数
                    //显示小红点
                    alert("不再当前聊天框");
                    getUnreadMsg(msg_obj.from, msg_obj.to);
                }
                //如果from==current_username的话就显示在对话框内，并将数据库中的数据改为已读，
                //否则就读取数据库中未读消息的条数，显示在小红点上


            }

            //连接关闭的回调方法
            websocket.onclose = function () {
                // setMessageInnerHTML("WebSocket连接关闭");
            }

            //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
            window.onbeforeunload = function () {
                closeWebSocket();
            }


        } else {
            alert('当前浏览器 Not support websocket')
        }
    };

    function showUnreadMsg(count, from) {
        //找到from所在的p的li标签，添加一个小数字
        // var id = "#" + from;
        // var point =  '<p color="red" > '+ count +' </p>';
        console.log("未读消息")
        // console.log($("#" + from +"_tip"));
        $("#" + from + "_tip").text(count);
        $("#" + from + "_tip").css('display', 'block');
        // $("#" +from).add(point);
    }

    //收到未读消息
    function getUnreadMsg(from, to) {
        $.ajax({
            url: "chatServlet",
            type: "post",
            data: {
                action: "getUnreadMsg",
                from: from,
                to: to
            },
            dataType: "json",
            success: function (data) {
                //返回每个好友未读消息的条数
                //显示在界面上
                if (data.count != 0) {
                    showUnreadMsg(data.count, from);
                }
            }
        })
    }


    //将数据库中的消息状态改为已读
    function ChangeMsgState(from, to) {
        $.ajax({
            url: "http://localhost:8080/wechat/chatServlet",
            type: "post",
            data: {
                action: "changeMsgState",
                from: from,
                to: to
            },
            dataType: "json",
            success: function (data) {
            }
        })
    }


    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }


    /**
     *
     * @param msg
     * @param type 发送的信息则为0,接受到信息为其他
     */
    function addMsg(msg, type) {
        // alert(msg);
        // msg = JSON.parse(msg);
        let messageItem;
        const messageItem_self =
            '<li>'
            + '<p class="time"><span>' + msg.time + '</span></p>'
            + '<div class="main self">'
            + '<img class="avatar" width="30" height="30" src="static/img/avatar2.png">'
            + '<div class="nick">' + msg.from + '</div>'
            + '<div class="text">' + msg.content + '</div>'
            + '</div>'
            + '</li>';

        const messageItem_get =
            '<li>'
            + '<p class="time"><span>' + msg.time + '</span></p>'
            + '<div class="main">'
            + '<img class="avatar" width="30" height="30" src="static/img/avatar2.png">'
            + '<div class="nick">' + msg.from + '</div>'
            + '<div class="text">' + msg.content + '</div>'
            + '</div>'
            + '</li>';


        //0表示消息是自己发的，显示在右边，1表示显示消息在左边，是接受到的
        if (type == 0) {
            messageItem = messageItem_self;
        } else {
            messageItem = messageItem_get;
        }

        $(messageItem).appendTo('#show-message');
    }

    function addGroupMsg(msg, type) {
        let messageItem;
        const messageItem_self =
            '<li>'
            + '<p class="time"><span>' + msg.time + '</span></p>'
            + '<div class="main self">'
            + '<img class="avatar" width="30" height="30" src="static/img/avatar2.png">'
            + '<div class="nick">' + msg.user_id + '</div>'
            + '<div class="text">' + msg.content + '</div>'
            + '</div>'
            + '</li>';

        const messageItem_get =
            '<li>'
            + '<p class="time"><span>' + msg.time + '</span></p>'
            + '<div class="main">'
            + '<img class="avatar" width="30" height="30" src="static/img/avatar2.png">'
            + '<div class="nick">' + msg.user_id + '</div>'
            + '<div class="text">' + msg.content + '</div>'
            + '</div>'
            + '</li>';

        //0表示消息是自己发的，显示在右边，1表示显示消息在左边，是接受到的
        if (type == 0) {
            messageItem = messageItem_self;
        } else {
            messageItem = messageItem_get;
        }
        $(messageItem).appendTo('#show-message');

    }

</script>

<script src="static/layui/layui.js"></script>
<script type="text/javascript">
    var websocket2;
    var current_group = null;
    var current_group_name = null;
    var current_group_owner = null;

    $(function () {
        //页面加载完成后，发送ajax请求获取当前用户的群
        $.ajax({
            url: "chatServlet",
            type: "post",
            data: {
                action: "getGroupList",
                username: "${sessionScope.user.username}"
            },
            dataType: "json",
            success: function (data) {
                var dataString = JSON.stringify(data);
                for (let i = 0; i < dataString.length; i++) {
                    //展示好友列表
                    showGroupsList(data[i].id, data[i].name, data[i].owner);
                    //展示未读消息的条数
                }

            }
        })
        // GroupDetail();
    })


    function showGroupsList(group_id, group_name, owner) {//图片暂时还不知道要怎么弄
        var GroupItem = '<li class="active" >'
            + '<img class="avatar" src="static/img/avatar1.png" width="40" height="40">'
            + ' <p class="name" style="width: 100px" id="' + group_id + '" onclick="gOnclick()" >' + group_name + '<span class="msg_Tip" id="' + owner + '" style="display:none" > </span></p>'
            + '<p class="more" style="width: 30px" onclick="GroupDetail()"><i class="layui-icon layui-icon-more" style="font-size: 30px; color: cornflowerblue;"></i></p>  '
            + ' </li>'

        $(GroupItem).appendTo('#g-list');
    }

    function GroupDetail() {
        //判断当前用户是否是群主，分配不同的权限

        $("#chat .sidebar .m-list #g-list li").on("click", ".more", function () {
            current_group_owner = $(this).parent().find('span').attr('id');
        })
        layui.use(['layer'], function () {
            var layer = layui.layer;
            //判断当前点击的group_id的owner是不是自己
            console.log("群主"+current_group_owner);
            console.log("当前用户"+login_username);
            if (current_group_owner === login_username) {
                //群主页面
                layer.open({
                    type: 2,
                    content: 'pages/Chat/Group_detail.jsp',//这里content是一个普通的String
                    area: ['428px', '406px'],
                    success: function (layero, index) {
                        var body = layer.getChildFrame('body', index);

                        body.find('.layui-input-inline').val(current_group_name);
                        body.find('.layui-input-inline').attr('id', current_group);

                    }
                });
            } else {
                //群成员页面
                layer.open({
                    type: 2,
                    content: 'pages/Chat/Group_detail_member.jsp',//这里content是一个普通的String
                    area: ['428px', '406px'],
                    success: function (layero, index) {
                        var body = layer.getChildFrame('body', index);

                        body.find('.layui-input-inline').val(current_group_name);
                        body.find('.layui-input-inline').attr('id', current_group);
                        body.find('.layui-input-inline').attr('readonly',true);
                    }
                });



            }

        })
    }


    function gOnclick() {
        $("#chat .sidebar .m-list #g-list li").on("click", ".name", function () {
            //获取当前选中的current_group
            current_group = $(this).prop("id");
            current_group_owner = $(this).find('span').prop('id');
            current_group_name = $(this).text();
            // console.log("当前群名称" + current_group_name);
            // console.log("当前群的群主" + current_group_owner);
            //清空聊天区
            $("#show-message").html("");
            //获取群聊的连接
            connect(current_group);
            //显示历史该群的历史记录
            showGroupHistoryMsg();
        });
    }

    function showGroupHistoryMsg() {
        //发送ajax请求，获取该群的历史消息
        $.ajax({
            url: "chatServlet",
            type: "post",
            data: {
                action: "getGroupHistory",
                "room_id": current_group
            },
            dataType: "json",
            success: function (data) {
                //转化为普通的list集合
                console.log(data);
                var dataString = JSON.stringify(data);
                //遍历集合
                if (dataString.length !== 0) {
                    for (let i = 0; i < dataString.length; i++) {
                        var roomId = data[i].room_id;
                        if (roomId == current_group) {
                            addGroupMsg(data[i], 1);
                        } else {
                            addGroupMsg(data[i], 0);
                        }
                    }
                    $('.m-message').scrollTop($('.m-message')[0].scrollHeight);
                }

            }
        })

    }

    function connect(group_id) {
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            var ro_user = group_id + "_" + "${sessionScope.user.username}";
            console.log(ro_user)
            // var value = $("#b").val();
            websocket2 = new WebSocket("ws://localhost:8080/wechat/community/" + ro_user);
            //连接发生错误的回调方法
            websocket2.onerror = function (ev) {
                console.log(ev);
                console.log("连接群失败");
            };

            //连接成功建立的回调方法
            websocket2.onopen = function () {
                console.log("连接群成功");
            }

            //接收到消息的回调方法
            websocket2.onmessage = function (event) {

                console.log(event.data);//这个就是要显示的信息
                var group_msg_json = {//用于前端的展示
                    "room_id": current_group,
                    "user_id": login_username,
                    "content": event.data,
                    "time": new Date()
                }
                addGroupMsg(group_msg_json, 1);
                alert("接受到了信息");
            }

            //连接关闭的回调方法
            websocket2.onclose = function () {
                // setMessageInnerHTML("WebSocket连接关闭");
                console.log("群连接关闭")
            }

            //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
            window.onbeforeunload = function () {
                websocket2.close();
            }


        } else {
            alert('当前浏览器 Not support websocket');
        }
    };

</script>

</html>
