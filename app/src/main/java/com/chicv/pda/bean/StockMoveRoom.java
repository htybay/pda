package com.chicv.pda.bean;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-17
 * email: liheyu999@163.com
 *
 * 移库上架
 */
public class StockMoveRoom {


    /**
     * Id : 1
     * FromRoomId : 1
     * ToRoomId : 8
     * Status : 1
     * MoveStatus : 90
     * PrintCount : 1
     * PrintStatus : 2
     * ExceptionStatus : 0
     * MoveCount : 38
     * InCount : 38
     * GoodsCount : 0
     * BatchCodeCount : 38
     * PrintUserName : 陈登林
     * PrintTime : 2019-05-21T09:21:55.12
     * PickUserName : 搬仓下架123
     * PickEndTime : 2019-05-21T12:40:56.42
     * InUserName : 搬仓上架320
     * InEndTime : 2019-05-21T16:05:30.823
     * ExceptionCount : 0
     * ExceptionUserName :
     * Details : [{"Id":59272,"MoveId":1,"GoodsId":6809496,"BatchCode":"2CCR/001952759","SkuId":1952759,"Status":1,"MoveStatus":90,"PickTime":"2019-05-21T12:35:59.84","InTime":"2019-05-21T16:05:30.823"}]
     */

    private int Id;
    private int FromRoomId;
    private int ToRoomId;
    private int Status;
    private int MoveStatus;
    private int PrintCount;
    private int PrintStatus;
    private int ExceptionStatus;
    private int MoveCount;
    private int InCount;
    private int GoodsCount;
    private int BatchCodeCount;
    private String PrintUserName;
    private String PrintTime;
    private String PickUserName;
    private String PickEndTime;
    private String InUserName;
    private String InEndTime;
    private int ExceptionCount;
    private String ExceptionUserName;
    private List<DetailsBean> Details;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getFromRoomId() {
        return FromRoomId;
    }

    public void setFromRoomId(int FromRoomId) {
        this.FromRoomId = FromRoomId;
    }

    public int getToRoomId() {
        return ToRoomId;
    }

    public void setToRoomId(int ToRoomId) {
        this.ToRoomId = ToRoomId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getMoveStatus() {
        return MoveStatus;
    }

    public void setMoveStatus(int MoveStatus) {
        this.MoveStatus = MoveStatus;
    }

    public int getPrintCount() {
        return PrintCount;
    }

    public void setPrintCount(int PrintCount) {
        this.PrintCount = PrintCount;
    }

    public int getPrintStatus() {
        return PrintStatus;
    }

    public void setPrintStatus(int PrintStatus) {
        this.PrintStatus = PrintStatus;
    }

    public int getExceptionStatus() {
        return ExceptionStatus;
    }

    public void setExceptionStatus(int ExceptionStatus) {
        this.ExceptionStatus = ExceptionStatus;
    }

    public int getMoveCount() {
        return MoveCount;
    }

    public void setMoveCount(int MoveCount) {
        this.MoveCount = MoveCount;
    }

    public int getInCount() {
        return InCount;
    }

    public void setInCount(int InCount) {
        this.InCount = InCount;
    }

    public int getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(int GoodsCount) {
        this.GoodsCount = GoodsCount;
    }

    public int getBatchCodeCount() {
        return BatchCodeCount;
    }

    public void setBatchCodeCount(int BatchCodeCount) {
        this.BatchCodeCount = BatchCodeCount;
    }

    public String getPrintUserName() {
        return PrintUserName;
    }

    public void setPrintUserName(String PrintUserName) {
        this.PrintUserName = PrintUserName;
    }

    public String getPrintTime() {
        return PrintTime;
    }

    public void setPrintTime(String PrintTime) {
        this.PrintTime = PrintTime;
    }

    public String getPickUserName() {
        return PickUserName;
    }

    public void setPickUserName(String PickUserName) {
        this.PickUserName = PickUserName;
    }

    public String getPickEndTime() {
        return PickEndTime;
    }

    public void setPickEndTime(String PickEndTime) {
        this.PickEndTime = PickEndTime;
    }

    public String getInUserName() {
        return InUserName;
    }

    public void setInUserName(String InUserName) {
        this.InUserName = InUserName;
    }

    public String getInEndTime() {
        return InEndTime;
    }

    public void setInEndTime(String InEndTime) {
        this.InEndTime = InEndTime;
    }

    public int getExceptionCount() {
        return ExceptionCount;
    }

    public void setExceptionCount(int ExceptionCount) {
        this.ExceptionCount = ExceptionCount;
    }

    public String getExceptionUserName() {
        return ExceptionUserName;
    }

    public void setExceptionUserName(String ExceptionUserName) {
        this.ExceptionUserName = ExceptionUserName;
    }

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public static class DetailsBean {
        /**
         * Id : 59272
         * MoveId : 1
         * GoodsId : 6809496
         * BatchCode : 2CCR/001952759
         * SkuId : 1952759
         * Status : 1
         * MoveStatus : 90
         * PickTime : 2019-05-21T12:35:59.84
         * InTime : 2019-05-21T16:05:30.823
         */

        private int Id;
        private int MoveId;
        private int GoodsId;
        private String BatchCode;
        private int SkuId;
        //正常 = 1
        private int Status;
        //        未知 = 0,
        //        移库中 = 10,
        //        上架中 = 20,
        //        移库完成 = 90
        private int MoveStatus;
        private String PickTime;
        private String InTime;
        private boolean IsMatch;

        //本地使用
        private boolean isScan;

        public boolean isScan() {
            return isScan;
        }

        public void setScan(boolean scan) {
            isScan = scan;
        }

        //本地使用，用于收货，记录相同规格下的总件数，已扫件数
        private int localTotalCount;
        private int localScanCount;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getMoveId() {
            return MoveId;
        }

        public void setMoveId(int MoveId) {
            this.MoveId = MoveId;
        }

        public int getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(int GoodsId) {
            this.GoodsId = GoodsId;
        }

        public String getBatchCode() {
            return BatchCode;
        }

        public void setBatchCode(String BatchCode) {
            this.BatchCode = BatchCode;
        }

        public int getSkuId() {
            return SkuId;
        }

        public void setSkuId(int SkuId) {
            this.SkuId = SkuId;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getMoveStatus() {
            return MoveStatus;
        }

        public void setMoveStatus(int MoveStatus) {
            this.MoveStatus = MoveStatus;
        }

        public String getPickTime() {
            return PickTime;
        }

        public void setPickTime(String PickTime) {
            this.PickTime = PickTime;
        }

        public String getInTime() {
            return InTime;
        }

        public void setInTime(String InTime) {
            this.InTime = InTime;
        }

        public int getLocalTotalCount() {
            return localTotalCount;
        }

        public void setLocalTotalCount(int localTotalCount) {
            this.localTotalCount = localTotalCount;
        }

        public int getLocalScanCount() {
            return localScanCount;
        }

        public void setLocalScanCount(int localScanCount) {
            this.localScanCount = localScanCount;
        }

        public boolean isMatch() {
            return IsMatch;
        }

        public void setMatch(boolean match) {
            IsMatch = match;
        }
    }
}
