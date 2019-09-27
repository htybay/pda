package com.chicv.pda.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseFragment;
import com.chicv.pda.ui.goods.BatchManyMoveStockActivity;
import com.chicv.pda.ui.goods.BatchMoveStockActivity;
import com.chicv.pda.ui.pickGoods.TransferPickActivity;
import com.chicv.pda.ui.stock.BatchInStockActivity;
import com.chicv.pda.ui.stock.BatchOutStockActivity;
import com.chicv.pda.ui.transferGoods.TransferGoodsAddActivity;
import com.chicv.pda.ui.transferGoods.TransferInStockActivity;
import com.chicv.pda.ui.transferGoods.TransferReceiveActivity;
import com.chicv.pda.ui.transferGoods.TransferSendActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class StockFragment extends BaseFragment {

    private Unbinder bind;

    public static StockFragment newInstance() {

        Bundle args = new Bundle();

        StockFragment fragment = new StockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @OnClick({R.id.text_in_stock, R.id.text_goods_move, R.id.text_transfer_add, R.id.text_transfer_pick,
            R.id.text_transfer_receive, R.id.text_transfer_send, R.id.text_transfer_in_stock,
            R.id.text_out_stock, R.id.text_batch_many_move})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_in_stock:
                startActivity(new Intent(getActivity(), BatchInStockActivity.class));
                break;
            case R.id.text_goods_move:
                startActivity(new Intent(getActivity(), BatchMoveStockActivity.class));
                break;
            case R.id.text_transfer_add:
                startActivity(new Intent(getActivity(), TransferGoodsAddActivity.class));
                break;
            case R.id.text_transfer_pick:
                startActivity(new Intent(getActivity(), TransferPickActivity.class));
                break;
            case R.id.text_transfer_send:
                startActivity(new Intent(getActivity(), TransferSendActivity.class));
                break;
            case R.id.text_transfer_receive:
                startActivity(new Intent(getActivity(), TransferReceiveActivity.class));
                break;
            case R.id.text_transfer_in_stock:
                startActivity(new Intent(getActivity(), TransferInStockActivity.class));
                break;
            case R.id.text_out_stock:
                startActivity(new Intent(getActivity(), BatchOutStockActivity.class));
                break;
            case R.id.text_batch_many_move:
                startActivity(new Intent(getActivity(), BatchManyMoveStockActivity.class));
                break;
        }
    }
}
