package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-08-12
 * email: liheyu999@163.com
 * 理库 获取理库信息
 */
public class StockCardingBean {


    /**
     * Id : 422
     * Status : 10
     * Location : M区14列/15-04-02
     */

    private int Id;
    private int Status;
    private String Location;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
}
