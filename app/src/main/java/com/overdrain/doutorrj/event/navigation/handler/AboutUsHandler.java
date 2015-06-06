package com.overdrain.doutorrj.event.navigation.handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.event.Event;
import com.overdrain.doutorrj.event.EventHandler;
import com.overdrain.doutorrj.event.EventManager;
import com.overdrain.doutorrj.view.fragment.AboutUsFragment;

/**
 * Created by mcardoso on 6/5/15.
 */
public class AboutUsHandler extends EventHandler {

    public AboutUsHandler(Event event) {
        super(event);
    }

    @Override
    public Fragment getFragment() {
        return new AboutUsFragment();
    }
}
