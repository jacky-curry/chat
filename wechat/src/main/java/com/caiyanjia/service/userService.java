package com.caiyanjia.service;

import com.caiyanjia.Bean.User;

import java.sql.Connection;

public interface userService {

    public User login(Connection conn,User user);

    public void registUser(Connection conn,User user);

    /**
     * 用于判断用户名是否可用
     * @param username
     * @return 用户名重复的话，就返回false ， 否则 ，返回true
     */
    public boolean existsUsername(Connection conn,String username);

    public String getEmailAsString(Connection conn, String username);

}
