package com.chicv.pda.bean;

import java.util.List;

/**
 * 捡货信息
 */

public class PickGoods {

    private long id;
    // 拣货开始时间
    private String startTime;
    // 拣货完成时间
    private String stopTime;
    /// 批次物品数量
    private int totalCount;
    // 包裹数量
    private int packageCount;
    // 囤货物品数量
    private int batchCodeCount;
    // 库房
    private int roomId;
    // 位置类型
    //    未知数据 = 0,
    //    单SKU单件同列 = 1,
    //    单SKU单件同区 = 2,
    //    单SKU单件同层 = 3,
    //
    //    多SKU单件同列 = 4,
    //    多SKU单件同区 = 5,
    //    多SKU单件同层 = 6,
    //
    //    多SKU多件同列 = 7,
    //    多SKU多件同区同列数同列值 = 8,
    //    多SKU多件同区 = 9,
    //
    //    多SKU多件同层同区数同区值 = 10,
    //    多SKU多件同层 = 11,
    //
    //    多SKU多件同库同层数同层值 = 12,
    //    多SKU多件同库 = 13
    private int locationType;
    //        拣货单状态
    //        未知数据 = 0,
    //        待打印 = 10,
    //        待领取 = 20,
    //        待拣货 = 30,
    //        待配货 = 40,
    //        待出库 = 50,
    //        已完成 = 90
    private int pickStatus;
    // 数据类型
    //    未知数据 = 0,
    //    默认数据 = 1,
    //    StyleWe数据 = 2
    private int sourceType;
    //出库数量
    private int pickCount;
    // 配合数量
    private int matchCount;
    // 出库数量
    private int outCount;
    /// 异常数量
    private int exceptionCount;
    /// 拣货结束时间
    private String pickEndTime;
    // 配合结束时间
    private String matchEndTime;
    // 出库结束时间
    private String outEndTime;
    // 拣货负责人
    private String pickDutyUserName;
    /// 拣货领取时间
    private String pickReceiveTime;
    /// 配合负责人
    private String matchDutyUserName;
    /// 配货领取时间
    private String matchReceiveTime;
    /// 拣货单打印次数
    private int printNumber;
    /// 拣货明细
    public List<PickGoodsDetail> details;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getBatchCodeCount() {
        return batchCodeCount;
    }

