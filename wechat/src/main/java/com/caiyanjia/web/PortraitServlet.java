package com.caiyanjia.web;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import com.caiyanjia.Bean.User;
import com.caiyanjia.daoImpl.userDaoImpl;
import com.caiyanjia.serviceImpl.userServiceImpl;
import com.caiyanjia.utils.JDBCUtils;
import com.google.gson.Gson;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@MultipartConfig
@WebServlet("/PortraitServlet")
public class PortraitServlet extends HttpServlet {
    userDaoImpl userdao = new userServiceImpl();
    Connection conn = JDBCUtils.getConnection();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Map map=new HashMap();
        String username = req.getParameter("username");
//        System.out.println("有没有进来");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            // 文件上传

            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator fileItemIterator = upload.getItemIterator(req);
                while (fileItemIterator.hasNext()) {
                    FileItemStream item = fileItemIterator.next();
                    String name = item.getFieldName();
                    System.out.println(name);
                    if (item.isFormField()) {
                        InputStream stream = item.openStream();
                        // 普通域
                        System.out.println("普通域");
                        String value = Streams.asString(stream, "UTF-8");
                        if (map.containsKey(name)) {
                            String valueto = (String) map.get(name);
                            valueto += "," + value;
                            map.put(name, valueto);
                        } else {
                            map.put(name, value);
                        }
                        stream.close();
                    } else {
                        // 文件域
                        System.out.println("文件域");
                        InputStream in = item.openStream();// 获取文件流
                        byte[] bytes =new byte[1024*1024];
                        in.read(bytes);
//                        System.out.println(bytes);
                        BASE64Encoder encoder = new BASE64Encoder();
                        String image = encoder.encode(bytes);
//                        System.out.println(image);
                        if(userdao.savaPortrait(conn,image,username) > 0){
                            System.out.println("存储头像成功");
                        } else {
                            System.out.println("存储头像失败");
                        }
                    }
                }
                System.out.println("来自");
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("进入失败");
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        System.out.println("获取当前登录用户" + username +"的信息");
        //告诉浏览器返回的数据编码格式
        resp.setCharacterEncoding("utf-8");

        //在数据库中找到当前登录的用户信息
        User user = userdao.getUser(conn,username);

        //创建Gson对象实例
        Gson gson = new Gson();

        String jsonObject = gson.toJson(user);

        //返回json字符串
        resp.getWriter().append(jsonObject);

    }
}



