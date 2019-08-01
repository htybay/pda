package com.chicv.pda.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * author: liheyu
 * date: 2019-06-10
 * email: liheyu999@163.com
 * 物品货位 实体类
 */
public class GoodsStock implements Comparable<GoodsStock> {

    private int goodsId;
    private String batchCode;
    private String position;
    private boolean isReturn;
    private String specification;
    private String notOutNum;

    //本地使用 扫描的是否是囤货规格
    private boolean isGoodsRule;

    public boolean isGoodsRule() {
        return isGoodsRule;
    }

    public void setGoodsRule(boolean goodsRule) {
        isGoodsRule = goodsRule;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getNotOutNum() {
        return notOutNum;
    }

    public void setNotOutNum(String notOutNum) {
        this.notOutNum = notOutNum;
    }

    @Override
    public int compareTo(@NonNull GoodsStock o) {
        if (TextUtils.isEmpty(batchCode) && !TextUtils.isEmpty(o.getBatchCode())) {
            return 1;
        }
        if (!TextUtils.isEmpty(batchCode) && TextUtils.isEmpty(o.getBatchCode())) {
            return -1;
        }
        if (TextUtils.isEmpty(batchCode) && TextUtils.isEmpty(o.getBatchCode())) {
            return 0;
        }
        if (TextUtils.equals(batchCode, o.getBatchCode())) {
            return 0;
        }
        return batchCode.compareTo(o.batchCode);
    }
}
