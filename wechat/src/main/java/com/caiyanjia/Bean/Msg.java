package com.caiyanjia.Bean;

import java.sql.Date;
import java.sql.Timestamp;

public class Msg {
    private int id;
    private String to;
    private String from;
    private String content;
    private Timestamp time;
    private Boolean state;

    @Override
    public String toString() {
        return "Msg{" +
                "id=" + id +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", state=" + state +
                '}';
    }

    public Msg(String to, String from, String content, Timestamp time, Boolean state) {
        this.to = to;
        this.from = from;
        this.content = content;
        this.time = time;
        this.state = state;
    }

    public Msg() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
