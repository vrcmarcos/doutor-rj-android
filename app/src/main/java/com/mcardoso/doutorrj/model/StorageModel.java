package com.mcardoso.doutorrj.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 12/13/15.
 */
public abstract class StorageModel {

    private static String TAG = "StorageModel";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        String prefsKey = ctx.getResources().getString(R.string.shared_preferences_key);
        return ctx.getSharedPreferences(prefsKey, Context.MODE_PRIVATE);
    }

    public void save(Context ctx){
        String json = new Gson().toJson(this);
        Log.d(TAG, "Saving " + json);
        String key = this.getClass().getSimpleName();
        getSharedPreferences(ctx).edit().putString(key, json).commit();
    }

    public static Object load(Context ctx, Class clazz){
        String key = clazz.getSimpleName();
        String savedData = getSharedPreferences(ctx).getString(key, null);
        return new Gson().fromJson(savedData, clazz);
    }
}
