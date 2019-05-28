package com.chicv.pda.bean;

import android.support.annotation.NonNull;

/**
 * 货位信息
 */

public class StockInfo implements Comparable<StockInfo> {
    /// <summary>
    /// 货位编号
    /// </summary>
    private long id;
    /// <summary>
    /// 货位名称
    /// </summary>
    private String name;
    /// <summary>
    /// 所属层编号
    /// </summary>
    private int rowsId;
    /// <summary>
    /// 所属层名称
    /// </summary>
    private String rowName;
    /// <summary>
    /// 所属货架编号
    /// </summary>
    private int shelfId;
    /// <summary>
    /// 所属货架名称
    /// </summary>
    private String shelfName;
    /// <summary>
    /// 所属库区编号
    /// </summary>
    private int areaId;
    /// <summary>
    /// 所属库区名称
    /// </summary>
    private String areaName;
    /// <summary>
    /// 所属库房编号
    /// </summary>
    private int roomId;
    /// <summary>
    /// 所属库房名称
    /// </summary>
    private String roomName;
    /// <summary>
    /// 所属列编号
    /// </summary>
    private int columnId;
    /// <summary>
    /// 楼层
    /// </summary>
    private int floorId;
    /// <summary>
    /// 所属列名
    /// </summary>
    private String columnName;
    /// <summary>
    /// 楼层名称
    /// </summary>
    private String floorName;
    /// <summary>
    ///
    /// </summary>
    private boolean isPickGrid;
    /// <summary>
    /// 库区排序
    /// </summary>
    private int areaSort;
    /// <summary>
    /// 货区类型
    /// </summary>
//    public AreaType areaType ;


    /// <summary>
    /// 排序
    /// </summary>
    private int sort;
    /// <summary>
    /// 是否启用
    /// </summary>
    private boolean isEnable;

    public String getDescription() {
        return areaName + columnName + "/" + shelfName + "-" + rowName + "-" + name;
    }


    @Override
    public int compareTo(@NonNull StockInfo o) {
        int i = areaSort - o.areaSort;
        if (i != 0) {
            return i;
        }

        i = columnId - o.columnId;
        if (i != 0) {
            return i;
        }

        i = shelfId - o.shelfId;
        if (i != 0) {
            return i;
        }
        i = rowsId - o.rowsId;
        if (i != 0) {
            return i;
        }
        i = (int) (id - o.id);
        if (i != 0) {
            return i;
        }

        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowsId() {
        return rowsId;
    }

    public void setRowsId(int rowsId) {
        this.rowsId = rowsId;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public boolean isPickGrid() {
        return isPickGrid;
    }

    public void setPickGrid(boolean pickGrid) {
        isPickGrid = pickGrid;
    }

    public int getAreaSort() {
        return areaSort;
    }

    public void setAreaSort(int areaSort) {
        this.areaSort = areaSort;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
