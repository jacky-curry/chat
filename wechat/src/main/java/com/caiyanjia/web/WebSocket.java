package com.caiyanjia.web;



import com.caiyanjia.Bean.Message_show;
import com.caiyanjia.Bean.Msg;
import com.caiyanjia.serviceImpl.chatServiceImpl;
import com.caiyanjia.utils.JDBCUtils;
import com.google.gson.Gson;
import org.junit.Test;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;



@ServerEndpoint("/websocket/{clientId}")
public class WebSocket {
    static chatServiceImpl chatService = new chatServiceImpl();
    static Connection conn = JDBCUtils.getConnection();
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    // private static CopyOnWriteArraySet<WebSocket> webSocketSet = new
    // CopyOnWriteArraySet<WebSocket>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    //记录每个客户端的实例变量, 现在拿下面的全局map记录
    //private Session session;

    private static Map<String, Session> webSocketMap = new ConcurrentHashMap<String, Session>();

    //在线成员
    private static ConcurrentLinkedQueue<String> members = new ConcurrentLinkedQueue<String>();




    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("clientId") String clientId, Session session) {

        // 用登录用户编号和sessionId的拼接来做webSocket通信的唯一标识
        String key = getWebSocketMapKey(clientId, session);
        System.out.println(clientId);
        webSocketMap.put(key, session);
        addOnlineCount(); // 在线数加1
        System.out.println(webSocketMap);
        System.out.println("WebSocket有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("clientId") String clientId, Session session, CloseReason closeReason) {
        String key = getWebSocketMapKey(clientId, session);
        webSocketMap.remove(key, session);
        subOnlineCount(); // 在线数减1
        System.out.println("WebSocket有一连接关闭！当前在线人数为" + getOnlineCount());
    }



    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage( String message, Session session) {
        System.out.println("WebSocket收到来自客户端的消息:" + message);

        //对前端数据进行处理
        Gson gson = new Gson();
        Message_show msg_json = gson.fromJson(message, Message_show.class);
        String to =  msg_json.getTo();
        String content = msg_json.getContent();
        String from = msg_json.getFrom();
//        String time = msg_json.getTime();

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Msg msg = new Msg(to,from,content, new Timestamp(System.currentTimeMillis()),true);

        String msg_jsonString = gson.toJson(msg);
        //将数据存到数据库中，
        chatService.sava_msg(conn,msg);
        //根据id发送给特点用户,为什么要发给自己呢？
        sendMessageByClientId(to, msg_jsonString);
    }


    /**
     * 获取webSocketMap集合的Key
     *
     * @param clientId 用户编号
     * @param session  webSocket的Session
     * @return
     */
    private String getWebSocketMapKey(String clientId, Session session) {
        return  clientId + "_" +session.getId() ;
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket发生错误");
    }

    public static void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    /**
     * 通过用户的编号来发送webSocket消息
     *
     * @param clientId
     * @param message
     */
    public static int sendMessageByClientId(String clientId, String message) {
        int status = 0;
        if (webSocketMap.size() > 0) {
            for (Map.Entry<String, Session> entry : webSocketMap.entrySet()) {
                try {
                    String key = entry.getKey();//获取Map里的key
                    // 判断webSocketMap中的clientId和发送的clientId是否相同
                    // 若相同则进行发送消息
                    String key1 = key.substring(0, key.lastIndexOf("_"));//获取key中的clientId
                    System.out.println(key1);
                    if (key1.equals(clientId)) {
                        System.out.println(message);
                        sendMessage(entry.getValue(), message);
                        status = 200;
                    }
                } catch (Exception e) {
                    System.out.println("WebSocket doSend is error:");
                    continue;
                }
            }
        }
        return status;
    }

    public static void sendSpeechMessageByClientId(String clientId, String message) {
        if (webSocketMap.size() > 0) {
            for (Map.Entry<String, Session> entry : webSocketMap.entrySet()) {
                try {
                    String key = entry.getKey();
                    // 判断webSocketMap中的clientId和发送的clientId是否相同
                    // 若相同则进行发送消息
                    String key1 = key.substring(0, key.lastIndexOf("_"));
                    if (key1.equals(clientId)) {
                        sendMessage(entry.getValue(), message);
                    }
                } catch (IOException e) {
                    System.out.println("WebSocket doSend is error:");
                    continue;
                }
            }
        }
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount.getAndDecrement();
    }

}
