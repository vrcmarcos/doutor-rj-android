package com.overdrain.doutorrj.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.overdrain.doutorrj.model.rest.Establishment;
import com.overdrain.doutorrj.model.rest.EstablishmentsList;
import com.overdrain.doutorrj.view.fragment.MapFragment;

import java.util.List;

/**
 * Created by mcardoso on 9/15/15.
 */
public class RestRequest extends AsyncTask {

    private static String TAG = "RestRequest";

    @Override
    protected Object doInBackground(Object[] params) {
        String url = "http://192.168.100.101:8000/estabelecimentos.json";
        String requestData = null;
        try {
            requestData = Unirest.get(url).asString().getBody();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        EstablishmentsList list = new Gson().fromJson(requestData, EstablishmentsList.class);
        return list;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        List<Establishment> results = ((EstablishmentsList) o).getResults();
        MapFragment.updateMap(results);
    }
}
