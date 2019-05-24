package com.chicv.pda.repository.remote;

import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.bean.PickGoods;
import com.chicv.pda.bean.User;
import com.chicv.pda.bean.param.LoginParam;
import com.chicv.pda.bean.param.PickGoodsParam;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public interface ApiService {


     @POST("/api/Account/Auth/Login")
     Observable<ApiResult<User>> login(@Body LoginParam param);

     @POST("/api/Stock/Picking/Receive")
     Observable<ApiResult<PickGoods>> pickGoods(@Body PickGoodsParam param);

}
