package com.mcardoso.doutorrj.helper;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 3/2/16.
 */
public class AnalyticsHelper {

    private static Tracker TRACKER;

    public static void trackScreen(Context ctx, String screenName) {
        Tracker tracker = getTracker(ctx);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void trackAction(Context ctx, String actionName) {
        Tracker tracker = getTracker(ctx);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(actionName)
                .build());
    }

    private static Tracker getTracker(Context ctx) {
        if ( TRACKER == null ) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(ctx);
            TRACKER = analytics.newTracker(R.xml.global_tracker);
        }

        return TRACKER;
    }
}
