package com.duodian.admore.main.admore.resourcevoucher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by duodian on 2017/10/20.
 * voucher fragment adapter
 */

public class VoucherFragmentAdapter extends FragmentStatePagerAdapter {

    private List<VoucherFragment> fragmentList;
    public String[] titles;

    public VoucherFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public VoucherFragmentAdapter(FragmentManager fm, List<VoucherFragment> fragmentList, String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
