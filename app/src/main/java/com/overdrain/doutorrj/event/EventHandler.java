package com.overdrain.doutorrj.event;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.view.fragment.AboutUsFragment;

/**
 * Created by mcardoso on 6/4/15.
 */
public abstract class EventHandler  {

    private static String TAG = "EventHandler";

    public EventHandler(Event event) {
        EventManager.getInstance().register(this, event);
    }

    public void handle() {
        FragmentTransaction transaction = MainActivity.getInstance().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, getFragment() );
        transaction.commit();
    }

    public abstract Fragment getFragment();

}
