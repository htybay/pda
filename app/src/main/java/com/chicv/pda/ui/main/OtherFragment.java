package com.chicv.pda.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseFragment;
import com.chicv.pda.ui.loseGoods.LoseDeliveryGoodsActivity;
import com.chicv.pda.ui.loseGoods.LosePickGoodsActivity;
import com.chicv.pda.ui.moveRoom.MoveRoomDownActivity;
import com.chicv.pda.ui.moveRoom.MoveRoomUpActivity;
import com.chicv.pda.ui.pickGoods.InternalPickActivity;
import com.chicv.pda.ui.transferGoods.TransferLoseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: liheyu
 * date: 2019-06-03
 * email: liheyu999@163.com
 */
public class OtherFragment extends BaseFragment {

    private Unbinder bind;

    public static OtherFragment newInstance() {

        Bundle args = new Bundle();

        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @OnClick({R.id.text_lose_pick, R.id.text_lose_delivery, R.id.text_internal_pick,
            R.id.text_transfer_lose, R.id.text_move_up, R.id.text_move_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_lose_pick:
                startActivity(new Intent(getActivity(), LosePickGoodsActivity.class));
                break;
            case R.id.text_lose_delivery:
                startActivity(new Intent(getActivity(), LoseDeliveryGoodsActivity.class));
                break;
            case R.id.text_internal_pick:
                startActivity(new Intent(getActivity(), InternalPickActivity.class));
                break;
            case R.id.text_transfer_lose:
                startActivity(new Intent(getActivity(), TransferLoseActivity.class));
                break;
            case R.id.text_move_up:
                startActivity(new Intent(getActivity(), MoveRoomUpActivity.class));
                break;
            case R.id.text_move_down:
                startActivity(new Intent(getActivity(), MoveRoomDownActivity.class));
                break;
        }
    }
}
