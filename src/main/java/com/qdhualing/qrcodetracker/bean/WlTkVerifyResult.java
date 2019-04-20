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
    private Integer bzID;
    private Integer bzStatus;
    private String bzName;
    private Integer fzrID;
    private Integer tlfzrID;
    private Integer tlfzrStatus;
    private Integer kgID;
    private Integer kgStatus;
    private String kgName;
    private Integer slfzrID;
    private Integer slfzrStatus;
    private String slfzrName;

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

    public Integer getTlfzrID() {
        return tlfzrID;
    }

    public void setTlfzrID(Integer tlfzrID) {
        this.tlfzrID = tlfzrID;
    }

    public Integer getTlfzrStatus() {
        return tlfzrStatus;
    }

    public void setTlfzrStatus(Integer tlfzrStatus) {
        this.tlfzrStatus = tlfzrStatus;
    }

    public Integer getKgID() {
        return kgID;
    }

    public void setKgID(Integer kgID) {
        this.kgID = kgID;
    }

    public Integer getKgStatus() {
        return kgStatus;
    }

    public void setKgStatus(Integer kgStatus) {
        this.kgStatus = kgStatus;
    }

    public String getKgName() {
        return kgName;
    }

    public void setKgName(String kgName) {
        this.kgName = kgName;
    }

    public Integer getSlfzrID() {
        return slfzrID;
    }

    public void setSlfzrID(Integer slfzrID) {
        this.slfzrID = slfzrID;
    }

    public Integer getSlfzrStatus() {
        return slfzrStatus;
    }

    public void setSlfzrStatus(Integer slfzrStatus) {
        this.slfzrStatus = slfzrStatus;
    }

    public String getSlfzrName() {
        return slfzrName;
    }

    public void setSlfzrName(String slfzrName) {
        this.slfzrName = slfzrName;
    }

    public List<WLTkShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLTkShowBean> beans) {
        this.beans = beans;
    }
}
