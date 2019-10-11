package com.chicv.pda.ui.moveRoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: liheyu
 * date: 2019-10-10
 * email: liheyu999@163.com
 */
public class MoveRoomManagerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_manager);
        ButterKnife.bind(this);
        initToolbar("移库管理");
    }

    @OnClick({R.id.text_move_up, R.id.text_move_down, R.id.text_move_lose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_move_up:
                startActivity(new Intent(this, MoveRoomUpActivity.class));
                break;
            case R.id.text_move_down:
                startActivity(new Intent(this, MoveRoomDownActivity.class));
                break;
            case R.id.text_move_lose:
                startActivity(new Intent(this, MoveRoomLoseActivity.class));
                break;
        }
    }
}
