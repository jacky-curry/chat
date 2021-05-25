package com.caiyanjia.web;


import com.caiyanjia.Bean.groupMsg;
import com.caiyanjia.serviceImpl.chatServiceImpl;
import com.caiyanjia.utils.JDBCUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/community/{ro_user}")
public class GroupSocket {

    private static final chatServiceImpl chatService = new chatServiceImpl();
    static Connection conn = JDBCUtils.getConnection();

    private static final Map<Integer, CopyOnWriteArraySet<GroupSocket>> rooms = new HashMap<>();

    private Session session;

    private String userId;

    private Integer roomId;

    @OnOpen
    public void onOpen(@PathParam(value = "ro_user") String ro_user, Session session) {
        this.session = session;
        String[] param = ro_user.split("_");
        this.roomId = Integer.parseInt(param[0]);
        this.userId = param[1];
        CopyOnWriteArraySet<GroupSocket> friends = rooms.get(roomId);
        if (friends == null) {
            synchronized (rooms) {
                if (!rooms.containsKey(roomId)) {
                    friends = new CopyOnWriteArraySet<>();
                    rooms.put(roomId, friends);
                }
            }
        }
        friends.add(this);
    }

    @OnClose
    public void onClose() {
        CopyOnWriteArraySet<GroupSocket> friends = rooms.get(roomId);
        if (friends != null) {
            friends.remove(this);
        }
    }

    @OnMessage
    public void onMessage(final String message, Session session) {
        System.out.println(message);
        System.out.println(roomId);
        System.out.println(userId);
        System.out.println("接受到了信息");
        //新建线程来保存用户聊天信息
        new Thread(new Runnable() {
            @Override
            public void run() {
//                service.save(new User_Message(0, userId, message, roomId, new Date()));
                if(chatService.insertGroupMsg(conn,new groupMsg(userId,roomId,new Timestamp(System.currentTimeMillis()),message))){
                    System.out.println("保存消息到数据库成功");
                }
            }
        }).start();


        CopyOnWriteArraySet<GroupSocket> friends = rooms.get(roomId);
        if (friends != null) {
            for (GroupSocket item : friends) {
                if(item != this){//不要吧信息发给自己
                    item.session.getAsyncRemote().sendText(message);
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
//        log.info("发生错误" + new Date());
        error.printStackTrace();
    }


}
