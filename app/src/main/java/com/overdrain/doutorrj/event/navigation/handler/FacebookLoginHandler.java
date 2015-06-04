package com.overdrain.doutorrj.event.navigation.handler;

import android.util.Log;

import com.overdrain.doutorrj.event.Event;
import com.overdrain.doutorrj.event.EventHandler;

/**
 * Created by mcardoso on 6/4/15.
 */
public class FacebookLoginHandler extends EventHandler {

    public FacebookLoginHandler(Event event) {
        super(event);
    }

    @Override
    protected void handle() {
        Log.i("TAG","Login FB!");
    }
}
