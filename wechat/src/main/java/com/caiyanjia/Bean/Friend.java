package com.caiyanjia.Bean;

public class Friend {
    private String user_id;
    private String friend_id;

    private String user_name;
    private String friend_name;
    private int relation;

    private String msg;

    private int black;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user_id='" + user_id + '\'' +
                ", friend_id='" + friend_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", friend_name='" + friend_name + '\'' +
                ", relation=" + relation +
                ", msg='" + msg + '\'' +
                ", black=" + black +
                '}';
    }

    public Friend() {
    }

    public Friend(String user_id, String friend_id, String user_name, String friend_name, int relation, String msg, int black) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.user_name = user_name;
        this.friend_name = friend_name;
        this.relation = relation;
        this.msg = msg;
        this.black = black;
    }
}
