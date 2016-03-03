package com.mcardoso.doutorrj.helper;

/**
 * Created by mcardoso on 3/2/16.
 */
public class AnalyticsHelper {

    private static final String PROPERTY_ID = "UA-35950922-4";

    public static int GENERAL_TRACKER = 0;

    public static HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public static synchronized Tracker getTracker(TrackerName trackerId, Activity context) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.string.ga_trackingId) : analytics.newTracker(R.string.ga_trackingId);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public static void sendScreen(String screenName, Activity activity) {
        ConnectivityHelper helper = new ConnectivityHelper(activity);
        //gnt.globo.com.receitas.utils.StringHelper stringHelper = new gnt.globo.com.receitas.utils.StringHelper();

        if (helper.isConnected()) {
            Tracker t = getTracker(TrackerName.APP_TRACKER, activity);
            screenName = StringHelper.toSlug(screenName);
            t.setScreenName(screenName);
            t.send(new HitBuilders.AppViewBuilder().build());

        }
    }

    public static void sendEvent(String category, String action, String label, Activity activity) {
        ConnectivityHelper helper = new ConnectivityHelper(activity);
        if (helper.isConnected()) {
            // Get tracker.
            Tracker t = getTracker(TrackerName.APP_TRACKER, activity);
            // Build and send an Event.
            label = StringHelper.toSlug(label);
            Log.d("EasyTrackerHelper.sendEvent.label", label);
            t.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
        }
    }
}
