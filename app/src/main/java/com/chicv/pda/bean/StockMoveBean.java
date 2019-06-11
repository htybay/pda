package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-11
 * email: liheyu999@163.com
 *
 * 物品移位 根据货位号查询的货位信息
 */
public class StockMoveBean {


    /**
     * Position : 备货区01列/货架1-1-1
     */

    private String position;

    //货位ID
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
