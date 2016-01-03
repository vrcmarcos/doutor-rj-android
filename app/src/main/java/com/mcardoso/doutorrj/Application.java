package com.mcardoso.doutorrj;

import android.support.multidex.MultiDexApplication;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by mcardoso on 1/2/16.
 */
public class Application extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
