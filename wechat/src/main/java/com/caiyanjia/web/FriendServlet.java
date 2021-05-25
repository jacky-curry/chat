package com.caiyanjia.web;

import com.caiyanjia.Bean.Friend;
import com.caiyanjia.Bean.User;
import com.caiyanjia.daoImpl.friendDaoImpl;
import com.caiyanjia.serviceImpl.friendServiceImpl;
import com.caiyanjia.utils.JDBCUtils;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/FriendServlet")
public class FriendServlet extends BaseServlet {

    static friendServiceImpl friendService = new friendServiceImpl();
    static Connection conn = JDBCUtils.getConnection();
    /*
    添加好友的底层逻辑
    页面初始化
    1.在后台获取所有用户的信息
        判断是否已经是好友或者已经有好友申请
            是：不插入信息
            否：插入好友信息

    点击添加好友
    2.从前端获取当前所添加用户的账号username，还有当前以登录的login_username
            弹出信息框
                msg：
                备注：（login_username对username的备注）
       写入数据库中
       **/
    public void showAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("展示所有数据");
        //还有一个date的数据数组
        friendDaoImpl friendDao = new friendDaoImpl();
        Connection connection = JDBCUtils.getConnection();

        List<User> allUsers = friendDao.getAllUsers(connection);

        //这里还要排除掉自己的信息，不能自己添加自己


        Gson gson = new Gson();
        String jsonString = gson.toJson(allUsers);
        String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";

        resp.getWriter().write(backDate);



    }

    public void addFriends(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("添加好友");

        /*
        添加好友：
            获取petName、msg、username、friend_id
            这里的friend_id从add_friend传过来,简简单单
            username也可能获取不到
         */
        String petName = req.getParameter("petName");
        String msg = req.getParameter("msg");
        String friend_id = req.getParameter("friend_id");

        System.out.println("好友id" +friend_id);
        String user_id = null;
        User user = (User) req.getSession().getAttribute("user");
        if(user != null){
            user_id = user.getUsername();
        } else {
            System.out.println("user为空");
        }
//        System.out.println(user.getUsername());

        //插入之前先判断是否已经存在申请了
        //然后返回数据给前端，进行显示

        if(friendService.ifCaninsert(conn,user_id,friend_id)){
            friendService.insertApplication(conn,user_id,friend_id,petName,msg,0);
            resp.getWriter().write(1);
        } else {
            resp.getWriter().write(0);
            System.out.println("已经是好友，或者已经有申请了");
        }





    }

    /**
     * 查找users的模糊搜索方法
     * @param req
     * @param resp
     */
    public void searchUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        /*
            1、获得前端传进来的searchName的值
            2、调用方法，进行查询
            3、将查到数据返回前端
         */

        String searchName = req.getParameter("searchName");
        System.out.println(searchName);

//        List<String> users_result = friendServlet.fuzzyQuery(conn, searchName);
        List<User> users_result = friendService.fuzzyQuery(conn,searchName);
        System.out.println(users_result);

        Gson gson = new Gson();
        String jsonString = gson.toJson(users_result);
        String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";

        resp.getWriter().write(backDate);

    }


       /*
****************
    好友申请
    1.页面初始化
        判断是否有login_username的好友申请
            有：则显式数据
                {}
            无：则不显示

    2.点击查看申请
    获取当前选中的行的用户username
        弹出：msg
            同意：
                弹出：备注：
                    确定：数据库中更新好友的状态，还有写入备注

            拒绝；
        ，
     */

    /**
     * 显示用户的好友申请信息
     * @param req
     * @param resp
     */
    public void showApply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取当前登录用户名
        System.out.println("显示申请列表");
        User user = (User) req.getSession().getAttribute("user");
        List<Friend> friendsApply = friendService.QueryApply(conn, user.getUsername());

        if(friendsApply.size() != 0){

            String msg = friendsApply.get(0).getMsg();
            System.out.println(msg);

            Gson gson = new Gson();
            String jsonString = gson.toJson(friendsApply);
            String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(backDate);
        }

    }

    /**
     * 同意好友请求
     * @param req
     * @param resp
     */
    public void agreeApply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
        1/获取前端传过来的petname，user_id,还有当前用户的username
         2.修改数据库中的申请信息
            成功：返回msg = 1
            失败：返回msg = 0


         */
        User user = (User) req.getSession().getAttribute("user");
        String friend_id = user.getUsername();
        String petName = req.getParameter("petName");
        String user_id = req.getParameter("user_id");

        System.out.println(user_id);
        System.out.println(petName);
        System.out.println(friend_id);

        if(friendService.agreeApply(conn,user_id,friend_id,petName)){
            System.out.println("同意申请成功");
            resp.getWriter().write(1);
        } else {
            System.out.println("同意申请失败");
            resp.getWriter().write(0);
        }


    }

    /**
     * 拒绝好友申请
     * @param req
     * @param resp
     */
    public void refuseApply(HttpServletRequest req, HttpServletResponse resp){
        /*
        获取当前行的用户名，还有登录的用户名
            将申请消息直接删除
         */

        String user_id = req.getParameter("user_id");
        User user = (User) req.getSession().getAttribute("user");
        String friend_id = user.getUsername();

        if(friendService.deleteAplly(conn,user_id,friend_id)){
            System.out.println("拒绝申请成功");
        } else {
            System.out.println("拒绝申请失败");
        }



    }

//*************************以下是好友聊天部分**********************
    public void getMyFriends(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取当前用户的user_id
        User user = (User) req.getSession().getAttribute("user");
        List<User> friendsList = friendService.getFriendsList(conn, user.getUsername());
        //将获取到的数据打包成json数据传回前端
        Gson gson = new Gson();
        String jsonObject = gson.toJson(friendsList);
        System.out.println(jsonObject);
//        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(jsonObject);

    }

    //************************************以下是群聊部分*********************************
    public void getAllFriends(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("群聊添加好友");
        String friend_id = req.getParameter("friend_id");


        List<User> friendsList = friendService.getFriendsList(conn, "haojia");


        Gson gson = new Gson();
        String jsonString = gson.toJson(friendsList);
        String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";

        resp.getWriter().write(backDate);
    }
}
