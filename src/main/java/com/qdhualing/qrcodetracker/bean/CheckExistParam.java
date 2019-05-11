package com.qdhualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @desc
 * @date 2019/5/11
 */
public class CheckExistParam {

    private String qrCodeId;
    private Integer currentFunctionType;

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public Integer getCurrentFunctionType() {
        return currentFunctionType;
    }

    public void setCurrentFunctionType(Integer currentFunctionType) {
        this.currentFunctionType = currentFunctionType;
    }
}
