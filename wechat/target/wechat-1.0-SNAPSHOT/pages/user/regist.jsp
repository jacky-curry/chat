<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>注册微信</title>
		<!--写base标签，永远固定相对路径跳转的结果-->
		<base href="http://localhost:8080/wechat/">

		<link type="text/css" rel="stylesheet" href="static/css/style.css" >
		<script type="text/javascript" src="static/script/jquery-1.7.2.js"></script>
		<script type="text/javascript">
			// 页面加载完成之后
			$(function () {
				var vCode = null;//用于保存邮箱验证码
				// 给注册绑定单击事件
				$.fn.showreturn = $("#sub_btn").click(function () {
					// 验证用户名：必须由字母，数字下划线组成，并且长度为5到12位
					//1 获取用户名输入框里的内容
					var usernameText = $("#username").val();
					//2 创建正则表达式对象
					var usernamePatt = /^\w{5,12}$/;
					//3 使用test方法验证
					if (!usernamePatt.test(usernameText)) {
						//4 提示用户结果
						$("span.errorMsg").text("用户名不合法！");

						return false;
					}

					// 验证密码：必须由字母，数字下划线组成，并且长度为5到12位
					//1 获取用户名输入框里的内容
					var passwordText = $("#password").val();
					//2 创建正则表达式对象
					var passwordPatt = /^\w{5,12}$/;
					//3 使用test方法验证
					if (!passwordPatt.test(passwordText)) {
						//4 提示用户结果
						$("span.errorMsg").text("密码不合法！");

						return false;
					}

					// 验证确认密码：和密码相同
					//1 获取确认密码内容
					var repwdText = $("#repwd").val();
					//2 和密码相比较
					if (repwdText != passwordText) {
						//3 提示用户
						$("span.errorMsg").text("确认密码和密码不一致！");

						return false;
					}

					// 邮箱验证：xxxxx@xxx.com
					//1 获取邮箱里的内容
					var emailText = $("#email").val();
					//2 创建正则表达式对象
					var emailPatt = /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/;
					//3 使用test方法验证是否合法
					if (!emailPatt.test(emailText)) {
						//4 提示用户
						$("span.errorMsg").text("邮箱格式不合法！");
						return false;
					}

					// 验证码：现在只需要验证用户已输入。因为还没讲到服务器。验证码生成。
					var codeText = $("#code").val();

					//去掉验证码前后空格
					// alert("去空格前：["+codeText+"]")
					codeText = $.trim(codeText);
					// alert("去空格后：["+codeText+"]")
					$("span.errorMsg").text("这里能不能提交");
					if (codeText == null || codeText == "") {
						//4 提示用户
						$("span.errorMsg").text("验证码不能为空！");

						return false;
					}

					// 去掉错误信息



					return true;
				});

				$("#code_img").click( function() {
					this.src = "${basePath}kaptcha.jpg";
				})


				// 获取邮箱验证码按钮
				$("#btnGetVcode").click(function() {
					var btnGet = document.getElementById("btnGetVcode");

					// useChangeBTN();
					//1 获取邮箱里的内容
					var emailText = $("#email").val();
					//2 创建正则表达式对象
					var emailPatt = /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/;
					if (!emailPatt.test(emailText)) {
						//4 提示用户
						$("span.errorMsg").text("邮箱格式不合法！");
						return false;
					} else {
						btnGet.disabled = true;  // 为了防止多次点击
						$("span.errorMsg").text("");
						$.ajax({
							url: "userServlet",
							data: {action:"email",email:$("#email").val()},
							type: 'post',
							dataType: 'json',
							success: function(msg) {
								//在表单提交的时候判断发送的验证码 是否一致
									useChangeBTN();  // 控制下一次重新获取验证码
							},
							error:function(msg){
							}
						});

					}
				})

			});



			//修改按钮，控制验证码重新获取
			function changeBTN(){
				if(time > 0){
					$("#btnGetVcode").text("("+time+"s)"+"重新获取");
					time = time - 1;
				}
				else{
					var btnGet = document.getElementById("btnGetVcode");
					btnGet.disabled = false;
					$("#btnGetVcode").text("获取验证码");
					clearInterval(t);
					time = time0;
				}
			}
			function useChangeBTN(){
				$("#btnGetVcode").text("("+time+"s)"+"重新获取");
				time = time - 1;
				t = setInterval("changeBTN()", 1000);  // 1s调用一次
			}

		</script>
	<style type="text/css">
		.login_form{
			height:450px;
			margin-top: 25px;
		}

	</style>
	</head>
	<body>
		<div id="login_header">
			<img class="logo_img" alt="" src="" >
		</div>

			<div class="login_banner">

				<div id="l_content">
					<span class="login_word">欢迎注册</span>
				</div>

				<div id="content">
					<div class="login_form">
						<div class="login_box">
							<div class="tit">
								<h1>注册微信</h1>
								<span class="errorMsg">
									${empty requestScope.msg ? "":requestScope.msg}
								</span>
							</div>
							<div class="form">
								<form action="userServlet" method="post" onsubmit="return $().showreturn()">
									<input type="hidden" name="action" value="regist">
									<label>用户名称：</label>
									<input class="itxt" type="text" placeholder="请输入用户名"
										   value="${requestScope.username}"
										   autocomplete="off" tabindex="1" name="username" id="username"
									/>
									<br />
									<br />
									<label>用户密码：</label>
									<input class="itxt" type="password" placeholder="请输入密码"
										   value=""
										   autocomplete="off" tabindex="1" name="password" id="password" />
									<br />
									<br />
									<label>确认密码：</label>
									<input class="itxt" type="password" placeholder="确认密码"
										   value=""
										   autocomplete="off" tabindex="1" name="repwd" id="repwd" />
									<br />
									<br />
									<label>电子邮件：</label>
									<input class="itxt" type="text" placeholder="请输入邮箱地址"
										   value="${requestScope.email}"
										   autocomplete="off" tabindex="1" name="email" id="email"
									/>

									<button id="btnGetVcode" style="cursor:pointer">获取验证码</button>
									<label>邮箱验证码：</label>
									<input type="text" name="vcode" id="vcode" placeholder="输入验证码"/>
									<br />
									<br />
									<label>验证码：</label>
									<input class="itxt" type="text" name="code" style="width: 150px;" id="code" value=""/>
									<img id="code_img" alt="" src="kaptcha.jpg" style="height: 40px; width: 100px;">
									<br />
									<br />
									<input type="submit" value="注册" id="sub_btn" />
								</form>
							</div>

						</div>
					</div>
				</div>
			</div>
		<div id="bottom">
			<span>
				网页版微信2021
			</span>
		</div>
	</body>
</html>