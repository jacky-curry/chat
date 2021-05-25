package com.caiyanjia.daoImpl;

import com.caiyanjia.Bean.Msg;
import com.caiyanjia.dao.BaseDAO;
import com.caiyanjia.dao.chatDao;

import java.sql.Connection;
import java.util.List;

public class chatDaoImpl extends BaseDAO implements chatDao{


    @Override
    public Boolean sava_msg(Connection conn, Msg msg) {

        String sql = "insert into message(`to`,`from`,content,`time`,state) values(?,?,?,?,1)";
        if(Update(conn,sql,msg.getTo(),msg.getFrom(),msg.getContent(),msg.getTime()) > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void ChangeMsgState(Connection conn, String to, String from) {
        String sql = "Update message set state = 0 where `to` = ? and `from` = ? and `state` = 1";
        Update(conn,sql,to,from);
    }

    @Override
    public long getUnreadMsgCount(Connection conn, String to, String from) {
        String sql = "select count(*) from message where `to` = ? and `from` = ? and state = 1";
        return getValue(conn, sql, to, from);
    }

    @Override
    public List<Msg> getHistoryMsg(Connection conn, String to, String from) {
        String sql = "select `from`,`to`,content,`time` from message where `to` = ? and `from` = ?";

        //双方的历史记录都要获取
        List<Msg> Msg1 = getForList(conn, Msg.class, sql, to, from);
        List<Msg> Msg2 = getForList(conn, Msg.class, sql, from, to);
        if(Msg1 != null){
            if(Msg2 != null){
                Msg1.addAll(Msg2);
                return Msg1;
            } else {
                return Msg1;
            }
        } else {
            return Msg2;
        }


    }


}
