package com.chicv.pda.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseActivity;
import com.chicv.pda.base.BaseFragment;
import com.chicv.pda.utils.CommonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public static final String[] TABS = {"home", "stock", "other", "mine"};

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.bottom_view)
    BottomNavigationView bottomNavigationView;

    private List<BaseFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("细刻科技",false);
        ButterKnife.bind(this);
        initFragments(savedInstanceState);
        initView();
    }

    private void initFragments(Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        HomeFragment homeFragment = null;
        StockFragment stockFragment = null;
        OtherFragment otherFragment = null;
        MineFragment mineFragment = null;
        if (savedInstanceState != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            homeFragment = (HomeFragment) supportFragmentManager.findFragmentByTag(TABS[0]);
            stockFragment = (StockFragment) supportFragmentManager.findFragmentByTag(TABS[1]);
            otherFragment = (OtherFragment) supportFragmentManager.findFragmentByTag(TABS[2]);
            mineFragment = (MineFragment) supportFragmentManager.findFragmentByTag(TABS[3]);
        }
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        if (stockFragment == null) {
            stockFragment = StockFragment.newInstance();
        }
        if (otherFragment == null) {
            otherFragment = OtherFragment.newInstance();
        }
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
        }
        mFragments.add(homeFragment);
        mFragments.add(stockFragment);
        mFragments.add(otherFragment);
        mFragments.add(mineFragment);
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
        if (getSupportFragmentManager().getFragments().size() == 0) {
            navigationFragment(0);
        }
    }

    private void navigationFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            BaseFragment fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
            if (i == position) {
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.fl_content, fragment, TABS[position]);
                }
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            CommonUtils.check();
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
