package com.chicv.pda.repository;

import com.chicv.pda.base.Constant;
import com.chicv.pda.repository.remote.ApiService;
import com.chicv.pda.repository.remote.OkhttpManager;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liheyu on 2017/3/3.
 * Email:liheyu999@163.com
 */


public class HttpManager {

    private static  HttpManager INSTANCE ;
    private Retrofit mRetrofit;

    private HttpManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_ADDRESS)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkhttpManager.getInstance().getOKhttp())
                .build();
    }

    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }

}
