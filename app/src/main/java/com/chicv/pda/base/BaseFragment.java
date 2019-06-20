package com.chicv.pda.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class BaseFragment extends RxFragment {

    private static final String STATUS_IS_HIDDEN = "STATUS_IS_HIDDEN";

    private ScannerReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATUS_IS_HIDDEN);
            if (isHidden) {
                getFragmentManager().beginTransaction().hide(this).commit();
            } else {
                getFragmentManager().beginTransaction().show(this).commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATUS_IS_HIDDEN, isHidden());
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
        if (mReceiver == null) {
            mReceiver = new ScannerReceiver();
            mFilter = new IntentFilter(ScanManager.ACTION_DECODE);
        }
        getActivity().registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    private class ScannerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            onReceiveBarcode(new String(barcode));
        }
    }

    protected void onReceiveBarcode(String barcode) {
    }

}
