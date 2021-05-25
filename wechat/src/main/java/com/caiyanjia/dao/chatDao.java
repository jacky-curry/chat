package com.caiyanjia.dao;

import com.caiyanjia.Bean.Msg;

import java.sql.Connection;
import java.util.List;

public interface chatDao {
    /**
     * 保存聊天记录
     * @param conn
     * @param msg
     * @return
     */
    public Boolean sava_msg(Connection conn, Msg msg);

//    public Boolean get_msg(Connection conn,String to);

    public void ChangeMsgState(Connection conn,String to,String from);

    public long getUnreadMsgCount(Connection conn,String to,String from);

    public List<Msg> getHistoryMsg(Connection conn,String to,String from);




}
