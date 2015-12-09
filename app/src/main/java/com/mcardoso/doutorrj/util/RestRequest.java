package com.mcardoso.doutorrj.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;

import java.lang.reflect.Method;

/**
 * Created by mcardoso on 12/7/15.
 */
public class RestRequest<T> extends AsyncTask {

    private static String TAG = "RestRequest";

    private String url;
    private Class<T> returnType;
    private Object receiver;
    private String callbackMethod;

    public RestRequest(String url, Class<T> returnType, Object receiver, String callbackMethod) {
        this.url = url;
        this.returnType = returnType;
        this.receiver = receiver;
        this.callbackMethod = callbackMethod;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Object result = null;
        try {
            result = Unirest.get(this.url).asString().getBody();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        T object = new Gson().fromJson((String) o, this.returnType);
        try {
            Method callback = null;
            for (Method method:this.receiver.getClass().getMethods()) {
                if (method.getName().equals(this.callbackMethod)) {
                    callback = method;
                    break;
                }
            }
            if (callback != null) {
                callback.invoke(this.receiver, object);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
