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

public class BcpInVerifyResult {

    private String inDh;
    private String jhDw;
    private String shRq;
    private String jhR;
    private String shR;
    private Integer bzID=0;
    private String bz="";
    private Integer bzStatus=-1;
    private Integer fzrID=0;
    private String fzr="";
    private Integer fzrStatus=-1;
    private Integer zjyID=0;
    private String zjy="";
    private Integer zjyStatus=-1;
    private Integer zjldID=0;
    private String zjld="";
    private Integer zjldStatus=-1;
    private Integer flfzrID=0;
    private String jhFzr="";
    private Integer flfzrStatus=-1;
    private Integer kgID=0;
    private String kg="";
    private Integer kgStatus=-1;
    private Integer llfzrID=0;
    private String shFzr="";
    private Integer llfzrStatus=-1;
    private String remark="";

    private List<BcpInShowBean> beans;

    public String getInDh() {
        return inDh;
    }

    public void setInDh(String inDh) {
        this.inDh = inDh;
    }

    public String getJhDw() {
        return jhDw;
    }

    public void setJhDw(String jhDw) {
        this.jhDw = jhDw;
    }

    public String getShRq() {
        return shRq;
    }

    public void setShRq(String shRq) {
        this.shRq = shRq;
    }

    public String getShR() {
        return shR;
    }

    public void setShR(String shR) {
        this.shR = shR;
    }

    public String getJhR() {
        return jhR;
    }

    public void setJhR(String jhR) {
        this.jhR = jhR;
    }

    public int getFzrID() {
        return fzrID;
    }

    public void setFzrID(int fzrID) {
        this.fzrID = fzrID;
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
    }

    public int getFzrStatus() {
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

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
    }

    public Integer getZjyStatus() {
        return zjyStatus;
    }

    public void setZjyStatus(Integer zjyStatus) {
        this.zjyStatus = zjyStatus;
    }

    public List<BcpInShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<BcpInShowBean> beans) {
        this.beans = beans;
    }

    public Integer getBzID() {
        return bzID;
    }

    public void setBzID(Integer bzID) {
        this.bzID = bzID;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public Integer getBzStatus() {
        return bzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        this.bzStatus = bzStatus;
    }

    public Integer getZjldID() {
        return zjldID;
    }

    public void setZjldID(Integer zjldID) {
        this.zjldID = zjldID;
    }

    public String getZjld() {
        return zjld;
    }

    public void setZjld(String zjld) {
        this.zjld = zjld;
    }

    public Integer getZjldStatus() {
        return zjldStatus;
    }

    public void setZjldStatus(Integer zjldStatus) {
        this.zjldStatus = zjldStatus;
    }

    public Integer getFlfzrID() {
        return flfzrID;
    }

    public void setFlfzrID(Integer flfzrID) {
        this.flfzrID = flfzrID;
    }

    public String getJhFzr() {
        return jhFzr;
    }

    public void setJhFzr(String jhFzr) {
        this.jhFzr = jhFzr;
    }

    public Integer getFlfzrStatus() {
        return flfzrStatus;
    }

    public void setFlfzrStatus(Integer flfzrStatus) {
        this.flfzrStatus = flfzrStatus;
    }

    public Integer getKgID() {
        return kgID;
    }

    public void setKgID(Integer kgID) {
        this.kgID = kgID;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public Integer getKgStatus() {
        return kgStatus;
    }

    public void setKgStatus(Integer kgStatus) {
        this.kgStatus = kgStatus;
    }

    public Integer getLlfzrID() {
        return llfzrID;
    }

    public void setLlfzrID(Integer llfzrID) {
        this.llfzrID = llfzrID;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
    }

    public Integer getLlfzrStatus() {
        return llfzrStatus;
    }

    public void setLlfzrStatus(Integer llfzrStatus) {
        this.llfzrStatus = llfzrStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
