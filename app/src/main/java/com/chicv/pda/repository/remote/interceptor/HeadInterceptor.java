package com.chicv.pda.repository.remote.interceptor;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.chicv.pda.bean.User;
import com.chicv.pda.utils.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liheyu on 2017/4/21.
 * Email:liheyu999@163.com
 * 表单：application/x-www-form-urlencoded ; charset=UTF-8
 * JSON：application/json; charset=UTF-8
 * .addHeader("Accept-Encoding", "gzip, deflate") OKHTTP 默认可以解压缩，如果加上此句，不解压缩
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        User user = SPUtils.getUser();
        String operateName = user.getName()==null?"":Base64.encodeToString(user.getName().getBytes(), Base64.NO_WRAP);
        String operateId = user.getId()==null?"":user.getId();
        Request original = chain.request();
        Request request = original
                .newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "application/json")
                .addHeader("OperateId", operateId)
                .addHeader("OperateName", operateName)
                .build();
        return chain.proceed(request);
    }
}
