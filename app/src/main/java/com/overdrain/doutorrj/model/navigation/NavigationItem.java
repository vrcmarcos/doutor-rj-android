package com.overdrain.doutorrj.model.navigation;

import com.overdrain.doutorrj.event.navigation.NavigationEvent;

/**
 * Created by mcardoso on 6/4/15.
 */
public enum NavigationItem {

    FACEBOOK_LOGIN(1, NavigationEvent.FACEBOOK_LOGIN);

    private int id;
    private NavigationEvent event;

    NavigationItem(int id, NavigationEvent event) {
        this.id = id;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public static NavigationItem findById(int identifier) {
        NavigationItem result = null;

        for( NavigationItem item : NavigationItem.values() ) {
            if (identifier == item.getId() ) {
                result = item;
                break;
            }
        }

        return result;
    }

    public NavigationEvent getEvent() {
        return event;
    }
}
