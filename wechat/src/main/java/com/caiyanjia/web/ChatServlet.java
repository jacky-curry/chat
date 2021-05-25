package com.caiyanjia.web;

import com.caiyanjia.Bean.*;
import com.caiyanjia.serviceImpl.chatServiceImpl;
import com.caiyanjia.utils.ComparatorDate;
import com.caiyanjia.utils.JDBCUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/chatServlet")
public class ChatServlet extends BaseServlet {
    static chatServiceImpl chatService = new chatServiceImpl();
    static Connection conn = JDBCUtils.getConnection();
    static Gson gson = new Gson();
    /**
     * 将未读消息改为已读
     *
     * @param req
     * @param resp
     */
    public void changeMsgState(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("消息已读程序");
        //获取前端传过来的to和from
        String to = req.getParameter("to");
        String from = req.getParameter("from");
        System.out.println(to);
        System.out.println(from);
        //在查找数据库中，将to和from匹配的消息state改为0
        chatService.ChangeMsgState(conn, to, from);

    }


    public void getUnreadMsg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("获取未读消息");
        //前端传过来的to和from
        String to = req.getParameter("to");
        String from = req.getParameter("from");
        //找到当前用户间未读消息，即state == 1 的消息条数
        int unreadMsgCount = (int) chatService.getUnreadMsgCount(conn, to, from);
        //回传给前端,以text格式
        resp.setContentType("application/json; charset=utf-8");
        String backDate = "{\"count\":" + unreadMsgCount + "}";
        resp.getWriter().write(backDate);

    }


    public void getHistoryMsg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String to = req.getParameter("to");
        String from = req.getParameter("from");

        ComparatorDate c = new ComparatorDate();

        //在数据库中获取当前两个用户间的所有聊天记录
        List<Msg> historyMsg = chatService.getHistoryMsg(conn, to, from);

//        System.out.println("修改前"+historyMsg);
        //按时间排序
        Collections.sort(historyMsg, c);