    public void setBatchCodeCount(int batchCodeCount) {
        this.batchCodeCount = batchCodeCount;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public int getPickStatus() {
        return pickStatus;
    }

    public void setPickStatus(int pickStatus) {
        this.pickStatus = pickStatus;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getPickCount() {
        return pickCount;
    }

    public void setPickCount(int pickCount) {
        this.pickCount = pickCount;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public int getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(int exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public String getPickEndTime() {
        return pickEndTime;
    }

    public void setPickEndTime(String pickEndTime) {
        this.pickEndTime = pickEndTime;
    }

    public String getMatchEndTime() {
        return matchEndTime;
    }

    public void setMatchEndTime(String matchEndTime) {
        this.matchEndTime = matchEndTime;
    }

    public String getOutEndTime() {
        return outEndTime;
    }

    public void setOutEndTime(String outEndTime) {
        this.outEndTime = outEndTime;
    }

    public String getPickDutyUserName() {
        return pickDutyUserName;
    }

    public void setPickDutyUserName(String pickDutyUserName) {
        this.pickDutyUserName = pickDutyUserName;
    }

    public String getPickReceiveTime() {
        return pickReceiveTime;
    }

    public void setPickReceiveTime(String pickReceiveTime) {
        this.pickReceiveTime = pickReceiveTime;
    }

    public String getMatchDutyUserName() {
        return matchDutyUserName;
    }

    public void setMatchDutyUserName(String matchDutyUserName) {
        this.matchDutyUserName = matchDutyUserName;
    }

    public String getMatchReceiveTime() {
        return matchReceiveTime;
    }

    public void setMatchReceiveTime(String matchReceiveTime) {
        this.matchReceiveTime = matchReceiveTime;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }

    public List<PickGoodsDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PickGoodsDetail> details) {
        this.details = details;
    }


    /**
     * 捡货详情
     */
    public static class PickGoodsDetail {
        /// <summary>
        /// Id
        /// </summary>
        private int id;
        /// <summary>
        /// 拣货单号
        /// </summary>
        private int pickId;
        /// <summary>
        /// 物品所在位置
        /// </summary>
        private int gridId;
        /// <summary>
        /// 物品编号
        /// </summary>
        private int goodsId;
        /// <summary>
        /// 囤货规格
        /// </summary>
        private String batchCode;
        /// <summary>
        /// Spu
        /// </summary>
        private int spuId;
        /// <summary>
        /// Sku
        /// </summary>
        private int skuId;
        /// <summary>
        /// 规格
        /// </summary>
        private String specification;
        /// <summary>
        /// 是否已拣货出库
        /// </summary>
        private boolean isOut;
        /// <summary>
        /// 是否已拣货
        /// </summary>
        private boolean isPick;
        /// <summary>
        /// 配货位
        /// </summary>
        private int groupNo;
        /// <summary>
        /// 包裹编号
        /// </summary>
        private int packageId;
        /// <summary>
        /// 物品类型
        // 订单物品 = 0,
        // 囤货物品 = 1
        private int goodsType;

        /// <summary>
        /// 是否打印了条码
        /// </summary>
        private boolean isPrintBar;
        /// <summary>
        /// 错误状态
        //        未知数据 = 0,
        //        正常数据 = 1,
        //        物品丢失 = 2,
        //        订单取消 = 3,
        //        订单暂停 = 4,
        //        订单地址变更 = 5,
        //        订单快递变更 = 6,
        //        搬仓移库 = 7,
        //        调拨移位丢失 = 8,
        //        手动丢失 = 99
        private int status;
        /// <summary>
        /// 状态
        /// </summary>
        private int pickStatus;
        /// <summary>
        /// 拣货时间
        /// </summary>
        private String pickTime;
        /// <summary>
        /// 配货时间
        /// </summary>
        private String matchTime;
        /// <summary>
        /// 出库时间
        /// </summary>
        private String outTime;
        /// 错误时间
        private String exceptionTime;
        /// 拣货人
        private String pickUserName;
        /// 配货人
        private String matchUserName;
        /// 出库人
        private String outUserName;
        /// 错误执行人
        private String exceptionUserName;
        // 货位信息
        private StockInfo stockGrid;


        //是否已丢失
        private boolean isLose;

        // 是否已扫描
        private boolean isScan;

        public boolean isLose() {
            return isLose;
        }

        public void setLose(boolean lose) {
            this.isLose = lose;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPickId() {
            return pickId;
        }

        public void setPickId(int pickId) {
            this.pickId = pickId;
        }

        public int getGridId() {
            return gridId;
        }

        public void setGridId(int gridId) {
            this.gridId = gridId;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getBatchCode() {
            return batchCode;
        }

        public void setBatchCode(String batchCode) {
            this.batchCode = batchCode;
        }

        public int getSpuId() {
            return spuId;
        }

        public void setSpuId(int spuId) {
            this.spuId = spuId;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public boolean isOut() {
            return isOut;
        }

        public void setOut(boolean out) {
            isOut = out;
        }

        public boolean isPick() {
            return isPick;
        }

        public void setPick(boolean pick) {
            isPick = pick;
        }

        public int getGroupNo() {
            return groupNo;
        }

        public void setGroupNo(int groupNo) {
            this.groupNo = groupNo;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public int getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(int goodsType) {
            this.goodsType = goodsType;
        }

        public boolean isScan() {
            return isScan;
        }

        public void setScan(boolean scan) {
            isScan = scan;
        }

        public boolean isPrintBar() {
            return isPrintBar;
        }

        public void setPrintBar(boolean printBar) {
            isPrintBar = printBar;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPickStatus() {
            return pickStatus;
        }

        public void setPickStatus(int pickStatus) {
            this.pickStatus = pickStatus;
        }

        public String getPickTime() {
            return pickTime;
        }

        public void setPickTime(String pickTime) {
            this.pickTime = pickTime;
        }

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public String getOutTime() {
            return outTime;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }

        public String getExceptionTime() {
            return exceptionTime;
        }

        public void setExceptionTime(String exceptionTime) {
            this.exceptionTime = exceptionTime;
        }

        public String getPickUserName() {
            return pickUserName;
        }

        public void setPickUserName(String pickUserName) {
            this.pickUserName = pickUserName;
        }

        public String getMatchUserName() {
            return matchUserName;
        }

        public void setMatchUserName(String matchUserName) {
            this.matchUserName = matchUserName;
        }

        public String getOutUserName() {
            return outUserName;
        }

        public void setOutUserName(String outUserName) {
            this.outUserName = outUserName;
        }

        public String getExceptionUserName() {
            return exceptionUserName;
        }

        public void setExceptionUserName(String exceptionUserName) {
            this.exceptionUserName = exceptionUserName;
        }

        public StockInfo getStockGrid() {
            return stockGrid;
        }

        public void setStockGrid(StockInfo stockGrid) {
            this.stockGrid = stockGrid;
        }
    }
}