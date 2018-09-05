package com.qdhualing.qrcodetracker.bean;

import java.util.List;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class WlTkVerifyResult {

    private String backDh;
    private String thDw;
    private String thRq;
    private String thR;
    private String thFzr;
    private String shFzr;
    private String remark;
    private Integer fzrID;
    private Integer fzrStatus;
    private Integer zjyID;
    private Integer zjyStatus;
    private String zjyName;

    private List<WLTkShowBean> beans;

    public String getBackDh() {
        return backDh;
    }

    public void setBackDh(String backDh) {
        this.backDh = backDh;
    }

    public String getThDw() {
        return thDw;
    }

    public void setThDw(String thDw) {
        this.thDw = thDw;
    }

    public String getThRq() {
        return thRq;
    }

    public void setThRq(String thRq) {
        this.thRq = thRq;
    }

    public String getThR() {
        return thR;
    }

    public void setThR(String thR) {
        this.thR = thR;
    }

    public String getThFzr() {
        return thFzr;
    }

    public void setThFzr(String thFzr) {
        this.thFzr = thFzr;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public Integer getFzrStatus() {
        return fzrStatus;
    }

    public void setFzrStatus(Integer fzrStatus) {
        this.fzrStatus = fzrStatus;
    }

    public Integer getZjyID() {
        return zjyID;
    }

    public void setZjyID(Integer zjyID) {
        this.zjyID = zjyID;
    }

    public Integer getZjyStatus() {
        return zjyStatus;
    }

    public void setZjyStatus(Integer zjyStatus) {
        this.zjyStatus = zjyStatus;
    }

    public String getZjyName() {
        return zjyName;
    }

    public void setZjyName(String zjyName) {
        this.zjyName = zjyName;
    }

    public List<WLTkShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLTkShowBean> beans) {
        this.beans = beans;
    }
}
