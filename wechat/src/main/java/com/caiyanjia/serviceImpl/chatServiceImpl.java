package com.caiyanjia.serviceImpl;

import com.caiyanjia.Bean.*;
import com.caiyanjia.daoImpl.GroupDaoImpl;
import com.caiyanjia.daoImpl.chatDaoImpl;
import com.caiyanjia.service.chatService;
import com.caiyanjia.utils.ComparatorDate;
import com.caiyanjia.utils.ComparatorDate2;
import com.caiyanjia.utils.JDBCUtils;
import org.junit.Test;

import javax.persistence.SecondaryTable;
import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class chatServiceImpl implements chatService {
    chatDaoImpl chatDao = new chatDaoImpl();
    GroupDaoImpl groupDao = new GroupDaoImpl();

    @Override
    public Boolean sava_msg(Connection conn, Msg msg) {
        return chatDao.sava_msg(conn, msg);
    }

    @Override
    public void ChangeMsgState(Connection conn, String to, String from) {
        chatDao.ChangeMsgState(conn,to,from);
    }

    @Override
    public long getUnreadMsgCount(Connection conn, String to, String from) {
        return chatDao.getUnreadMsgCount(conn, to, from);
    }

    @Override
    public List<Msg> getHistoryMsg(Connection conn, String to, String from) {
        return chatDao.getHistoryMsg(conn, to, from);
    }

    @Override
    public Boolean insertGroupMember(Connection conn, List<String> usernames, int group_id,int status) {
       return groupDao.insertGroupMember(conn, usernames, group_id,status);
    }

    @Override
    public Boolean create_group(Connection conn, String name, String owner) {
        return   groupDao.create_group(conn, name, owner);
    }

    @Override
    public int searchGroupId(Connection conn, String name) {
        return groupDao.searchGroupId(conn, name);
    }

    @Override
    public List<Group> getGroupCreateByOwner(Connection conn, String username) {
        return groupDao.getGroupCreateByOwner(conn,username);
    }

    @Override
    public List<Group> getGroupByUser(Connection conn, String username) {
        return groupDao.getGroupByUser(conn, username);
    }

    @Override
    public List<Group> getGroups(Connection conn, String username) {
        List<Group> groupByUser = groupDao.getGroupByUser(conn, username);
        List<Group> groupCreateByOwner = groupDao.getGroupCreateByOwner(conn, username);
        if(groupCreateByOwner!=null){
            groupCreateByOwner.addAll(groupByUser);
            return groupCreateByOwner;
        } else {
            return groupByUser;
        }

    }

    @Override
    public Boolean insertGroupMsg(Connection conn, groupMsg groupMsg) {
        return groupDao.insertMsg(conn, groupMsg);
    }

    @Override
    public List<groupMsg> getGroupMsgByRoomId(Connection conn, int room_id) {
        List<groupMsg> groupMsgByRoomId = groupDao.getGroupMsgByRoomId(conn, room_id);
        ComparatorDate2 c = new ComparatorDate2();
        Collections.sort(groupMsgByRoomId, c);
        return groupMsgByRoomId;
    }

    @Override
    public Boolean changGroupName(Connection conn, String group_id, String group_name) {
        return groupDao.changGroupName(conn, group_id, group_name);
    }

    @Override
    public List<User> getGroupMembers(Connection conn, String group_id) {
        return groupDao.getGroupMembers(conn, group_id);
    }

    @Override
    public Boolean deleteGroupMember(Connection conn, String group_id, String username) {
        return groupDao.deleteGroupMember(conn, group_id, username);
    }

    @Override
    public List<Group> getAllGroups(Connection conn) {
        return groupDao.getAllGroups(conn);
    }

    @Override
    public List<groupShow> getGroupApply(Connection conn, String username) {
        return groupDao.getGroupApply(conn, username);
    }

    @Override
    public Boolean agreeGroupApply(Connection conn, String username, int group_id) {
        return groupDao.agreeGroupApply(conn, username, group_id);
    }

    @Override
    public Boolean refuserGroupApply(Connection conn, String username, int group_id) {
        return groupDao.refuserGroupApply(conn, username, group_id);
    }


}
