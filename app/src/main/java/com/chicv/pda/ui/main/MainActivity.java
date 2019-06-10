package com.chicv.pda.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.bottom_view)
    BottomNavigationView bottomNavigationView;

    private List<BaseFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        initView();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(StockFragment.newInstance());
        mFragments.add(OtherFragment.newInstance());
        mFragments.add(MineFragment.newInstance());
    }

    private void initView() {
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.house:
                        navigationFragment(0);
                        break;
                    case R.id.stock:
                        navigationFragment(1);
                        break;
                    case R.id.other:
                        navigationFragment(2);
                        break;
                    case R.id.mine:
                        navigationFragment(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mFragments.get(0)).commit();
    }


    private int mCurrentItemPostion = 0;

    private void navigationFragment(int position) {
        if (mCurrentItemPostion != position) {
            Fragment fragment = mFragments.get(position);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mFragments.get(mCurrentItemPostion));
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.add(R.id.fl_content, fragment);
            }
            fragmentTransaction.commit();
            mCurrentItemPostion = position;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
