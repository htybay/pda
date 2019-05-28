package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.PickGoodsParam;

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

}
