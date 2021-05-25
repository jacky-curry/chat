package com.caiyanjia.daoImpl;

import com.caiyanjia.Bean.Friend;
import com.caiyanjia.Bean.User;
import com.caiyanjia.dao.BaseDAO;
import com.caiyanjia.dao.friendDao;
import com.caiyanjia.utils.JDBCUtils;

import java.sql.Connection;
import java.util.List;

public class friendDaoImpl extends BaseDAO implements friendDao {


    @Override
    public List<User> getAllUsers(Connection conn) {
        String sql = "select username from user";
        return getForList(conn, User.class,sql);
    }

    @Override
    public Boolean insertApplication(Connection conn, Friend friend) {
        String sql = "insert into friend(user_id,friend_id,relation,friend_name,msg,black) values(?,?,1,?,?,?) ";
        if(Update(conn,sql,friend.getUser_id(),friend.getFriend_id(),friend.getFriend_name(),friend.getMsg(),friend.getBlack()) > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean ifIsFriend(Connection conn, String username, String login_username) {
        String sql = "select * from friend where relation = 0 and ((user_id = ? and friend_id = ?) or (user_id = ? and friend_id = ?)) ";
        Friend friend = getInstance(conn,Friend.class,sql,username,login_username,login_username,username);
        if(friend != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean ifHaveApply(Connection conn, String username, String login_username) {
        String sql = "select * from friend where relation = 1 and (user_id = ? and friend_id = ?) or (user_id = ? and friend_id = ?) ";
        Friend friend = getInstance(conn,Friend.class,sql,username,login_username,login_username,username);
        if(friend != null){
            return true;
        } else {
            return false;
        }

    }


    @Override
    public List<Friend> getAllApply(Connection conn, String login_username) {
        String sql = "select * from friend where friend_id = ? and relation = 1";
        return getForList(conn,Friend.class,sql,login_username);
    }

    @Override
    public Boolean acceptApply(Connection conn, String userPetName, String username, String friendName) {
        String sql = "update friend set relation = 0,user_name = ? where user_id = ? and friend_id = ?";
        if(Update(conn,sql,userPetName,username,friendName)>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> fuzzyQuery(Connection conn, String search_username) {
        String sql = "select username from user where username like ?";
       return getForList(conn,User.class,sql,"%"+search_username+"%");
    }

    @Override
    public List<Friend> QueryApply(Connection conn, String username) {
        String sql = "select msg,user_id from friend where friend_id = ? and relation = 1";
        return   getForList(conn,Friend.class,sql,username);
    }



    @Override
    public Boolean agreeApply(Connection conn, String user_id, String friend_id, String petNameToUser) {
        String sql = "update friend set relation = 0,user_name = ? where user_id = ? and friend_id = ?";
        if(Update(conn,sql,petNameToUser,user_id,friend_id) > 0){
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean deleteAplly(Connection conn, String user_id, String friend_id) {
        String sql = "delete from friend where user_id = ? and friend_id = ?";
        if(Update(conn,sql,user_id,friend_id) > 0){
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public List<User> getFriendsList(Connection conn, String user_id) {
        String sql = "SELECT portrait,NAME,username FROM USER WHERE username IN " +
                " (SELECT user_id FROM friend WHERE friend_id = ? and relation = 0 ) ";
        String sql1 = "SELECT portrait,NAME,username FROM USER WHERE username IN " +
                "  (SELECT friend_id FROM friend WHERE user_id = ? and relation = 0 )";
        //这里要找两次，用不了联合查询，因为会有两列数据
        List<User> users = getForList(conn, User.class, sql1, user_id);
        List<User> forList = getForList(conn, User.class, sql, user_id);
        for (User user:
             users) {
            forList.add(user);
        }
        return forList;
    }


}
