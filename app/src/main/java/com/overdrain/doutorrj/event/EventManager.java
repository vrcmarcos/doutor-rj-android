package com.overdrain.doutorrj.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mcardoso on 6/4/15.
 */
public class EventManager {

    private static EventManager INSTANCE = new EventManager();
    private static Map<Event, List<EventHandler>> EVENTS_MAP = new HashMap<Event, List<EventHandler>>();

    public static EventManager getInstance() {
        return INSTANCE;
    }

    private EventManager() {
    }

    public void register(EventHandler handler, Event event) {
        List<EventHandler> handlers;
        if( EVENTS_MAP.containsKey(event) ) {
            handlers = EVENTS_MAP.get(event);
        } else {
            handlers = new ArrayList<EventHandler>();
        }

        handlers.add(handler);

        EVENTS_MAP.put(event, handlers);
    }

    public void dispatch(Event event) {
        if( EVENTS_MAP.containsKey(event) ) {
            List<EventHandler> handlers = EVENTS_MAP.get(event);
            for( EventHandler handler : handlers ) {
                handler.handle();
            }
        }
    }
}
