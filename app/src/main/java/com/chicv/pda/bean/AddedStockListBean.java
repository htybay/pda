package com.chicv.pda.bean;

import java.util.List;

/**
 * author: liheyu
 * date: 2019-06-11
 * email: liheyu999@163.com
 * 囤货已上架货架物品信息对象
 */
public class AddedStockListBean {

    private List<AddedStock> addedStockList;

    public List<AddedStock> getAddedStockList() {
        return addedStockList;
    }

    public void setAddedStockList(List<AddedStock> addedStockList) {
        this.addedStockList = addedStockList;
    }

    public static class AddedStock {
        /// 货位编号
        /// </summary>
        private int gridId;

        /// <summary>
        /// 货位详细位置
        /// </summary>
        private String positionText;

        public int getGridId() {
            return gridId;
        }

        public void setGridId(int gridId) {
            this.gridId = gridId;
        }

        public String getPositionText() {
            return positionText;
        }

        public void setPositionText(String positionText) {
            this.positionText = positionText;
        }
    }

}
