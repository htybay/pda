package com.chicv.pda.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: liheyu
 * date: 2019-08-12
 * email: liheyu999@163.com
 * 理库 获取理库信息
 */
public class StockCardingBean implements Parcelable {


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeInt(this.Status);
        dest.writeString(this.Location);
    }

    public StockCardingBean() {
    }

    protected StockCardingBean(Parcel in) {
        this.Id = in.readInt();
        this.Status = in.readInt();
        this.Location = in.readString();
    }

    public static final Parcelable.Creator<StockCardingBean> CREATOR = new Parcelable.Creator<StockCardingBean>() {
        @Override
        public StockCardingBean createFromParcel(Parcel source) {
            return new StockCardingBean(source);
        }

        @Override
        public StockCardingBean[] newArray(int size) {
            return new StockCardingBean[size];
        }
    };
}
