package com.caiyanjia.web;

import com.caiyanjia.Bean.User;
import com.caiyanjia.serviceImpl.userServiceImpl;
import com.caiyanjia.utils.EmailUtil;
import com.caiyanjia.utils.JDBCUtils;
import com.caiyanjia.utils.MD5Util;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/userServlet")
public class UserServlet extends BaseServlet {

    static userServiceImpl userService = new userServiceImpl();
    static Connection conn = JDBCUtils.getConnection();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //分成两个部分
            String action = req.getParameter("action");
            //判断是登录请求还是注册请求

            Method method = this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this,req,resp);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private String vCode = null;  // 后台产生的验证码


    protected void regist(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("这里是regist");

        //设置语言编码
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        //从前端获取数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");// 获取的收件人邮箱
        String vCodeReceive = req.getParameter("vcode");// 接收到前端输入的验证码
        String code = req.getParameter("code");//图形验证码
//        EmailUtil emailUtil = EmailUtil.instance;


        //用于获取图形验证码
        String token = (String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY");
        //之后删除session域中的验证码，防止被重复使用
        req.getSession().removeAttribute("KAPTCHA_SESSION_KEY");
        System.out.println(token);

        //判断邮箱验证码是否正确

//            emailUtil.sendEmail(email);
//            vCode = emailUtil.getVCode();
//            System.out.println("验证码为：" + vCode);

        if(vCodeReceive.equals(vCode)){

            // 2、检查 验证码是否正确
            if(code != null & token.equalsIgnoreCase(code)){
                //3.验证用户名是否可用
                if(userService.existsUsername(conn,username)){
                    password = MD5Util.createPassword(password);
                    //可以用，进行注册
                    userService.registUser(conn,new User(null,username,password,email));//为什么id=null
//                req.getRequestDispatcher("/pages/user/regist_success.jsp");//'/'表示webapp下
                    req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                } else {
                    System.out.println("用户["+username+"]已经存在");
                    //将信息存人requestScope域中，让前端进行调用
                    req.setAttribute("msg","用户名已存在");
                    req.setAttribute("username",username);
                    req.setAttribute("email",email);
//                req.getRequestDispatcher(("/pages/user/regist.jsp"));
                    req.getRequestDispatcher("/pages/user/regist.jsp").forward(req,resp);
                }
            } else {
                req.setAttribute("msg","验证码错误!!!");
                req.setAttribute("username",username);
                req.setAttribute("email",email);
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req,resp);
            }
        } else {
            req.setAttribute("msg","邮箱验证码错误!!!");
            req.setAttribute("username",username);
            req.setAttribute("email",email);
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req,resp);
        }
    }

    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        //从前台获取验证码
        String code = req.getParameter("code");
        //从后台获取验证码
        String token = (String) req.getSession().getAttribute("KAPTCHA_SESSION_KEY");
        req.getSession().removeAttribute("KAPTCHA_SESSION_KEY");
        //将参数值注入到user实例中，并且返回
        User user = new User();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        password = MD5Util.createPassword(password);
        user.setPassword(password);
        user.setUsername(username);

        // 调用 userService.login()登录处理业务
        User loginUser = userService.login(conn,user);
        // 如果等于 null,说明登录 失败!
        if(code != null & token.equalsIgnoreCase(code)){

            if(loginUser != null){
                //登录成功
                req.getSession().setAttribute("user",loginUser);
                req.getRequestDispatcher("/pages/user/main_page.jsp").forward(req,resp);
//                resp.sendRedirect("wechat/pages/user/main_page.jsp");
            } else {
                //登录失败，回显数据
                req.setAttribute("msg","用户名或密码错误");
                req.setAttribute("username",user.getUsername());
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req,resp);
            }
        } else {
            req.setAttribute("msg","验证码错误");
            req.setAttribute("username",user.getUsername());
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req,resp);
        }

    }


    protected void email(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("email方法被调用了");
        String email = req.getParameter("email");//获得邮箱

        EmailUtil emailUtil = EmailUtil.instance;
        emailUtil.sendEmail(email);//发送邮箱
        vCode = emailUtil.getVCode();//获取验证码

        resp.getWriter().write(1);

        System.out.println("验证码为：" + vCode);
    }
//出现错误，解决不了
    protected void changePassword_sendCode(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String email = req.getParameter("email");
        EmailUtil emailUtil = EmailUtil.instance;
        emailUtil.sendEmail(email);

        vCode = emailUtil.getVCode();
        Map<String, Object> result = new HashMap<String, Object>();
        if(vCode != null){
            result.put("result",1);
            result.put("vCode",vCode);
        } else {
            result.put("result",-1);
        }

        //生成Gson对象，把map转成json字符串返回
        Gson gson = new Gson();
        String responseStr = gson.toJson(result);
        resp.getWriter().write(responseStr);

//        resp.getWriter().write(1);
        System.out.println("验证码为：" + vCode);

    }

    protected void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.getSession().invalidate();

        resp.sendRedirect("pages/user/login.jsp");
    }

    protected void changePassword(HttpServletRequest req, HttpServletResponse resp){
        //获取username还有新的密码
        String password = req.getParameter("password");
        String username = req.getParameter("username");
        System.out.println("修改密码");
        //MD5加密
        password = MD5Util.createPassword(password);

        if(userService.changePassword(conn,password,username)){
            System.out.println("修改密码成功");
        } else {
            System.out.println("修改密码失败");
        }

    }

    protected void getEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取username查找当前用户的email并返回
        String username = req.getParameter("username");

        String emailAsString = userService.getEmailAsString(conn, username);
        if(emailAsString != null){
            resp.getWriter().write(emailAsString);
        } else {
            System.out.println("读取邮箱失败");
        }
    }


    public void getPortrait(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //获取username
        String username = req.getParameter("username");
        User user = userService.getPortrait(conn, username);
        InputStream portrait = (InputStream) user.getPortrait();
        if(portrait!=null){
            resp.getWriter().write(String.valueOf(portrait));
        }

    }


    public static void readBlob(InputStream inputStream, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
