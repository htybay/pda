package com.chicv.pda.repository.remote;


import com.chicv.pda.BuildConfig;
import com.chicv.pda.repository.remote.interceptor.HeadInterceptor;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * OkHttp管理类，可添加缓存，添加公共请求参数
 */
public class OkhttpManager {

    private static OkhttpManager instance = new OkhttpManager();
    private final OkHttpClient.Builder mOkHttpBuilder;

    private static final int CONNECTIMEOUT = 20000;
    private static final int READTIMEOUT = 20000;

    private OkhttpManager() {

        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeadInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));

    }

    public static OkhttpManager getInstance() {
        return instance;
    }

    public OkHttpClient getOKhttp() {
        return mOkHttpBuilder.build();
    }


}