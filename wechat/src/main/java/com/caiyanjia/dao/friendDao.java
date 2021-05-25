package com.caiyanjia.dao;

import com.caiyanjia.Bean.Friend;
import com.caiyanjia.Bean.User;

import java.sql.Connection;
import java.util.List;

public interface friendDao {

    /**
     * 获取所有用户信息，用于添加好友时展示
     * @param conn
     * @return
     */
    public List<User> getAllUsers(Connection conn);



    /**
     * 插入申请信息
     * @param conn
     * @param friend
     */
    public Boolean insertApplication(Connection conn, Friend friend);

    /**
     * 判断是否已经是好友
     * @param conn
     * @param username
     * @param login_username 已经登录的用户账号
     * @return
     */
    public Boolean ifIsFriend(Connection conn,String username,String login_username);

    /**
     * 判断是否已经有好友申请(双向）
     * @param conn
     * @param username
     * @param login_username
     * @return
     */
    public Boolean ifHaveApply(Connection conn,String username,String login_username);


    /**
     * 获取别人给你的申请（单向）
     * @param conn
     * @param login_username
     * @return
     */
    public List<Friend> getAllApply(Connection conn, String login_username);

    /**
     * 接受好友申请
     * @param conn
     * @param userPetName 对对方的备注
     * @param username  从前端获取的信息的来源
     * @param friendName    当前被添加的用户
     * @return
     */
    public Boolean acceptApply(Connection conn,String userPetName,String username,String friendName);


    /**
     * 用于模糊查询
     * @param conn
     * @param search_username
     * @return 返回username（用户名）的数组
     */
    public List<User> fuzzyQuery(Connection conn,String search_username);

    /**
     * 查询当前用户的所有好友申请
     * @param conn
     * @param username
     * @return
     */
    public List<Friend> QueryApply(Connection conn,String username);

    /**
     * 同意申请
     * @param conn
     * @param user_id
     * @param friend_id
     * @param petNameToUser
     * @return
     */
    public Boolean agreeApply(Connection conn,String user_id,String friend_id,String petNameToUser);

    /**
     * 拒绝申请，直接删除申请信息
     * @param conn
     * @param user_id
     * @param friend_id
     * @return
     */
    public Boolean deleteAplly(Connection conn,String user_id,String friend_id);

    /**
     * 获取当前用户的好友信息，涉及多表查询
     * @param conn
     * @param user_id
     * @return
     */
    public List<User> getFriendsList(Connection conn,String user_id);


}
