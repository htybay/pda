package com.chicv.pda.bean;

public class PickGoods {

    public int Id ;
    // 拣货开始时间
    public String startTime ;
    // 拣货完成时间
    public String stopTime ;
    /// 批次物品数量
    public int totalCount ;
    // 包裹数量
    public int packageCount;
    // 囤货物品数量
    public int batchCodeCount ;
    // 库房
    public int RoomId ;
//    // 位置类型
//    public PickLocationType LocationType ;
//    // 拣货单状态
//    public PickStatus PickStatus;
//    // 数据类型
//    public PickSourceType SourceType ;
    //出库数量
    public int pickCount ;
    // 配合数量
    public int matchCount ;
    // 出库数量
    public int outCount ;
    /// 异常数量
    public int exceptionCount ;
    /// 拣货结束时间
    public String pickEndTime ;
    // 配合结束时间
    public String matchEndTime ;
    // 出库结束时间
    public String outEndTime ;
    // 拣货负责人
    public String pickDutyUserName ;
    /// 拣货领取时间
    public String pickReceiveTime ;
    /// 配合负责人
    public String matchDutyUserName ;
    /// 配货领取时间
    public String  matchReceiveTime ;
    /// 拣货单打印次数
    public int printNumber ;
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
