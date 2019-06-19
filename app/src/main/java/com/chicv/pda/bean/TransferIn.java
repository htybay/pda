package com.chicv.pda.bean;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-15
 * email: liheyu999@163.com
 * 调拨入库
 */
public class TransferIn {


    /**
     * Id : 0
     * Type : 1
     * InCount : 0
     * Status : 10
     * Details : [{"DetailId":0,"Specification":"string","GoodsId":0,"BatchCode":"string","IsIn":true}]
     */

    private int Id;
    private int Type;
    private int InCount;
    private int Status;
    private List<DetailsBean> Details;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getInCount() {
        return InCount;
    }

    public void setInCount(int InCount) {
        this.InCount = InCount;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public static class DetailsBean implements Comparable<DetailsBean> {
        /**
         * DetailId : 0
         * Specification : string
         * GoodsId : 0
         * BatchCode : string
         * IsIn : true
         */

        private int DetailId;
        private String Specification;
        private int GoodsId;
        private String BatchCode;
        private boolean IsIn;

        public int getDetailId() {
            return DetailId;
        }

        public void setDetailId(int DetailId) {
            this.DetailId = DetailId;
        }

        public String getSpecification() {
            return Specification;
        }

        public void setSpecification(String Specification) {
            this.Specification = Specification;
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

        public boolean isIsIn() {
            return IsIn;
        }

        public void setIsIn(boolean IsIn) {
            this.IsIn = IsIn;
        }

        @Override
        public int compareTo(@NonNull DetailsBean o) {
            if (isIsIn() != o.isIsIn()) {
                if (isIsIn()) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        }
    }
}
