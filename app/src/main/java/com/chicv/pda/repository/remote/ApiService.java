package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.ExpressBean;
import com.chicv.pda.bean.RecommendStock;
import com.chicv.pda.bean.LocationGoods;
import com.chicv.pda.bean.LoseGoods;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.InStockParam;
import com.chicv.pda.bean.param.RecommendStockParam;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.OutStockParam;
import com.chicv.pda.bean.param.PickGoodsParam;

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
     * 根据物品ID获取建议的货位信息
     */
    @POST("/api/Stock/Goods/GetInVerifiInfo")
    Observable<ApiResult<RecommendStock>> getRecommendStockInfo(@Body RecommendStockParam param);

    /**
     * 入库
     */
    @POST("/api/Stock/Goods/GoodsIn")
    Observable<ApiResult<Object>> inStock(@Body InStockParam param);

    /**
     * 根据货位ID获取库存信息及物品
     */
    @GET("/api/Stock/Goods/GetLocationGoods")
    Observable<ApiResult<LocationGoods>> getLocationGoods(@Query("gridId") long gridId);

    /**
     * 获取库存记录
     */
    @GET("/Api/Stock/Goods/GetInRecord")
    Observable<ApiResult<List<StockRecord>>> getInRecord(@Query("gridId") long gridId);


    /**
     * 获取库存记录
     */
    @GET("/Api/Stock/Goods/GetOutRecord")
    Observable<ApiResult<List<StockRecord>>> getOutRecord(@Query("gridId") long gridId);

    /**
     * 根据快递单号获取收货信息
     */
//    @GET("/api/stock/GetReceiveByExpressNo")
    @GET("/Api/Purchase/Goods/GetReceiveByExpressNo")
    Observable<ApiResult<List<ExpressBean>>> getReceiveByExpressNo(@Query("expressNo") String expressNo);

    /**
     * 更新签收信息
     */
    @POST("/api/stock/UpdateReceiveByPDASign")
    Observable<ApiResult<Object>> expressSign(@Body ExpressBean bean);

}
