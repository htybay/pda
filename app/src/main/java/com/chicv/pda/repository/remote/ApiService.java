package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.ExpressBean;
import com.chicv.pda.bean.GoodsMoveBean;
import com.chicv.pda.bean.GoodsStock;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.PurchaseBatch;
import com.chicv.pda.bean.AddedStockListBean;
import com.chicv.pda.bean.RecommendStock;
import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockMoveBean;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.InStockParam;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.OutStockParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.chicv.pda.bean.param.RecommendStockParam;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {


    //登陆
    @POST("api/Account/Auth/Login")
    Observable<ApiResult<User>> login(@Body LoginParam param);

    //获取拣货信息
    @GET("api/Stock/Picking/GetStockPick")
    Observable<ApiResult<PickGoods>> getPickGoodsInfo(@Query("pickId") String pickId);

    //拣货，扫描到货物时上传数据
    @POST("/api/Stock/Picking/Picking")
    Observable<ApiResult<Object>> pickGoods(@Body PickGoodsParam pickId);

    //领取捡货单
    @POST("/api/Stock/Picking/Receive")
    Observable<ApiResult<Object>> receivePickGoods(@Body PickGoodsParam pickParam);

    //更改捡货单
    @POST("api/Stock/Picking/ReceiveChange")
    Observable<ApiResult<Object>> changePickGoods(@Body PickGoodsParam pickParam);

    //配货
    @POST("/api/Stock/Picking/Distribution")
    Observable<ApiResult<Object>> deliveryGoods(@Body PickGoodsParam pickParam);

    //出库
    @POST("/api/Stock/Picking/DistributionOut")
    Observable<ApiResult<Object>> outStock(@Body OutStockParam outStockParam);

    //获取拣货丢失物品集合
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
    Observable<ApiResult<List<StockRecord>>> getInRecord(@Query("gridId") long gridId);


    /**
     * 分类入库---获取库存记录
     */
    @GET("/Api/Stock/Goods/GetOutRecord")
    Observable<ApiResult<List<StockRecord>>> getOutRecord(@Query("gridId") long gridId);

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
     * 物品移位---根据货位号获取货位信息
     */
    @GET("Api/Stock/Location/GetPositionByGridId")
    Observable<ApiResult<StockMoveBean>> getStockMoveInfoByGridId(@Query("id") int id);

    /**
     * 货位信息查询---根据货位号获取货位信息
     */
    @GET("Api/Stock/Location/GetGridById")
    Observable<ApiResult<StockInfo>> getStockInfoByStockId(@Query("id") int id);

    /**
     * 囤货入库---根据批次号获取信息
     */
    @GET("Api/Stock/Order/ReceiveBatchIsExists")
    Observable<ApiResult<PurchaseBatch>> getPurchaseBatch(@Query("batchCode") String batchCode);

    /**
     * 囤货入库---根据批次号获取已上架货位信息
     */
    @GET("Api/Stock/Order/GetAddedStockList")
    Observable<ApiResult<AddedStockListBean>> getAddedStockList(@Query("batchCode") String batchCode);

}
