package com.overdrain.doutorrj.event.navigation.handler;

import android.support.v4.app.Fragment;

import com.overdrain.doutorrj.event.Event;
import com.overdrain.doutorrj.event.EventHandler;
import com.overdrain.doutorrj.view.fragment.MapFragment;

/**
 * Created by mcardoso on 6/5/15.
 */
public class MapHandler extends EventHandler {


    public MapHandler(Event event) {
        super(event);
    }

    @Override
    public Fragment getFragment() {
        return new MapFragment();
    }
}
