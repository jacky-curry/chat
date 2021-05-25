package com.caiyanjia.daoImpl;

import com.caiyanjia.Bean.Group;
import com.caiyanjia.Bean.User;
import com.caiyanjia.Bean.groupMsg;
import com.caiyanjia.Bean.groupShow;
import com.caiyanjia.dao.BaseDAO;
import com.caiyanjia.dao.groupDao;

import java.sql.Connection;
import java.util.List;

public class GroupDaoImpl extends BaseDAO implements groupDao {

    @Override
    public Boolean create_group(Connection conn, String name, String owner) {
        String sql = "insert into `group`(name,owner) values(?,?)";
        if(Update(conn,sql,name,owner) > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean insertGroupMember(Connection conn, List<String> usernames,int group_id ,int status) {
        String sql ="insert into group_member(username,group_id,state) values(?,?,?)";
        int number = 0;
        for (String username:usernames) {
           number += Update(conn,sql,username,group_id,status);
        }
        if(number > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int searchGroupId(Connection conn, String name) {
        String sql = "select id from `group` where name = ?";
        if(getValue(conn,sql,name) != null){
            return getValue(conn,sql,name);
        } else {
            return 0;
        }
    }

    @Override
    public List<Group> getGroupCreateByOwner(Connection conn, String username) {
        String sql = "select `id`,`name`,`owner` from `group` where owner = ?";
        return getForList(conn, Group.class, sql, username);
    }

    @Override
    public List<Group> getGroupByUser(Connection conn, String username) {
        String sql = "select id,name,`owner` from `group` where id in (select group_id from group_member where username = ?)";
        return getForList(conn,Group.class,sql,username);
    }

    @Override
    public Boolean insertMsg(Connection conn, groupMsg Msg) {
        String sql = "insert group_message(content,time,room_id,user_id) values(?,?,?,?)";
        if(Update(conn,sql,Msg.getContent(),Msg.getTime(),Msg.getRoom_id(),Msg.getUser_id()) > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<groupMsg> getGroupMsgByRoomId(Connection conn, int room_id) {
        String sql = "select * from group_message where room_id = ?";
        return getForList(conn,groupMsg.class,sql,room_id);
    }

    @Override
    public Boolean changGroupName(Connection conn, String group_id, String group_name) {
        String sql = "update `group` set `name` = ? where id = ?";
        return Update(conn, sql, group_name, group_id) > 0;
    }

    @Override
    public List<User> getGroupMembers(Connection conn, String group_id) {
        String sql = "select username from group_member where group_id = ?";
        return getForList(conn,User.class,sql,group_id);
    }

    @Override
    public Boolean deleteGroupMember(Connection conn, String group_id, String username) {
        String sql = "delete from group_member where group_id = ? and username = ?";
        return Update(conn,sql,group_id,username)>0;
    }

    @Override
    public List<Group> getAllGroups(Connection conn) {
        String sql = "select * from `group`";
        return getForList(conn,Group.class,sql);
    }

    @Override
    public List<groupShow> getGroupApply(Connection conn, String username) {
        String sql1 = "select username,group_id from group_member where group_id in (select id from `group` where `owner` = ?) and state = 0";
        return getForList(conn,groupShow.class,sql1,username);
    }

    @Override
    public Boolean agreeGroupApply(Connection conn, String username, int group_id) {
        String sql = "Update group_member set state = 1 where username = ? and group_id = ?";
        return Update(conn,sql,username,group_id) > 0;
    }

    @Override
    public Boolean refuserGroupApply(Connection conn, String username, int group_id) {
        String sql = "delete from group_member where username = ? and group_id = ? and state = 0";
        return Update(conn,sql,username,group_id) > 0;
    }


}
