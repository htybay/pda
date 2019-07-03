package com.chicv.pda.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chicv.pda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateDialog extends Dialog {

    @BindView(R.id.text_progress)
    TextView textProgress;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    public UpdateDialog(Activity context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        ButterKnife.bind(this);
        initConfig();
    }

    private void initConfig() {
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = getWindow();
        WindowManager windowManager = window.getWindowManager();
        WindowManager.LayoutParams attributes = window.getAttributes();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        attributes.width = (int) (defaultDisplay.getWidth() * 0.8);
        window.setBackgroundDrawableResource(R.drawable.shape_corner_white);
        window.setAttributes(attributes);
    }

    public void setProgress(int progress) {
        pbProgress.setProgress(progress);
    }

    public void setMaxProgress(int totalProgress){
        pbProgress.setMax(totalProgress);
    }

    public void setProgressDetial(String text){
        textProgress.setText(text);
    }
}
