package com.overdrain.doutorrj.event.navigation.handler;

import android.support.v4.app.Fragment;

import com.overdrain.doutorrj.event.Event;
import com.overdrain.doutorrj.event.EventHandler;
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
