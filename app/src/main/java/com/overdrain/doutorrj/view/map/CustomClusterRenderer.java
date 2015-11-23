package com.overdrain.doutorrj.view.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.overdrain.doutorrj.model.navigation.MapPointItem;

/**
 * Created by mcardoso on 11/22/15.
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<MapPointItem> {

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MapPointItem> clusterManager) {
        super(context, map, clusterManager);
    }



}
