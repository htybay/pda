package com.chicv.pda.base;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chicv.pda.BuildConfig;
import com.chicv.pda.repository.remote.exception.AppCrashException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Liheyu on 2017/8/14.
 * Email:liheyu999@163.com
 */

public class BaseApplication extends Application {

    private List<BaseActivity> activitys = new LinkedList<>();
    private List<Service> services = new LinkedList<>();

    private static BaseApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        AppCrashException.init();
        initLeank();
        initLogger();
    }

    private void initLeank() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(2)
                .methodOffset(5)
                .logStrategy(null)
                .tag("pda")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    public BaseActivity getCurrentActivity() {
        if (activitys.size() == 0) {
            return null;
        }
        return activitys.get(activitys.size() - 1);
    }

    public void addActivity(BaseActivity activity) {
        activitys.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        activitys.remove(activity);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }

    public void closeApplication() {
        closeActivity();
        closeService();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void closeService() {
        ListIterator<Service> listIterator = services.listIterator();
        while (listIterator.hasNext()) {
            Service service = listIterator.next();
            if (service != null) {
                stopService(new Intent(this, service.getClass()));
            }
        }
    }

    public void closeActivity() {
        ListIterator<BaseActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            BaseActivity activity = listIterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void finishOtherActivity(BaseActivity nowAct) {
        ListIterator<BaseActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            BaseActivity activity = listIterator.next();
            if (activity != null && activity != nowAct) {
                activity.finish();
            }
        }
    }

    public void finishTheActivity(Class<? extends BaseActivity> nowAct) {
        ListIterator<BaseActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            BaseActivity activity = listIterator.next();
            if (activity != null && TextUtils.equals(activity.getClass().getName(), nowAct.getName())) {
                activity.finish();
            }
        }
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }
}