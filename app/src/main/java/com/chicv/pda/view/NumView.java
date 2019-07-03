package com.chicv.pda.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chicv.pda.R;

/**
 * author: liheyu
 * date: 2019-07-03
 * email: liheyu999@163.com
 */
public class NumView extends LinearLayout implements View.OnClickListener {

    private EditText editNum;

    public NumView(Context context) {
        this(context, null);
    }

    public NumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflate.inflate(R.layout.view_num, this);
        rootView.findViewById(R.id.ib_sub).setOnClickListener(this);
        rootView.findViewById(R.id.ib_add).setOnClickListener(this);
        editNum = rootView.findViewById(R.id.edit_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_sub:
                int value = getNumValue();
                value--;
                if (value < 0) {
                    value = 0;
                }
                editNum.setText(String.valueOf(value));
                break;
            case R.id.ib_add:
                int addValue = getNumValue();
                addValue++;
                editNum.setText(String.valueOf(addValue));
                break;
        }
    }

    public int getNumValue() {
        String content = editNum.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return 0;
        }
        int i = 0;
        try {
            i = Integer.parseInt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public void setText(@NonNull String text) {
        editNum.setText(text);
    }
}
