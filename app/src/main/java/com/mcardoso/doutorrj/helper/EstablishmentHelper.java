package com.mcardoso.doutorrj.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.establishment.EstablishmentType;
import com.mcardoso.doutorrj.response.EstablishmentsPerTypeResponse;
import com.mcardoso.doutorrj.view.NotifiableFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mcardoso on 12/23/15.
 */
public class EstablishmentHelper {

    private static String TAG = "EstablishmentHelper";

    private Gson gson;
    private Context ctx;

    public EstablishmentHelper(Context ctx) {
        this.ctx = ctx;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(EstablishmentType.class, new EstablishmentTypeDeserializer())
                .create();
        NotifiableFragment.broadcastEstablishmentsPerTypeResponse(this.getCachedResponse());
//        this.update();
    }

    private EstablishmentsPerTypeResponse getCachedResponse() {
        Resources res = this.ctx.getResources();
        String key = res.getString(R.string.shared_preferences_key);
        SharedPreferences prefs = this.ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        String establishmentsKey = res.getString(R.string.shared_preferences_establishments);
        String json = prefs.getString(establishmentsKey, null);
        if ( json == null ) {
            try {
                json = readFromFile();
                this.setCachedResponse(prefs, establishmentsKey, json);
            } catch (IOException io) {
                Log.e(TAG, io.getMessage(), io);
            }
        }

        return this.gson.fromJson(json, EstablishmentsPerTypeResponse.class);
    }

    private void setCachedResponse( String json ) {
        Resources res = this.ctx.getResources();
        String key = res.getString(R.string.shared_preferences_key);
        SharedPreferences prefs = this.ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        String establishmentsKey = res.getString(R.string.shared_preferences_establishments);
        this.setCachedResponse(prefs, establishmentsKey, json);
    }

    private void setCachedResponse(SharedPreferences prefs, String key, String json) {
        prefs.edit().putString(key, json).apply();
    }

    private String readFromFile() throws IOException {
        InputStream is = this.ctx.getResources().openRawResource(R.raw.establishments);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String output;
        StringBuilder json = new StringBuilder();
        while ((output = br.readLine()) != null) {
            json.append(output);
        }

        return json.toString();
    }

    private void update() {
        String url = this.ctx.getResources().getString(R.string.api_establishments_per_type);
        new RequestHelper(url, RequestHelper.Method.GET, new RequestHelper.RestRequestCallback() {
            @Override
            public void onRequestSuccess(String json) {
                setCachedResponse(json);
                NotifiableFragment.updateEstablishmentsPerTypeResponse(
                        gson.fromJson(json, EstablishmentsPerTypeResponse.class));
            }

            @Override
            public void onRequestFail() {
                Log.e(TAG, "Request update fail");
            }
        }).execute();
    }

    public static void sort(List<Establishment> establishmentList, LatLng latLng) {
        final Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        Collections.sort(establishmentList, new Comparator<Establishment>() {
            @Override
            public int compare(Establishment lhs, Establishment rhs) {
                Location location1 = new Location("");
                location1.setLongitude(lhs.getLongitude());
                location1.setLatitude(lhs.getLatitude());
                Float distance1 = location.distanceTo(location1);

                Location location2 = new Location("");
                location2.setLongitude(rhs.getLongitude());
                location2.setLatitude(rhs.getLatitude());
                Float distance2 = location.distanceTo(location2);

                return Math.round(distance1 - distance2);
            }
        });
    }

    class EstablishmentTypeDeserializer implements JsonDeserializer<EstablishmentType> {
        @Override
        public EstablishmentType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            EstablishmentType result = null;
            for(EstablishmentType type : EstablishmentType.values()) {
                if (type.getName().equals(json.getAsString())) {
                    result = type;
                    break;
                }
            }
            return result;
        }
    }

}
