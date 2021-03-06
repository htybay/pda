package com.chicv.pda.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chicv.pda.R;
import com.chicv.pda.repository.HttpManager;
import com.chicv.pda.repository.remote.ApiService;
import com.chicv.pda.utils.BarcodeUtils;
import com.chicv.pda.utils.CommonUtils;
import com.chicv.pda.utils.StatusBarUtil;
import com.chicv.pda.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import zxing.activity.CaptureActivity;

/**
 * Created  on 2019/1/18 10:48
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class BaseActivity extends RxAppCompatActivity {

    public static final String PDA_ACTION = "com.qs.scancode";
    public static final String PDA_KEY = "data";

    private static Handler handler;
    protected ApiService apiService;
    private ScannerReceiver mReceiver;
    private IntentFilter mfilter;
    private ZxingReceiver mZxingReceiver;
    private IntentFilter mZxingFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeInstanceState(savedInstanceState);
        BaseApplication.getInstance().addActivity(this);
        apiService = HttpManager.getInstance().getApiService();
        if (mZxingReceiver == null) {
            mZxingReceiver = new ZxingReceiver();
            mZxingFilter = new IntentFilter(CaptureActivity.ZXING_PDA_ACTION);
        }
        registerReceiver(mZxingReceiver, mZxingFilter);
    }

    private void removeInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "Android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (mReceiver == null) {
            mReceiver = new ScannerReceiver();
            mfilter = new IntentFilter();
            mfilter.addAction(ScanManager.ACTION_DECODE);
            mfilter.addAction(PDA_ACTION);
        }
        registerReceiver(mReceiver, mfilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
        if (mZxingReceiver != null) {
           unregisterReceiver(mZxingReceiver);
        }
    }

    protected void initToolbar(String title) {
        initToolbarConfig(title, true);
    }

    protected void initToolbar(String title, boolean showHomeUp) {
        initToolbarConfig(title, showHomeUp);
    }

    private void initToolbarConfig(String title, boolean showHomeUp) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        TextView textTitle = findViewById(R.id.text_title);
        textTitle.setText(title);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeUp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void initEditBarcode() {
        EditText editBarocde = findViewById(R.id.edit_barcode);
        if (editBarocde == null) {
            return;
        }
        editBarocde.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = CommonUtils.getString(v);
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showString("条码不能为空");
                    } else {
                        showKeyboard(false);
                        onReceivedCode(content);
                    }
                }
                return false;
            }
        });
    }

    public void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
        initEditBarcode();
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    public class ScannerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String barcode = "";
            if (TextUtils.equals(intent.getAction(), PDA_ACTION)) {
                barcode = intent.getStringExtra(PDA_KEY);
            } else if (TextUtils.equals(intent.getAction(), ScanManager.ACTION_DECODE)) {
                barcode = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG);
            }

            if (!TextUtils.isEmpty(barcode)) {
                onReceivedCode(barcode.trim());
            }
        }
    }

    public class ZxingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra(CaptureActivity.ZXING_PDA_DATA_KEY);
            String activityName = intent.getStringExtra(CaptureActivity.START_ACTIVITY_NAME);
            if(TextUtils.equals(activityName,BaseActivity.this.getClass().getSimpleName())){
                onReceivedCode(stringExtra);
            }
        }
    }

    private void onReceivedCode(String barcode){
        Logger.d("onReceivedCode："+barcode);
        if (BarcodeUtils.isQRCode(barcode)) {
            // 如果扫到的是二维码，提取出物品号或囤货规格
            barcode = BarcodeUtils.getWpOrBatchCodeFromQr(barcode);
        }else if (BarcodeUtils.isQRNewCode(barcode)){
            barcode = BarcodeUtils.getWpOrBatchCodeFromNewQr(barcode);
        }
        onReceiveBarcode(barcode);
    }

    protected void onReceiveBarcode(String barcode) {
    }
}
