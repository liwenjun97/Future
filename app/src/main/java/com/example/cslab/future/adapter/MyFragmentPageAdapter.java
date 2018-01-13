package com.example.cslab.future.adapter;

/**
 * Created by CSLab on 2017/7/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPageAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentlist;

    public MyFragmentPageAdapter(FragmentManager fm,List<Fragment> fragmentlist){
        super(fm);
        this.fragmentlist=fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }
}
