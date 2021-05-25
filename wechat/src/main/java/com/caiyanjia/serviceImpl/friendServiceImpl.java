package com.caiyanjia.serviceImpl;

import com.caiyanjia.Bean.Friend;
import com.caiyanjia.Bean.User;
import com.caiyanjia.daoImpl.friendDaoImpl;
import com.caiyanjia.service.friendService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class friendServiceImpl implements friendService {
    friendDaoImpl friendDao = new friendDaoImpl();
    @Override
    public List<User> getForApply(Connection conn) {
        return null;
    }

    @Override
    public Boolean insertApplication(Connection conn, String user_id, String friend_id, String petName, String msg, int black) {
        Friend friend = new Friend(user_id,friend_id,"",petName,1,msg,black);

        if(friendDao.insertApplication(conn,friend)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean ifCaninsert(Connection conn, String user_id, String friend_id) {
        if(friendDao.ifIsFriend(conn,user_id,friend_id) | friendDao.ifHaveApply(conn,user_id,friend_id)){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<User> fuzzyQuery(Connection conn, String search_username) {
        return friendDao.fuzzyQuery(conn, search_username);
    }

    @Override
    public List<Friend> QueryApply(Connection conn, String username) {
        return friendDao.QueryApply(conn, username);
    }

    @Override
    public Boolean agreeApply(Connection conn, String user_id, String friend_id, String petNameToUser) {
        return friendDao.agreeApply(conn, user_id, friend_id, petNameToUser);
    }

    @Override
    public Boolean deleteAplly(Connection conn, String user_id, String friend_id) {
        return friendDao.deleteAplly(conn, user_id, friend_id);
    }

    @Override
    public List<User> getFriendsList(Connection conn, String user_id) {
        return friendDao.getFriendsList(conn, user_id);
    }


}
