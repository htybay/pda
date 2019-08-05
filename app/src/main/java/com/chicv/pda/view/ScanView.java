package com.chicv.pda.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chicv.pda.R;
import com.chicv.pda.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import zxing.activity.CaptureActivity;

/**
 * Created  on 2017/12/29 14:58
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class ScanView extends LinearLayout implements View.OnClickListener {

    private final Context mAppContext;

    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAppContext = context.getApplicationContext();
        initView();
    }

    private void initView() {
        LayoutInflater inflate = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflate.inflate(R.layout.view_edit_scan, this);
        ImageView imgScan = (ImageView) rootView.findViewById(R.id.img_scan);
        imgScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==R.id.img_scan){
            beginScan();
        }
    }

    private void beginScan() {
        final Activity activity = scanForActivity(getContext());
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            openZxing(activity);
        }else {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if(aBoolean){
                        openZxing(activity);
                    }else {
                        ToastUtils.showString("无权限！");
                    }
                }
            });
        }
    }

    private void openZxing(Activity activity) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        intent.putExtra(CaptureActivity.START_ACTIVITY_NAME,activity.getClass().getSimpleName());
        activity.startActivity(intent);
    }

    public Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity)context;
        } else {
            return context instanceof ContextWrapper ? scanForActivity(((ContextWrapper)context).getBaseContext()) : null;
        }
    }
}
