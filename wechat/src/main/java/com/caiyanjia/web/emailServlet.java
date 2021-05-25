package com.caiyanjia.web;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/emailServlet")
public class emailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post方法");
        req.setCharacterEncoding("utf-8");//设置编码格式
//        resp.setContentType("application/json; charset=utf-8");
        String test = req.getParameter("test");
        System.out.println(test);
        String url = req.getParameter("url");
        System.out.println(url);
        String date = req.getParameter("date");
        System.out.println(date);
        String action = req.getParameter("action");
        System.out.println(action);
        String email = req.getParameter("email");
        System.out.println(email);

        System.out.println();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get方法");
        req.setCharacterEncoding("utf-8");//设置编码格式
        resp.setContentType("application/json; charset=utf-8");
        String test = req.getParameter("test");
        System.out.println(test);
        String url = req.getParameter("url");
        System.out.println(url);
        String date = req.getParameter("date");
        System.out.println(date);
        String action = req.getParameter("action");
        System.out.println(action);
        String email = req.getParameter("email");
        System.out.println(email);

        resp.getWriter().write("result:1");


        System.out.println();
    }
}
