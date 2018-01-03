package com.duodian.admore.monitor.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.duodian.admore.main.BaseFragment;

import java.util.List;

/**
 * Created by duodian on 2017/12/25.
 * MonitorFragmentAdapter
 */

public class MonitorFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public MonitorFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
