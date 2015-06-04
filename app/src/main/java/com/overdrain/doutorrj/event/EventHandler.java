package com.overdrain.doutorrj.event;

/**
 * Created by mcardoso on 6/4/15.
 */
public abstract class EventHandler {

    public EventHandler(Event event) {
        EventManager.getInstance().register(this, event);
    }

    protected abstract void handle();
}
