package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.AddedStockListBean;
import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.ExpressBean;
import com.chicv.pda.bean.GoodsBatchCode;
import com.chicv.pda.bean.GoodsMoveBean;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.bean.InternalPick;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.OutGoodsInfo;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.PositionGoods;
import com.chicv.pda.bean.RecommendStock;
import com.chicv.pda.bean.SampleInverfyInfo;
import com.chicv.pda.bean.StockAbnomalGoods;
import com.chicv.pda.bean.StockBackPickInfo;
import com.chicv.pda.bean.StockCardingBean;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.StockLoss;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.bean.StockPosition;
import com.chicv.pda.bean.StockPositionBean;
import com.chicv.pda.bean.StockReceiveBatch;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.bean.StockTaking;
import com.chicv.pda.bean.TakingSubTask;
import com.chicv.pda.bean.TransferBathBean;
import com.chicv.pda.bean.TransferIn;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.bean.UpdateInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.BadGoodsParam;
import com.chicv.pda.bean.param.BatchInStockParam;
import com.chicv.pda.bean.param.BatchManyMoveStockParam;
import com.chicv.pda.bean.param.BatchMoveStockParam;
import com.chicv.pda.bean.param.GoodsOutParam;
import com.chicv.pda.bean.param.HandleStockParam;
import com.chicv.pda.bean.param.InStockParam;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.MoveRoomDownParam;
import com.chicv.pda.bean.param.MoveRoomLoseParam;
import com.chicv.pda.bean.param.MoveRoomUpParam;
import com.chicv.pda.bean.param.OutStockParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.bean.param.PickInternalGoodsParam;
import com.chicv.pda.bean.param.PickListParam;
import com.chicv.pda.bean.param.RecommendStockParam;
import com.chicv.pda.bean.param.SampleInReturnParam;
import com.chicv.pda.bean.param.SampleInStockParam;
import com.chicv.pda.bean.param.StockBackOutParam;
import com.chicv.pda.bean.param.TransferGoodsAddParam;
import com.chicv.pda.bean.param.TransferInStockParam;
import com.chicv.pda.bean.param.TransferLoseParam;
import com.chicv.pda.bean.param.TransferPickGoodsParam;
import com.chicv.pda.bean.param.TransferReceiveParam;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {


    /**
     * 登陆
     */
    @POST("api/Account/Auth/Login")
    Observable<ApiResult<User>> login(@Body LoginParam param);

    /**
     * 获取拣货信息
     */
    @GET("api/Stock/Picking/GetStockPick")
    Observable<ApiResult<PickGoods>> getPickGoodsInfo(@Query("pickId") String pickId);

    /**
     * 拣货，扫描到货物时上传数据
     */
    @POST("/api/Stock/Picking/Picking")
    Observable<ApiResult<Object>> pickGoods(@Body PickGoodsParam pickId);

    /**
     * 领取捡货单
     */
    @POST("/api/Stock/Picking/Receive")
    Observable<ApiResult<Object>> receivePickGoods(@Body PickGoodsParam pickParam);

    /**
     * 更改捡货单
     */
    @POST("api/Stock/Picking/ReceiveChange")
    Observable<ApiResult<Object>> changePickGoods(@Body PickGoodsParam pickParam);

    /**
     * 配货
     */
    @POST("/api/Stock/Picking/Distribution")
    Observable<ApiResult<Object>> deliveryGoods(@Body PickGoodsParam pickParam);

    /**
     * 出库
     */
    @POST("/api/Stock/Picking/DistributionOut")
    Observable<ApiResult<Object>> outStock(@Body OutStockParam outStockParam);

    /**
     * 获取拣货丢失物品集合
     */
    @GET("/api/Stock/Picking/GetWaitLoseGoods")
    Observable<ApiResult<List<LoseGoods>>> getLosePickGoods(@Query("pickId") String pickId);

    /**
     * 拣货丢失
     */
    @GET("/api/Stock/Picking/PickingLose")
    Observable<ApiResult<Object>> losePickGoods(@Query("pickId") String pickId, @Query("goodsIds") String goodIds);

    /**
     * 获取配货丢失物品集合
     */
    @GET("/api/Stock/Picking/GetDistributionWaitLoseGoods")
    Observable<ApiResult<List<LoseGoods>>> getLoseDeliveryGoods(@Query("pickId") String pickId);

    /**
     * 配货丢失
     */
    @GET("/api/Stock/Picking/DistributionGoodsLose")
    Observable<ApiResult<Object>> loseDeliveryGoods(@Query("pickId") String pickId, @Query("goodsIds") String goodIds);

    /**
     * 分类入库---根据物品ID获取建议的货位信息
     */
    @POST("/api/Stock/Goods/GetInVerifiInfo")
    Observable<ApiResult<RecommendStock>> getRecommendStockInfo(@Body RecommendStockParam param);

    /**
     * 分类入库---入库
     */
    @POST("/api/Stock/Goods/GoodsIn")
    Observable<ApiResult<Object>> inStock(@Body InStockParam param);

    /**
     * 分类入库---根据货位ID获取库存信息及物品
     */
    @GET("/api/Stock/Goods/GetLocationGoods")
    Observable<ApiResult<LocationGoods>> getLocationGoods(@Query("gridId") long gridId);

    /**
     * 分类入库---获取库存记录
     */
    @GET("/Api/Stock/Goods/GetInRecord")
    Observable<ApiResult<List<StockRecord>>> getInRecord(@Query("goodsId") long gridId);


    /**
     * 分类入库---获取库存记录
     */
    @GET("/Api/Stock/Goods/GetOutRecord")
    Observable<ApiResult<List<StockRecord>>> getOutRecord(@Query("goodsId") long gridId);

    /**
     * 接货---根据快递单号获取收货信息
     */
    @GET("/Api/Purchase/Goods/GetReceiveByExpressNo")
    Observable<ApiResult<List<ExpressBean>>> getReceiveByExpressNo(@Query("expressNo") String expressNo);

    /**
     * 接货---更新签收信息
     */
    @POST("Api/Stock/Goods/UpdateReceiveByPDASign")
    Observable<ApiResult<Object>> expressSign(@Body ExpressBean bean);

    /**
     * 物品货位---根据货位编号获取货位信息
     */
    @GET("Api/Stock/Goods/GetPositionGoods")
    Observable<ApiResult<List<GoodsStock>>> getGoodsStockByGridId(@Query("gridId") int gridId);

    /**
     * 物品货位---根据物品编号获取货位信息
     */
    @GET("Api/Stock/Goods/PositionByGoodsId")
    Observable<ApiResult<GoodsStock>> getGoodsStockByGoodsId(@Query("goodsId") int goodsId);

    /**
     * 物品货位---根据囤货规格获取货位信息
     */
    @GET("Api/Stock/Location/PositionListByBatchCode")
    Observable<ApiResult<List<GoodsStock>>> getGoodsStockByBatchCode(@Query("batchCode") String batchCode);

    /**
     * 物品移位---根据物品ID获取移位信息
     */
    @GET("Api/Stock/Goods/GetGoodsMove")
    Observable<ApiResult<GoodsMoveBean>> getGoodsMove(@Query("id") int id);

    /**
     * 物品移位---移位多个物品
     */
    @POST("Api/Stock/Goods/GoodsManyMove")
    Observable<ApiResult<Object>> moveGoods(@Body List<GoodsMoveBean> bean);

    /**
     * 物品移位---移位物品
     */
    @GET
    Observable<ApiResult<String>> testUpdate(@Url String url);

    /**
     * 物品移位---根据货位号获取货位信息
     */
    @GET("Api/Stock/Location/GetPositionByGridId")
    Observable<ApiResult<StockPositionBean>> getPositionByGridId(@Query("id") int id);

    /**
     * 货位信息查询---根据货位号获取货位信息
     */
    @GET("Api/Stock/Location/GetGridById")
    Observable<ApiResult<StockInfo>> getStockInfoByStockId(@Query("id") int id);

    /**
     * 囤货入库---根据批次号获取信息
     */
    @GET("Api/Stock/Order/ReceiveBatchIsExists")
    Observable<ApiResult<StockReceiveBatch>> getReceiveBatch(@Query("batchCode") String batchCode);

    /**
     * 囤货入库---根据批次号获取已上架货位信息
     */
    @GET("Api/Stock/Order/GetAddedStockList")
    Observable<ApiResult<AddedStockListBean>> getAddedStockList(@Query("batchCode") String batchCode);

    /**
     * 囤货入库---根据批次号获取已上架货位限制信息
     */
    @GET("api/Stock/Goods/StockInLimit")
    Observable<ApiResult<StockLimit>> getStockLimit(@Query("gridId") int gridId, @Query("code") String code);

    /**
     * 囤货入库---入库
     */
    @POST("Api/Stock/Order/GoodsIn")
    Observable<ApiResult<Object>> batchInStock(@Body BatchInStockParam param);

    /**
     * 囤货移位---根据批次号 货位号获取数量信息
     */
    @GET("Api/Stock/Order/GetWaitSingleMoveQuantity")
    Observable<ApiResult<Integer>> getWaitMoveQuantity(@Query("gridId") int gridId, @Query("batchCode") String code);

    /**
     * 囤货移位---移位
     */
    @POST("Api/Stock/Order/StockGoodsSigleMove")
    Observable<ApiResult<Object>> batchMoveStock(@Body BatchMoveStockParam param);

    /**
     * 囤货移位---批量移位
     */
    @POST("Api/Stock/Order/StockGoodsBatchMove")
    Observable<ApiResult<Object>> batchManyMoveStock(@Body BatchManyMoveStockParam param);

    /**
     * 内部拣货单---获取内部拣货单信息
     */
    @GET("Api/Stock/Picking/GetInternalPick ")
    Observable<ApiResult<InternalPick>> getInternalPickDetail(@Query("pickId") int pickId);


    /**
     * 内部拣货单---扫到物品上时上传
     */
    @POST("Api/Stock/Picking/InternalPick")
    Observable<ApiResult<Object>> internalPickGoods(@Body List<PickInternalGoodsParam> param);

    /**
     * 内部拣货单---出库
     */
    @POST("Api/Stock/Picking/InternalOut")
    Observable<ApiResult<Object>> internalGoodsOut(@Body List<PickInternalGoodsParam> param);

    /**
     * 调拨单添加---查询囤货数量
     */
    @GET("")
    Observable<ApiResult<TransferBathBean>> getTransferBath(@Query("batchCode") String param);

    /**
     * 调拨单添加---添加
     */
    @POST("")
    Observable<ApiResult<Object>> transferBatchAdd(@Body() TransferGoodsAddParam param);

    /**
     * 调拨单拣货---获取调拨单的拣货信息
     */
    @GET("/Api/Stock/Transfer/GetTransferPick")
    Observable<ApiResult<TransferPick>> getTransferPick(@Query("transferId") int pickId);


    /**
     * 调拨单拣货---扫到物品上时上传
     */
    @POST("Api/Stock/Transfer/TransferPick")
    Observable<ApiResult<Object>> transferPickGoods(@Body TransferPickGoodsParam param);

    /**
     * 调拨单发货---发货
     */
    @GET("Api/Stock/Transfer/TransferDelivery")
    Observable<ApiResult<Object>> transferDelivery(@Query("transferId") int transferId);

    /**
     * 调拨单发货---获取调拨单
     */
    @GET("Api/Stock/Transfer/GetTransfer")
    Observable<ApiResult<TransferPick>> getTransfer(@Query("transferId") int transferId);

    /**
     * 调拨单收货---收货
     */
    @POST("Api/Stock/Transfer/TransferSigning")
    Observable<ApiResult<Object>> transferReceive(@Body TransferReceiveParam param);

    /**
     * 调拨单入库---获取入库的调拨单信息
     */
    @GET("Api/Stock/Transfer/GetTransferIn")
    Observable<ApiResult<TransferIn>> getTransferIn(@Query("transferId") int transferId);

    /**
     * 调拨单入库---入库
     */
    @POST("Api/Stock/Transfer/TransferIn")
    Observable<ApiResult<TransferIn>> transferIn(@Body TransferInStockParam param);

    /**
     * 调拨挂失---丢失
     */
    @POST("Api/Stock/Transfer/TransferLose")
    Observable<ApiResult<Object>> transferLose(@Body TransferLoseParam param);

    /**
     * 移库上架---获取移库信息
     */
    @GET("/api/Stock/MoveRoom/GetStockMoveRoom")
    Observable<ApiResult<StockMoveRoom>> getStockMoveRoom(@Query("moveId") int moveId);

    /**
     * 移库上架---移库完成
     */
    @GET("/api/Stock/MoveRoom/MoveRoomComplete")
    Observable<ApiResult<Object>> moveRoomComplete(@Query("moveId") int moveId);

    /**
     * 移库上架---上架
     */
    @POST("/api/Stock/MoveRoom/InUpperShelf")
    Observable<ApiResult<Object>> moveRoomUp(@Body MoveRoomUpParam param);

    /**
     * 移库下架---是否为清仓SKU
     */
    @GET("/api/Stock/Goods/IsClearanceSku")
    Observable<ApiResult<Boolean>> isClearanceSku(@Query("skuId") int skuId);

    /**
     * 移库下架---下架
     */
    @POST("/api/Stock/MoveRoom/PickLowerShelf")
    Observable<ApiResult<Object>> moveRoomDown(@Body MoveRoomDownParam param);

    /**
     * 移库丢失---提交
     */
    @POST("/api/Stock/MoveRoom/MoveRoomLose")
    Observable<ApiResult<Object>> moveRoomLose(@Body MoveRoomLoseParam param);

    /**
     * 检查更新
     */
    @GET("/Api/Sys/VersionCode/VersionInfo")
    Observable<ApiResult<UpdateInfo>> checkUpdate();

    /**
     * 样品入库---根据货位号获取货位信息
     */
    @GET("Api/Stock/Location/GetViewGrid")
    Observable<ApiResult<StockInfo>> getViewGrid(@Query("gridId") int id);

    /**
     * 样品入库---提交
     */
    @POST("Api/Stock/Goods/SampleGoodsIn")
    Observable<ApiResult<Object>> sampleInStock(@Body List<SampleInStockParam> param);

    /**
     * 归还上架---获取囝货规格信息
     */
    @GET("/api/Stock/StockSample/SampleReceiveBatch")
    Observable<ApiResult<StockReceiveBatch>> sampleReceiveBatch(@Query("batchCode") String batchCode);

    /**
     * 归还上架---提交
     */
    @POST("/api/Stock/StockSample/StockGoodsSigleMove")
    Observable<ApiResult<StockReceiveBatch>> sampleGoodsMove(@Body List<SampleInReturnParam> param);

    /**
     * 理库任务
     */
    @GET("/api/Stock/Carding/GetCardingList")
    Observable<ApiResult<List<StockCardingBean>>> getCardingList();

    /**
     * 理库操作
     */
    @GET("/api/Stock/Carding/GetGridCardingScaneGoods")
    Observable<ApiResult<List<GoodsBatchCode>>> getGridGoods(@Query("gridId") int gridId, @Query("lkId") int lkId);

    /**
     * 理库操作 上传数据
     */
    @POST("/api/Stock/Carding/StockCardingMove")
    Observable<ApiResult<Object>> stockCardingMove(@Body HandleStockParam param);

    /**
     * 理库点数 获取货位下的囤货规格
     */
    @GET("api/Stock/Carding/StockCardingHandleGridInfo")
    Observable<ApiResult<List<PositionGoods>>> stockCardingHandleGridInfo(@Query("gridId") int id, @Query("type") int type);

    /**
     * 理库点数 上传数据
     */
    @POST("/api/Stock/Carding/StockCardingHandleCount")
    Observable<ApiResult<Object>> stockCardingHandleCount(@Body HandleStockParam param);

    /**
     * 手动理库 上传数据
     */
    @POST("/api/Stock/Carding/StockCardingHandleMove")
    Observable<ApiResult<Object>> stockCardingHandleMove(@Body HandleStockParam param);

    /**
     * 退货物品出库 获取退货出库单信息
     */
    @GET("/api/Logistics/BackGoods/GetBackPick")
    Observable<ApiResult<Integer>> getBackPick(@Query("pickId") int pickId);

    /**
     * 退货物品出库 获取单个物品退货信息
     */
    @GET("/api/Logistics/BackGoods/GetGoodsOut")
    Observable<ApiResult<OutGoodsInfo>> getGoodsOut(@Query("goodsId") int goodsId, @Query("pickId") int pickId);

    /**
     * 退货物品出库 上传数据
     */
    @POST("/api/Logistics/BackGoods/GoodsManyOut")
    Observable<ApiResult<Object>> goodsManyOut(@Body GoodsOutParam param);

    /**
     * 囤货物品出库 判断是否存在该拣货单
     */
    @GET("/api/Stock/StockBack/GetHasStockBackPick")
    Observable<ApiResult<Object>> getHasStockBackPick(@Query("stockPickId") int stockPickId, @Query("operateName") String operateName);

    /**
     * 囤货物品出库 获取拣货单的信息
     */
    @GET("/api/Stock/StockBack/IsStockBackPickEnd")
    Observable<ApiResult<StockBackPickInfo>> isStockBackPickEnd(@Query("stockPickId") int stockPickId);

    /**
     * 囤货物品出库 当前扫描的囤货退货拣货单明细是否有存在该货位上待出库囤货
     */
    @GET("/api/Stock/StockBack/GetPositionText")
    Observable<ApiResult<StockPosition>> getPositionText(@Query("stockPickId") int stockPickId, @Query("gridId") int gridId);

    /**
     * 囤货物品出库 获取当前拣货单、货位下待出库囤货
     */
    @GET("/api/Stock/StockBack/GetWaitOutQuantity")
    Observable<ApiResult<Integer>> getWaitOutQuantity(@Query("stockPickId") int stockPickId, @Query("gridId") int gridId, @Query("batchCode") String batchCode);

    /**
     * 囤货物品出库 根据当前货位\当前规格获取库存数量
     */
    @GET("/api/Stock/StockBack/GetStockQuantity")
    Observable<ApiResult<Integer>> getStockQuantity(@Query("gridId") int gridId, @Query("batchCode") String batchCode);

    /**
     * 囤货物品出库 提交数据
     */
    @POST("/api/Stock/StockBack/StockBackOut")
    Observable<ApiResult<Object>> stockBackOut(@Body StockBackOutParam param);

    /**
     * 报损 获取报损列表
     */
    @GET("/api/Stock/StockLosses/GetStockLosses")
    Observable<ApiResult<List<StockLoss>>> getStockLosses(@Query("roomId") int roomId);

    /**
     * 报损 获取报损详情
     */
    @GET("/api/Stock/StockLosses/GetAbnomalGoodsList")
    Observable<ApiResult<List<StockAbnomalGoods>>> getAbnomalGoodsList(@Query("lossesId") int lossesId);


    /**
     * 报损 获取报损详情
     */
    @POST("/api/Stock/StockLosses/PostLossesCheckData")
    Observable<ApiResult<Object>> postLossesCheckData(@Body BadGoodsParam param);

    /**
     * 盘点 获取盘点任务
     */
    @GET("/api/Stock/StockTaking/GetTask")
    Observable<ApiResult<List<StockTaking>>> getCheckTask(@Query("userId") String userId);

    /**
     * 盘点 获取盘点详情
     */
    @GET("/api/Stock/StockTaking/GetSubtask")
    Observable<ApiResult<TakingSubTask>> getSubtask(@Query("taskId") int taskId);

    /**
     * 盘点 上传盘点数据
     */
    @POST("/api/Stock/StockTaking/SubmitSubtaskRecords")
    Observable<ApiResult<Object>> submitSubtaskRecords(@Body TakingSubTask param);


    /**
     * 取样 提交货位信息
     */
    @GET("/api/Stock/StockSample/Sampling")
    Observable<ApiResult<Object>> commitSample(@Query("gridId") int gridId);


    /**
     * 调样入库
     */
    @POST("/api/Stock/StockSample/SampleInVerify")
    Observable<ApiResult<String>> sampleInVerify(@Body SampleInverfyInfo sampleInverfyInfo);

    /**
     * 调样入库
     */
    @POST("/api/Stock/StockSample/SampleIn")
    Observable<ApiResult<Object>> sampleIn(@Body List<SampleInverfyInfo> infos);

    /**
     * 单件拣货 获取拣货单列表
     */
    @POST("/Api/Stock/Picking/GetPickList")
    Observable<ApiResult<List<PickGoods>>> getPickList(@Body PickListParam param);

    /**
     * 单件拣货 派单
     */
    @POST("/Api/Stock/Picking/DistributeOrder")
    Observable<ApiResult<Object>> distributeOrder();

    /**
     * 单件拣货 扫描货框 绑定货框
     */
    @GET("/Api/Stock/Picking/ContainerBuild")
    Observable<ApiResult<Object>> containerBuild(@Query("pickId") int pickId, @Query("containerId") int containerId);

    /**
     * 单件拣货 拣货完成
     */
    @GET("/Api/Stock/Picking/PickingComplete")
    Observable<ApiResult<Object>> pickingComplete(@Query("pickId") int pickId);


}
