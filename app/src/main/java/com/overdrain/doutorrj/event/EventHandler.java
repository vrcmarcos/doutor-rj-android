package com.overdrain.doutorrj.event;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;

/**
 * Created by mcardoso on 6/4/15.
 */
public abstract class EventHandler  {

    private static String TAG = "EventHandler";

    public EventHandler(Event event) {
        EventManager.getInstance().register(this, event);
    }

    public void handle() {
        FragmentManager manager = MainActivity.getInstance().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_container, getFragment());
        transaction.commit();
    }

    public abstract Fragment getFragment();

}
