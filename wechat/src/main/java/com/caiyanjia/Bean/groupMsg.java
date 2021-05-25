package com.caiyanjia.Bean;

import java.sql.Timestamp;

public class groupMsg {
    private int id;
    private String user_id;
    private int room_id;
    private Timestamp time;
    private String content;

    public groupMsg() {
    }

    public groupMsg(String user_id, int room_id, Timestamp time, String content) {
        this.user_id = user_id;
        this.room_id = room_id;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
