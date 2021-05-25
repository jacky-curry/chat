package com.caiyanjia.service;

import com.caiyanjia.Bean.*;

import java.sql.Connection;
import java.util.List;

public interface chatService {

    public Boolean sava_msg(Connection conn, Msg msg);

    public void ChangeMsgState(Connection conn, String to, String from);

    /**
     * 获取两个用户之间的未读消息，用于前端的展示
     * @param conn
     * @param to
     * @param from
     * @return
     */
    public long getUnreadMsgCount(Connection conn, String to, String from);

    /**
     * 获取历史聊天记录
     * @param conn
     * @param to
     * @param from
     * @return
     */
    public List<Msg> getHistoryMsg(Connection conn, String to, String from);


    /**
     * 创建群成员
     * @param conn
     * @param usernames
     * @param group_id
     * @return
     */
    public Boolean insertGroupMember(Connection conn, List<String> usernames,int group_id ,int status);

    /**
     * 创建群
     *
     * @param conn
     * @param name
     * @param owner
     * @return
     */
    public Boolean create_group(Connection conn, String name, String owner);

    /**
     * 查找群id
     * @param conn
     * @param name
     * @return
     */
    public int searchGroupId(Connection conn, String name);

    public List<Group> getGroupCreateByOwner(Connection conn, String username);

    public List<Group> getGroupByUser(Connection conn, String username);

    /**
     * 获取当前用户的所有群，包括自己建的还有
     * @param conn
     * @param username
     * @return
     */
    public List<Group> getGroups(Connection conn,String username);

    /**
     * 插入一条群消息
     * @param conn
     * @param Msg
     * @return
     */
    public Boolean insertGroupMsg(Connection conn, groupMsg Msg);

    /**
     * 获取当前群的所有历史消息
     * @param conn
     * @param room_id
     * @return
     */
    public List<groupMsg> getGroupMsgByRoomId(Connection conn, int room_id);


    /**
     * 修改群名称
     * @param conn
     * @param group_id
     * @param group_name
     * @return
     */
    public Boolean changGroupName(Connection conn, String group_id, String group_name);

    /**
     * 获取群成员
     * @param conn
     * @param group_id
     * @return
     */
    public List<User> getGroupMembers(Connection conn, String group_id);

    /**
     * 踢出群成员和退出群聊
     * @param conn
     * @param group_id
     * @param username
     * @return
     */
    public Boolean deleteGroupMember(Connection conn, String group_id, String username);

    public List<Group> getAllGroups(Connection conn);

    public List<groupShow> getGroupApply(Connection conn, String username);

    public Boolean agreeGroupApply(Connection conn, String username, int group_id);

    public Boolean refuserGroupApply(Connection conn, String username, int group_id);

}
