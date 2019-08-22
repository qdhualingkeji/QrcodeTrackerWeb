package com.qdhualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class TLYLBean {

    private int ID;
    private String QRCodeID;
    private String ProductName;
    private Float syzl;
    private String dw;
    private boolean flag ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQRCodeID() {
        return QRCodeID;
    }

    public void setQRCodeID(String QRCodeID) {
        this.QRCodeID = QRCodeID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Float getSyzl() {
        return syzl;
    }

    public void setSyzl(Float syzl) {
        this.syzl = syzl;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