//        System.out.println("排序后" + historyMsg);
        String json = gson.toJson(historyMsg);

        resp.setContentType("text/html;charset=utf-8");

        //传回给前端进行显示
        resp.getWriter().write(json);

    }

    /**
     * 创建群和群成员
     *
     * @param req
     * @param resp
     */
    public void createGroup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String member = req.getParameter("member");
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray asJsonArray = parser.parse(member).getAsJsonArray();
        List<String> members = new ArrayList<String>();
        for (JsonElement jsonElement : asJsonArray) {
            groupShow groupShow = gson.fromJson(jsonElement, groupShow.class);
            String username = groupShow.getUsername();
            members.add(username);
        }

        //创建群，并返回群id
        if (chatService.create_group(conn, name, owner)) {
            System.out.println("创建群成功");

            //查找群id
            int groupId = chatService.searchGroupId(conn, name);
            if (groupId != 0) {
                //创建群成员
                chatService.insertGroupMember(conn,members,groupId,1);
                resp.getWriter().write(1);
            } else {
                resp.getWriter().write(0);
            }
        }

    }

    public void getGroupList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取当前用户创建的群，还有所在的群
        //获取当前登录的用户username
        String username = req.getParameter("username");
        System.out.println("获取当前用户的群列表");

        List<Group> groups = chatService.getGroups(conn, username);
        Gson gson = new Gson();
        String jsonObject = gson.toJson(groups);
        System.out.println(jsonObject);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(jsonObject);

    }

    public void getGroupHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取前端的room_id
        String room_id = req.getParameter("room_id");
        //获取历史消息的集合
        List<groupMsg> msg = chatService.getGroupMsgByRoomId(conn, Integer.parseInt(room_id));
        //返回数据
        String json = gson.toJson(msg);
        resp.setContentType("text/html;charset=utf-8");

        //传回给前端进行显示
        resp.getWriter().write(json);

    }

    public void GroupMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("加载群成员");

        //group_id下的群成员
        String group_id = req.getParameter("group_id");

        List<User> groupMembers = chatService.getGroupMembers(conn, group_id);


        Gson gson = new Gson();
        String jsonString = gson.toJson(groupMembers);
        String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";

        resp.getWriter().write(backDate);


    }

    public void changeGroupName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取要该的名字还有group的id
        String group_id = req.getParameter("group_id");
        String group_name = req.getParameter("group_name");

        System.out.println(group_name + "  " + group_id);

        if(chatService.changGroupName(conn,group_id,group_name)){
            resp.getWriter().write(1);
            System.out.println("修改群昵称成功");
        } else  {
            resp.getWriter().write(0);
            System.out.println("修改群昵称失败");
        }



    }

    public void GetOutOfGroup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String group_id = req.getParameter("group_id");
        String member = req.getParameter("member");

        if(chatService.deleteGroupMember(conn,group_id,member)){
            System.out.println("踢出成功");
            resp.getWriter().write("1");
        } else {
            System.out.println("踢出失败");
            resp.getWriter().write(0);
        }

    }

    public void ExitGroup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取gruop_id还有当前用户的username
        String group_id = req.getParameter("group_id");
        String username = req.getParameter("username");

        System.out.println(group_id + "_____" + username);

        if(chatService.deleteGroupMember(conn, group_id, username)) {
            System.out.println("退出群聊成功");
            resp.getWriter().write(1);
        } else {
            System.out.println("退出群聊失败");
            resp.getWriter().write(0);
        }


    }

    //群主列表
    public void JoinGroupList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String search = req.getParameter("search");
        List<Group> allGroups = chatService.getAllGroups(conn);
        Gson gson = new Gson();
        if(search == null){

            //获得所有的群信息
            allGroups.removeIf(g -> g.getOwner().equals(username));


            String jsonString = gson.toJson(allGroups);
            String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(backDate);
        } else {
            System.out.println("进入搜索");
            List<Group> searchGroup = new ArrayList<>();
            //用于搜索
            for (Group g: allGroups) {
                if( g.getId() == Integer.parseInt(search)){
                    searchGroup.add(g);
                }
            }
            String jsonString = gson.toJson(searchGroup);
            String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(backDate);
        }
    }

    public void JoinInGroup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取group_id和username，判断是否已经加入该群
        //      如果已经加入：显示加入失败    否则：加入群里


        String group_id = req.getParameter("group_id");
        String member = req.getParameter("member");


        System.out.println(group_id + "______" + member);

        List<String> members = new ArrayList<String>();
        members.add(member);
        if(chatService.getGroups(conn, member) != null){
            //发送群申请
            if(chatService.insertGroupMember(conn,members,Integer.parseInt(group_id),0)){
                System.out.println("发送群申请成功");
                resp.getWriter().write(0);
            } else {
                System.out.println("发送群申请失败");
            }
        } else {
            System.out.println("已经在群里，加群失败");
        }


    }


    public void showGroupApply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");

        System.out.println(username);

        System.out.println("获取群申请");
        //获取群申请信息
        List<groupShow> groupApply = chatService.getGroupApply(conn, username);
        Gson gson = new Gson();
        String jsonString = gson.toJson(groupApply);
        String backDate =  "{\"code\":\"0\",\"msg\":\"ok\",\"count\":100,\"data\":"+jsonString+"}";
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(backDate);

    }

    public void AgreeJoinIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String group_id = req.getParameter("group_id");

        if(chatService.agreeGroupApply(conn,username,Integer.parseInt(group_id))){
            System.out.println("同意申请成功");
            resp.getWriter().write(1);
        } else {
            System.out.println("同意失败");
        }

    }

    public void refuseGroupApply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String group_id = req.getParameter("group_id");

        if(chatService.refuserGroupApply(conn,username,Integer.parseInt(group_id))){
            System.out.println("拒绝成功");
            resp.getWriter().write(1);
        } else {
            System.out.println("拒绝申请失败");
        }

    }

}
