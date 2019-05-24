package com.chicv.pda.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.chicv.pda.base.BaseApplication;
import com.chicv.pda.bean.User;
import com.google.gson.Gson;

import io.reactivex.annotations.Nullable;


public class SPUtils {

    private static final String SP_FILE_NAME = "appConfig";
    private static final String KEY_USER = "key_user";

    public static SharedPreferences getSP() {
        return BaseApplication.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean putString(String key, String value) {
        return getSP().edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return getSP().getString(key, "");
    }

    public static int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    public static boolean putInt(String key, int value) {
        return getSP().edit().putInt(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public static boolean putBoolean(String key, boolean value) {
        return getSP().edit().putBoolean(key, value).commit();
    }

    public static boolean saveUser(@Nullable User user) {
        return getSP().edit().putString(KEY_USER, new Gson().toJson(user)).commit();
    }

    public static User getUser() {
        User user = null;
        if (!TextUtils.isEmpty(getString(KEY_USER))) {
            user = new Gson().fromJson(getString(KEY_USER), User.class);
        }
        if (user == null) {
            user = new User();
        }
        return user;
    }

    ;
}
