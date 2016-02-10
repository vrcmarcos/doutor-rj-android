package com.mcardoso.doutorrj.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mcardoso on 12/7/15.
 */
public class RestRequest extends AsyncTask<String, Void, String> {

    public enum Method {
        GET("GET"), POST("POST");

        public String value;

        Method(String value) {
            this.value = value;
        }
    }

    private static String TAG = "RestRequest";
    private static int TIMEOUT_IN_MILLIS = 1000 * 5;

    private String url;
    private Method method;
    private RestRequestCallback callback;

    public RestRequest(String url, Method method, RestRequestCallback callback) {
        this.url = url;
        this.method = method;
        this.callback = callback;
    }

    public void sync() {
        doRequest();
    }

    @Override
    protected String doInBackground(String... params) {
        doRequest();
        return null;
    }

    private void doRequest() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(this.method.value);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIS);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                String errorMessage = "Failed : HTTP error code : " + conn.getResponseCode();
                Log.e(TAG, errorMessage);
                this.callback.onRequestFail(new Exception(errorMessage));
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
            this.callback.onRequestFail(e);
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public interface RestRequestCallback {
        void onRequestSuccess(String json);
        void onRequestFail(Exception e);
    }
}
