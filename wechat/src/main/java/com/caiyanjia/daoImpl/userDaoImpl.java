package com.caiyanjia.daoImpl;

import com.caiyanjia.Bean.User;
import com.caiyanjia.dao.BaseDAO;
import com.caiyanjia.dao.userDao;
import com.mysql.jdbc.Blob;

import javax.swing.text.BadLocationException;
import java.io.InputStream;
import java.sql.Connection;

public class userDaoImpl extends BaseDAO implements userDao {


    @Override
    public User queryUserByUsername(Connection conn, String username) {
        String sql = "select `id`,`username`,`password`,`email` from user where username = ?";
        return getInstance(conn,User.class, sql, username);
    }

    @Override
    public User queryUserByUsernameAndPassword(Connection conn,String username, String password) {
        String sql = "select `id`,`username`,`password`,`email` from user where username = ? and password = ?";
        return getInstance(conn,User.class, sql, username,password);
    }

    @Override
    public int saveUser(Connection conn,User user) {
        String sql = "insert into user(`username`,`password`,`email`) values(?,?,?)";
        return Update(conn,sql, user.getUsername(),user.getPassword(),user.getEmail());
    }

    @Override
    public int savaPortrait(Connection conn, Object portrait, String username) {
        String sql = "update  `user` set portrait = ? where username = ?";

        return Update(conn,sql,portrait,username);
    }

    @Override
    public int changUsername(Connection conn, String petName,String username) {
        String sql = "update `user` set name = ? where username = ?";
        return Update(conn,sql,petName,username);
    }

    @Override
    public User getEmail(Connection conn, String username) {
        String sql = "select email from user where username = ?";
        return getInstance(conn,User.class,sql,username);
    }

    @Override
    public Boolean changePassword(Connection conn,String password, String username) {
        String sql  = "Update user set(password) values(?) where username = ?";
        return Update(conn,sql,password,username)>0;
    }

    @Override
    public User getPortrait(Connection conn, String username) {
        String sql = "select portrait from `user` where username = ?";
        return getInstance(conn,User.class,sql,username);
    }


    public User getUser(Connection conn, String username){
        String sql  = "select username,password,name,email from user where username = ?";
        return getInstance(conn,User.class,sql,username);
    }



}
