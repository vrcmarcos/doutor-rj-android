package com.mcardoso.doutorrj.storage;

import android.content.Context;

/**
 * Created by mcardoso on 12/13/15.
 */
public class StorageManager<T> {

    private Context ctx;

    public StorageManager(Context ctx) {
        this.ctx = ctx;
    }

    public T get() {
//        this.ctx.getSharedPreferences(T.toString(), Context.MODE_PRIVATE);
        return null;
    }
}
