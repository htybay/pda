package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-10-10
 * email: liheyu999@163.com
 *
 * 调样入库 根据囤货规格获取描述信息
 */
public class SampleInverfyInfo {

    private String BatchCode;
    private int GridId;
    private String OperatorId;
    private String SkuAttribute;

    public String getBatchCode() {
        return BatchCode;
    }

    public void setBatchCode(String batchCode) {
        BatchCode = batchCode;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        OperatorId = operatorId;
    }

    public String getSkuAttribute() {
        return SkuAttribute;
    }

    public void setSkuAttribute(String skuAttribute) {
        SkuAttribute = skuAttribute;
    }
}
