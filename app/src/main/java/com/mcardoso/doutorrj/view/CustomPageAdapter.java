package com.mcardoso.doutorrj.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mcardoso on 12/10/15.
 */
public class CustomPageAdapter extends FragmentPagerAdapter {

    public CustomPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch(position) {
            case 1:
                fragment = new FavoriteFragment();
                break;
            default:
                fragment = new ListFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section "+(position+1);
    }
}
