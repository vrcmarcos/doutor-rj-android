package com.mcardoso.doutorrj.controller;

import com.mcardoso.doutorrj.model.EstablishmentsList;

/**
 * Created by mcardoso on 12/16/15.
 */
public class StorageController {
    private static StorageController ourInstance = new StorageController();

    public static StorageController getInstance() {
        return ourInstance;
    }

    private StorageController() {
    }

    private EstablishmentsList establishmentsList;

    public EstablishmentsList getEstablishmentsList() {
        return establishmentsList;
    }

    public void setEstablishmentsList(EstablishmentsList establishmentsList) {
        this.establishmentsList = establishmentsList;
    }


//    public enum Type {
//        ESTABLISHMENTS_LIST(EstablishmentsList.class);
//
//        public Class clazz;
//
//        Type(Class clazz) {
//            this.clazz = clazz;
//        }
//
//        public String getKey() {
//            return this.clazz.getSimpleName();
//        }
//    }
//
//    public void save(Context ctx, Type type, Object object) {
//        String json = GSON.toJson(object);
//        this.getSharedPreferences(ctx).edit().putString(type.getKey(), json).commit();
//    }
//
//    public Object load(Context ctx, Type type) {
//        String savedData = this.getSharedPreferences(ctx).getString(type.getKey(), "");
//        return GSON.fromJson(savedData, type.clazz.getClass());
//    }
//
//    private SharedPreferences getSharedPreferences(Context ctx) {
//        String prefsKey = ctx.getResources().getString(R.string.shared_preferences_key);
//        return ctx.getSharedPreferences(prefsKey, Context.MODE_PRIVATE);
//    }
}
