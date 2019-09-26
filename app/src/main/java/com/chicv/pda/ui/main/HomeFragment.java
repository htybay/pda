package com.chicv.pda.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseFragment;
import com.chicv.pda.ui.deliveryGoods.DeliveryGoodsActivity;

import com.chicv.pda.ui.goods.GoodsMoveStockActivity;
import com.chicv.pda.ui.goods.GoodsStockActivity;
import com.chicv.pda.ui.goods.ReceiveGoodsActivity;
import com.chicv.pda.ui.pickGoods.PickgoodsActivity;
import com.chicv.pda.ui.stock.GoodsOutStockActivity;
import com.chicv.pda.ui.stock.HandleStockActivity;
import com.chicv.pda.ui.stock.TypeInSockActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class HomeFragment extends BaseFragment {

    private Unbinder bind;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @OnClick({R.id.text_receive_goods, R.id.text_pick_goods, R.id.text_delivery_goods, R.id.text_in_stock,
            R.id.text_goods_stock, R.id.text_goods_change_stock, R.id.text_handle_stock, R.id.text_out_stock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_receive_goods:
                startActivity(new Intent(getActivity(), ReceiveGoodsActivity.class));
                break;
            case R.id.text_in_stock:
                startActivity(new Intent(getActivity(), TypeInSockActivity.class));
                break;
            case R.id.text_pick_goods:
                startActivity(new Intent(getActivity(), PickgoodsActivity.class));
                break;
            case R.id.text_delivery_goods:
                startActivity(new Intent(getActivity(), DeliveryGoodsActivity.class));
                break;
            case R.id.text_goods_stock:
                startActivity(new Intent(getActivity(), GoodsStockActivity.class));
                break;
            case R.id.text_goods_change_stock:
                startActivity(new Intent(getActivity(), GoodsMoveStockActivity.class));
                break;
            case R.id.text_handle_stock:
                startActivity(new Intent(getActivity(), HandleStockActivity.class));
                break;
            case R.id.text_out_stock:
                startActivity(new Intent(getActivity(), GoodsOutStockActivity.class));
                break;
        }
    }
}
