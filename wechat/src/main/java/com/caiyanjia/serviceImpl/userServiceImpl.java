package com.caiyanjia.serviceImpl;

import com.caiyanjia.Bean.User;
import com.caiyanjia.daoImpl.userDaoImpl;
import com.caiyanjia.service.userService;

import java.sql.Connection;


public class userServiceImpl extends userDaoImpl implements userService{
    @Override
    public User login(Connection conn, User user) {
        return queryUserByUsernameAndPassword(conn,user.getUsername(),user.getPassword());
    }

    @Override
    public void registUser(Connection conn,User user) {

        if(saveUser(conn,user) > 0){
            System.out.println("注册成功");
        } else {
            System.out.println("注册失败");
        }


    }


    @Override
    public boolean existsUsername(Connection conn,String username) {

        User user = queryUserByUsername(conn,username);

        if(user != null){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public String getEmailAsString(Connection conn, String username){
        User u = getEmail(conn, username);
        if(u!=null){
            return u.getEmail();
        } else {
            return null;
        }
    }


}
