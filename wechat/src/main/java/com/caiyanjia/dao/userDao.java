package com.caiyanjia.dao;

import com.caiyanjia.Bean.User;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;

public interface userDao {
    /**
     * 通过用户名获取user信息
     * @param conn
     * @param username
     * @return
     */
    public User queryUserByUsername(Connection conn,String username);

    public User queryUserByUsernameAndPassword(Connection conn,String username,String password);

    public int saveUser(Connection conn,User user);

    /**
     *
     * @param conn
     * @param portrait  流
     * @param username  要存的微信号
     * @return 保存成功返回大于0，失败返回0
     */
    public int savaPortrait(Connection conn,Object portrait,String username);

    public int changUsername(Connection conn,String petName,String username);

    public User getEmail(Connection conn,String username);

    public Boolean changePassword(Connection conn,String password,String username);

    public User getPortrait(Connection conn,String username);

}
