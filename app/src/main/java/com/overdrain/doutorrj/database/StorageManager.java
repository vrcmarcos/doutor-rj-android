package com.overdrain.doutorrj.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.overdrain.doutorrj.MainActivity;

/**
 * Created by mcardoso on 6/11/15.
 */
public class StorageManager {

    private static String SHARED_PREFS_NAME = "DoutorRJ-SharedPrefs";
    private static SharedPreferences SHARED_PREFS;

    private static StorageManager ourInstance = new StorageManager();

    public static StorageManager getInstance() {
        if ( SHARED_PREFS == null ) {
            SHARED_PREFS = MainActivity.getInstance().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        }
        return ourInstance;
    }

    private StorageManager() {
    }

    public Integer getInteger(String key) {
        return getInteger(key, 0);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return SHARED_PREFS.getInt(key, defaultValue);
    }

    public void setInteger(String key, Integer value ) {
        SHARED_PREFS.edit().putInt(key, value).commit();
    }

    public Float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public Float getFloat(String key, Float defaultValue) {
        return SHARED_PREFS.getFloat(key, defaultValue);
    }

    public void setFloat(String key, Float value ) {
        SHARED_PREFS.edit().putFloat(key, value).commit();
    }

}
