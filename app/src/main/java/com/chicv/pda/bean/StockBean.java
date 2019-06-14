package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-14
 * email: liheyu999@163.com
 * \
 * 仓库实体
 */
public class StockBean {

    private int id;
    private String name;

    public StockBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
