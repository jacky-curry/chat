package com.caiyanjia.service;

import com.caiyanjia.Bean.Friend;
import com.caiyanjia.Bean.User;

import java.sql.Connection;
import java.util.List;

public interface friendService {
    public List<User> getForApply(Connection conn);

    /**
     * 插入好友申请信息
     * @param conn
     * @param user_id
     * @param friend_id
     * @param petName
     * @param msg
     * @param black
     * @return
     */
    public Boolean insertApplication(Connection conn,String user_id,String friend_id,String petName,String msg,int black);

    /**
     * 判断是否能插入，
     * @param conn
     * @param user_id
     * @param friend_id
     * @return 如果没有申请消息，或者已经是好友了的话，就返回false，否则就返回true
     */
    public Boolean ifCaninsert(Connection conn,String user_id,String friend_id );

    /**
     * 模糊查询
     * @param conn
     * @param search_username
     * @return
     */
    public List<User> fuzzyQuery(Connection conn, String search_username) ;

    /**
     * 查看好友申请
     * @param conn
     * @param username
     * @return
     */
    public List<Friend>  QueryApply(Connection conn,String username);

    /**
     * 同意好友申请
     * @param conn
     * @param user_id
     * @param friend_id
     * @param petNameToUser
     * @return
     */
    public Boolean agreeApply(Connection conn, String user_id, String friend_id, String petNameToUser);

    public Boolean deleteAplly(Connection conn, String user_id, String friend_id);

    /**
     * 获取所有好友信息
     * @param conn
     * @param user_id
     * @return
     */
    public List<User> getFriendsList(Connection conn,String user_id);
}
