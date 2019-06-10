package com.chicv.pda.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class BaseFragment extends RxFragment {

    private ScannerReceiver mReceiver;
    private IntentFilter mfilter;


    @Override
    public void onResume() {
        super.onResume();
        if (mReceiver == null) {
            mReceiver = new ScannerReceiver();
            mfilter = new IntentFilter(ScanManager.ACTION_DECODE);
        }
        getActivity().registerReceiver(mReceiver, mfilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mReceiver != null) {
            getActivity(). unregisterReceiver(mReceiver);
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
