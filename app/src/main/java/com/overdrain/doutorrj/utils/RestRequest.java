package com.overdrain.doutorrj.utils;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
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
        EstablishmentsList list = null;
        try {
            Resources res = MainActivity.getInstance().getResources();
            String url = res.getString(R.string.api_base_url) + res.getString(R.string.api_establishments);
            String requestData = Unirest.get(url).asString().getBody();
            list = new Gson().fromJson(requestData, EstablishmentsList.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return list;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        List<Establishment> results = ((EstablishmentsList) o).getResults();
        MapFragment.updateMap(results);
    }
}
