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

public class CpOutVerifyResult {

    private String outDh;
    private String lhRq;
    private Integer kgID;
    private String kg;
    private Integer kgStatus;
    private Integer fzrID;
    private String fhFzr;
    private Integer fzrStatus;
    private String remark;
    private List<CpOutShowBean> beans;

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getLhRq() {
        return lhRq;
    }

    public void setLhRq(String lhRq) {
        this.lhRq = lhRq;
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

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public String getFhFzr() {
        return fhFzr;
    }

    public void setFhFzr(String fhFzr) {
        this.fhFzr = fhFzr;
    }

    public Integer getFzrStatus() {
        return fzrStatus;
    }

    public void setFzrStatus(Integer fzrStatus) {
        this.fzrStatus = fzrStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CpOutShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<CpOutShowBean> beans) {
        this.beans = beans;
    }

}
