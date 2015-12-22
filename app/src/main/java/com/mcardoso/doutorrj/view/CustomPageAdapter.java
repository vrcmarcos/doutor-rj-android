package com.mcardoso.doutorrj.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 12/10/15.
 */
public class CustomPageAdapter extends FragmentPagerAdapter {

    private Context ctx;

    public CustomPageAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch(position) {
            case 1:
                fragment = new ListFragment();
                break;
            default:
                fragment = new BestChoiceFragment();
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
        int resourceId;

        switch (position) {
            case 1:
                resourceId = R.string.list_title;
                break;
            default:
                resourceId = R.string.best_choice_title;
                break;
        }
        return ctx.getResources().getString(resourceId);
    }
}
