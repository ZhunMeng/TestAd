package com.duodian.admore.invoice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by duodian on 2017/10/20.
 * Invoice fragment adapter
 */

public class InvoiceFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    public String[] titles;

    public InvoiceFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public InvoiceFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
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
