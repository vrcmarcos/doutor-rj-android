package com.mcardoso.doutorrj.helper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mcardoso on 12/7/15.
 */
public class RequestHelper extends AsyncTask<String, Void, String> {

    public enum Method {
        GET("GET"), POST("POST");

        public String value;

        Method(String value) {
            this.value = value;
        }
    }

    private static String TAG = "RequestHelper";
    private static int TIMEOUT_IN_MILLIS = 1000 * 5;

    private String url;
    private Method method;
    private RestRequestCallback callback;

    public RequestHelper(String url, Method method, RestRequestCallback callback) {
        this.url = url;
        this.method = method;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(this.method.value);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIS);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                Log.e(TAG, "Failed : HTTP error code : " + conn.getResponseCode());
                this.callback.onRequestFail();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                StringBuilder json = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    json.append(output);
                }

                conn.disconnect();
                this.callback.onRequestSuccess(json.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            this.callback.onRequestFail();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        return null;
    }

    public interface RestRequestCallback {
        void onRequestSuccess(String json);
        void onRequestFail();
    }
}
