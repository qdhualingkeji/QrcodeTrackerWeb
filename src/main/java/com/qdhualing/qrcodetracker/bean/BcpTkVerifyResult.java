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

public class BcpTkVerifyResult {

    private String backDh;
    private String thDw;
    private String thRq;
    private String thR;
    private String thFzr;
    private String shFzr;
    private String remark;
    private Integer bzID;
    private Integer bzStatus;
    private String bzName;
    private int fzrID;
    private int fzrStatus;
    private int zjyID;
    private int zjyStatus;
    private String zjyName;
    private Integer zjldID;
    private Integer zjldStatus;
    private String zjldName;

    private List<BcpTkShowBean> beans;

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

    public int getFzrID() {
        return fzrID;
    }

    public void setFzrID(int fzrID) {
        this.fzrID = fzrID;
    }

    public int getFzrStatus() {
        return fzrStatus;
    }

    public void setFzrStatus(int fzrStatus) {
        this.fzrStatus = fzrStatus;
    }

    public int getZjyID() {
        return zjyID;
    }

    public void setZjyID(int zjyID) {
        this.zjyID = zjyID;
    }

    public int getZjyStatus() {
        return zjyStatus;
    }

    public void setZjyStatus(int zjyStatus) {
        this.zjyStatus = zjyStatus;
    }

    public String getZjyName() {
        return zjyName;
    }

    public void setZjyName(String zjyName) {
        this.zjyName = zjyName;
    }

    public List<BcpTkShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<BcpTkShowBean> beans) {
        this.beans = beans;
    }

    public Integer getBzID() {
        return bzID;
    }

    public void setBzID(Integer bzID) {
        this.bzID = bzID;
    }

    public Integer getBzStatus() {
        return bzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        this.bzStatus = bzStatus;
    }

    public String getBzName() {
        return bzName;
    }

    public void setBzName(String bzName) {
        this.bzName = bzName;
    }

    public Integer getZjldID() {
        return zjldID;
    }

    public void setZjldID(Integer zjldID) {
        this.zjldID = zjldID;
    }

    public Integer getZjldStatus() {
        return zjldStatus;
    }

    public void setZjldStatus(Integer zjldStatus) {
        this.zjldStatus = zjldStatus;
    }

    public String getZjldName() {
        return zjldName;
    }

    public void setZjldName(String zjldName) {
        this.zjldName = zjldName;
    }
}
