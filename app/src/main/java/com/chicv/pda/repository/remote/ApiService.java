package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.AddedStockListBean;
import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.ExpressBean;
import com.chicv.pda.bean.GoodsMoveBean;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.bean.InternalPick;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.RecommendStock;
import com.chicv.pda.bean.StockCardingBean;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockLimit;
import com.chicv.pda.bean.StockMoveRoom;
import com.chicv.pda.bean.StockPositionBean;
import com.chicv.pda.bean.StockReceiveBatch;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.bean.TransferBathBean;
import com.chicv.pda.bean.TransferIn;
import com.chicv.pda.bean.TransferPick;
import com.chicv.pda.bean.UpdateInfo;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.BatchInStockParam;
import com.chicv.pda.bean.param.BatchMoveStockParam;
import com.chicv.pda.bean.param.InStockParam;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.MoveRoomDownParam;
import com.chicv.pda.bean.param.MoveRoomUpParam;
import com.chicv.pda.bean.param.OutStockParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.bean.param.PickInternalGoodsParam;
import com.chicv.pda.bean.param.RecommendStockParam;
import com.chicv.pda.bean.param.SampleInReturnParam;
import com.chicv.pda.bean.param.SampleInStockParam;
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
}
