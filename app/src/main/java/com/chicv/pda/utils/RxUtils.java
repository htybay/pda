package com.chicv.pda.utils;

import com.chicv.pda.base.Constant;
import com.chicv.pda.bean.ApiResult;
import com.chicv.pda.repository.remote.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lihy on 2018/6/28 14:55
 * E-Mail ：liheyu999@163.com
 */
public class RxUtils {

    public static <T> Observable<T> wrapHttp(Observable<ApiResult<T>> observable) {
        return observable.map(new Function<ApiResult<T>, T>() {
            @Override
            public T apply(ApiResult<T> tApiResult) throws Exception {
                if (!tApiResult.isSuccess()) {
                    if (tApiResult.getMessage() != null && tApiResult.getMessage().contains("无权访问")) {
                        throw new ApiException("登陆超时，请重新登陆");
                    } else {
                        throw new ApiException(Constant.TOAST_PREFIX + tApiResult.getMessage());
                    }
                }
                if (tApiResult.getData() == null) {
                    tApiResult.setData((T) new Object());
                }
                return tApiResult.getData();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> wrapUI(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
