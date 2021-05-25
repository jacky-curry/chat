package com.caiyanjia.dao;

import com.caiyanjia.Bean.Group;
import com.caiyanjia.Bean.User;
import com.caiyanjia.Bean.groupMsg;
import com.caiyanjia.Bean.groupShow;
import sun.text.resources.no.CollationData_no;

import java.sql.Connection;
import java.util.List;

public interface groupDao {
    /**
     * 创建一个群
     * @param conn
     * @param name
     * @param owner
     * @return
     */
    public Boolean create_group(Connection conn,String name, String owner);

    public Boolean insertGroupMember(Connection conn, List<String> usernames , int group_id ,int status);

    public int searchGroupId(Connection conn,String name);

    public List<Group> getGroupCreateByOwner(Connection conn,String username);

    public List<Group> getGroupByUser(Connection conn,String username);

    /**
     * 插入群的历史消息
     * @param conn
     * @param groupMsg
     * @return
     */
    public Boolean insertMsg(Connection conn, groupMsg groupMsg);

    public List<groupMsg> getGroupMsgByRoomId(Connection conn,int room_id);

    public Boolean changGroupName(Connection conn,String group_id,String group_name);

    public List<User> getGroupMembers(Connection conn,String group_id);

    /**
     * 踢出群成员，还有退出群聊
     * @param conn
     * @param group_id
     * @param username
     * @return
     */
    public Boolean deleteGroupMember(Connection conn,String group_id,String username);

    public List<Group> getAllGroups(Connection conn);

    public List<groupShow> getGroupApply(Connection conn, String username);

    public Boolean agreeGroupApply(Connection conn,String username,int group_id);

    public Boolean refuserGroupApply(Connection conn,String username,int group_id);

}
