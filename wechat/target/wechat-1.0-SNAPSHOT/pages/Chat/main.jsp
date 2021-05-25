<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <base href="http://localhost:8080/wechat/">
    <title>WebSocket Chat</title>
    <link rel="stylesheet" href="static/css/main.css">
<%--    <script src="static/script/mustache.min.js"></script>--%>
<%--    <script src="static/script/dateUtils.js"></script>--%>
</head>
<body>
    
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
                <footer>
                    <input class="search" placeholder="search user...">
                </footer>
            </div>

            <div class="m-list">
                <ul>
                    <li class="active">
                        <img class="avatar" src="static/img/avatar1.png" width="40" height="40">
                        <p class="name">所有人</p> 
                    </li>
                    <li>
                        <img class="avatar" src="static/img/avatar1.png" width="40" height="40">
                        <p class="name">Java老师</p> 
                    </li>
                </ul>
            </div>

        </div>

        <div class="main">

            <div class="m-message">
                <ul id="show-message">
                    <li>
                        <p class="time"><span>公元前271年5月2日13:07:12</span></p>
                        <div class="main">
                            <img class="avatar" width="30" height="30" src="static/img/avatar1.png">
                            <div class="nick">某个人</div>
                            <div class="text">这是其他人发来的信息测试！</div>
                        </div>
                    </li>

                    <li>
                        <p class="time"><span>公元前271年5月2日13:07:29</span></p>
                        <div class="main self">
                            <img class="avatar" width="30" height="30" src="static/img/avatar2.png">
                            <div class="nick">Java老师</div>
                            <div class="text">这是自发内容的一个测试</div>
                        </div>
                    </li>

                </ul>

            </div>

            <div class="m-text">
            	<div class="toolbar">
<%--            		<a class="select-pic" title="选择图片" onclick="emulateSelectFile()"></a>--%>
            		<input type="file" name="sendImage" id="file" style="display:none;" onchange="upload()">
            	</div>
                <textarea id="messageInput" name="messageInput" placeholder="按下Ctrl+Enter发送"></textarea>
                <div class="action">
                	<input type="button" class="btn_send" value="发送" id="sendBtn">
                </div>
            </div>

        </div>

    </div>
    <script id="text_messge_tpl" type="text/html">
	    <li>
			<p class="time"><span>{{sendtime}}</span></p>
			<div class="main {{self}}">
				<img class="avatar" width="30" height="30" src="images/avatar2.png">
				<div class="nick">{{username}}</div>
				<div class="text">{{message}}</div>
			</div>
		</li>
	</script>
	<script id="img_messge_tpl" type="text/html">
	    <li>
			<p class="time"><span>{{sendtime}}</span></p>
			<div class="main {{self}}">
				<img class="avatar" width="30" height="30" src="images/avatar2.png">
				<div class="nick">{{username}}</div>
				<div class="pic"></div>
			</div>
		</li>
	</script>
<%--	<script th:inline="javascript">--%>
<%--	    var currentUserName = [[${session['USER_INFO'].name}]];--%>
<%--	    console.log("当前用户:"+currentUserName);--%>
<%--	</script>--%>



    <script type="text/javascript">
        window.onload = function() {
            //让消息一开始就滚动到底部
            var messagePane = document.getElementsByClassName("m-message")[0];
            messagePane.scroll(0, messagePane.scrollHeight);

            //websocket接收数据
            var ws = new WebSocket("ws://" + window.location.host +"/wechat/mychat");
            ws.onopen=function(ev){
                console.log("websocket open");

                ws.onmessage=function(evt) {

                }
                // heartCheck.reset().start();
                // document.getElementById('msg').onkeydown = send;
                document.getElementById('sendBtn').onclick = sentText;
                console.log((ev));
            }

            ws.onerror=function (ev) {
                console.log(ev);

            }

            ws.onclose=function (event) {
                console.log(event);

            }


//                 //console.log("websocket getmessage:"+evt.data);
//                 // heartCheck.reset().start();
//                 if(typeof evt.data=="string"){ //文本消息
//                     var pushMessage = JSON.parse(evt.data);
//                     if(pushMessage.type=="TEXT"){
//
//                         var textMsgTemplate = document.getElementById('text_messge_tpl').innerHTML.trim();
//                         Mustache.parse(textMsgTemplate);
//                         var htmlAfterRendered = Mustache.render(textMsgTemplate, {
//                             username:pushMessage.fromUser.name,
//                             message:pushMessage.content,
//                             sendtime: datetimeFormat_1(pushMessage.sendDate),
//                             self: (currentUserName==pushMessage.fromUser.name)?"self":"",
//                         });
//                         document.getElementById("show-message").innerHTML += htmlAfterRendered;
//                         messagePane.scroll(0, messagePane.scrollHeight);
//                     }else if(pushMessage.type="IMAGE"){
//                         var textMsgTemplate = document.getElementById('img_messge_tpl').innerHTML.trim();
//                         Mustache.parse(textMsgTemplate);
//                         var htmlAfterRendered = Mustache.render(textMsgTemplate, {
//                             username:pushMessage.fromUser.name,
//                             sendtime: datetimeFormat_1(pushMessage.sendDate),
//                             self: (currentUserName==pushMessage.fromUser.name)?"self":"",
//                         });
//                         document.getElementById("show-message").innerHTML += htmlAfterRendered;
//                         messagePane.scroll(0, messagePane.scrollHeight);
//
// //					var item = document.createElement("li");
// //					item.innerHTML = "<span class='senderSpan'>"+pushMessage.fromUser.name + ": </span>";
// //					document.getElementById("show-message").appendChild(item);
//                     }
//                 }else{//图片消息
//                     var reader = new FileReader();
//                     reader.onload = function(event){
//                         if (event.target.readyState == FileReader.DONE){
//                             var img = document.createElement("img");
//                             img.setAttribute("width","200");
//                             img.style.setProperty("vertical-align","middle");
//                             img.src = this.result;
//                             document.getElementById("show-message").lastChild.getElementsByClassName("pic")[0].appendChild(img);
//                             setTimeout(function(){
//                                 messagePane.scroll(0, messagePane.scrollHeight);
//                             },500);//延时一下，否则可能图片没载入时滚动，不会滚动到底部
//                         }
//                     };
//                     reader.readAsDataURL(evt.data);
//
//                 }

            //websocket发送文本数据
             var sentText = function (){
                var textInput = document.getElementById("messageInput");
                ws.send(textInput.value);
                textInput.value = "";
            }


        }

    </script>
</body>
</html>