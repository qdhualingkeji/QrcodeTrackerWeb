package com.qdhualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class NonCheckBean {

    private String dh;
    private String name;
    private String time;
    private Integer flfzrID;
    private Integer llfzrID;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getFlfzrID() {
        return flfzrID;
    }

    public void setFlfzrID(Integer flfzrID) {
        this.flfzrID = flfzrID;
    }

    public Integer getLlfzrID() {
        return llfzrID;
    }

    public void setLlfzrID(Integer llfzrID) {
        this.llfzrID = llfzrID;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
