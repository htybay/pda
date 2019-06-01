package com.chicv.pda.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chicv.pda.R;
import com.chicv.pda.ui.deliveryGoods.DeliveryGoodsActivity;
import com.chicv.pda.ui.loseGoods.LoseDeliveryGoodsActivity;
import com.chicv.pda.ui.loseGoods.LosePickGoodsActivity;
import com.chicv.pda.ui.pickgoods.PickgoodsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text_pick_goods, R.id.text_delivery_goods, R.id.text_lose_pick,R.id.text_lose_delivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_pick_goods:
                startActivity(new Intent(this, PickgoodsActivity.class));
                break;
            case R.id.text_delivery_goods:
                startActivity(new Intent(this, DeliveryGoodsActivity.class));
                break;
            case R.id.text_lose_pick:
                startActivity(new Intent(this, LosePickGoodsActivity.class));
                break;
            case R.id.text_lose_delivery:
                startActivity(new Intent(this, LoseDeliveryGoodsActivity.class));
                break;
        }
    }
}
