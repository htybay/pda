package com.chicv.pda.bean;

import java.util.List;

public class PickGoods {

    public int Id ;
    // 拣货开始时间
    public String startTime ;
    // 拣货完成时间
    public String stopTime ;
    /// 批次物品数量
    public int totalCount ;
//    /// <summary>
//    /// 包裹数量
//    /// </summary>
//    public int PackageCount { get; set; }
//    /// <summary>
//    /// 囤货物品数量
//    /// </summary>
//    public int BatchCodeCount { get; set; }
//    /// <summary>
//    /// 库房
//    /// </summary>
//    public int RoomId { get; set; }
//    /// <summary>
//    /// 位置类型
//    /// </summary>
//    public PickLocationType LocationType { get; set; }
//    /// <summary>
//    /// 拣货单状态
//    /// </summary>
//    public PickStatus PickStatus { get; set; }
//    /// <summary>
//    /// 数据类型
//    /// </summary>
//    public PickSourceType SourceType { get; set; }
//    /// <summary>
//    /// 出库数量
//    /// </summary>
//    public int PickCount { get; set; }
//    /// <summary>
//    /// 配合数量
//    /// </summary>
//    public int MatchCount { get; set; }
//    /// <summary>
//    /// 出库数量
//    /// </summary>
//    public int OutCount { get; set; }
//    /// <summary>
//    /// 异常数量
//    /// </summary>
//    public int ExceptionCount { get; set; }
//    /// <summary>
//    /// 拣货结束时间
//    /// </summary>
//    public DateTime? PickEndTime { get; set; }
//    /// <summary>
//    /// 配合结束时间
//    /// </summary>
//    public DateTime? MatchEndTime { get; set; }
//    /// <summary>
//    /// 出库结束时间
//    /// </summary>
//    public DateTime? OutEndTime { get; set; }
//    /// <summary>
//    /// 拣货负责人
//    /// </summary>
//    public string PickDutyUserName { get; set; }
//    /// <summary>
//    /// 拣货领取时间
//    /// </summary>
//    public DateTime? PickReceiveTime { get; set; }
//    /// <summary>
//    /// 配合负责人
//    /// </summary>
//    public string MatchDutyUserName { get; set; }
//    /// <summary>
//    /// 配货领取时间
//    /// </summary>
//    public DateTime? MatchReceiveTime { get; set; }
//    /// <summary>
//    /// 拣货单打印次数
//    /// </summary>
//    public int PrintNumber { get; set; }
//    /// <summary>
//    /// 拣货明细
//    /// </summary>
//    public List<StockPickDetails> Details  { get; set; }
//
//}
//
//public class StockPickDetails
//{
//    /// <summary>
//    /// Id
//    /// </summary>
//    public int Id { get; set; }
//    /// <summary>
//    /// 拣货单号
//    /// </summary>
//    public int PickId { get; set; }
//    /// <summary>
//    /// 物品所在位置
//    /// </summary>
//    public int GridId { get; set; }
//    /// <summary>
//    /// 物品编号
//    /// </summary>
//    public int GoodsId { get; set; }
//    /// <summary>
//    /// 囤货规格
//    /// </summary>
//    public string BatchCode { get; set; }
//    /// <summary>
//    /// Spu
//    /// </summary>
//    public int SpuId { get; set; }
//    /// <summary>
//    /// Sku
//    /// </summary>
//    public int SkuId { get; set; }
//    /// <summary>
//    /// 规格
//    /// </summary>
//    public string Specification { get; set; }
//    /// <summary>
//    /// 是否已拣货出库
//    /// </summary>
//    public bool IsOut { get; set; }
//    /// <summary>
//    /// 是否已拣货
//    /// </summary>
//    public bool IsPick { get; set; }
//    /// <summary>
//    /// 配货位
//    /// </summary>
//    public int GroupNo { get; set; }
//    /// <summary>
//    /// 包裹编号
//    /// </summary>
//    public int PackageId { get; set; }
//    /// <summary>
//    /// 物品类型
//    /// </summary>
//    public GoodsType GoodsType { get; set; }
//    /// <summary>
//    /// 是否已扫描【囤货物品时需要设置】
//    /// </summary>
//    public bool IsScan { get; set; }
//    /// <summary>
//    /// 是否打印了条码
//    /// </summary>
//    public bool IsPrintBar { get; set; }
//    /// <summary>
//    /// 错误状态
//    /// </summary>
//    public PickDetailStatus Status { get; set; }
//    /// <summary>
//    /// 状态
//    /// </summary>
//    public PickDetailPickStatus PickStatus { get; set; }
//    /// <summary>
//    /// 拣货时间
//    /// </summary>
//    public DateTime? PickTime { get; set; }
//    /// <summary>
//    /// 配货时间
//    /// </summary>
//    public DateTime? MatchTime { get; set; }
//    /// <summary>
//    /// 出库时间
//    /// </summary>
//    public DateTime? OutTime { get; set; }
//    /// <summary>
//    /// 错误时间
//    /// </summary>
//    public DateTime? ExceptionTime { get; set; }
//    /// <summary>
//    /// 拣货人
//    /// </summary>
//    public string PickUserName { get; set; }
//    /// <summary>
//    /// 配货人
//    /// </summary>
//    public string MatchUserName { get; set; }
//    /// <summary>
//    /// 出库人
//    /// </summary>
//    public string OutUserName { get; set; }
//    /// <summary>
//    /// 错误执行人
//    /// </summary>
//    public string ExceptionUserName { get; set; }
//    /// <summary>
//    /// 货位信息
//    /// </summary>
//    public View_StockGrid StockGrid { get; set; }
}
