package com.imagination.technologies.ora.app.network;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RestfulRequest {
    private static final String LOG = RestfulRequest.class.getName();
    private static RestfulRequest pRestfulRequest =  new RestfulRequest();

    private RestfulRequest() {
    }

    public static RestfulRequest getSingleton() {
        return pRestfulRequest;
    }

    public String sendGetRequest(String url,  Map<String,
            String> headers) {

        String out = "";

        Headers mHeaders = Headers.of(headers);
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;

        try {
            request = new Request.Builder()
                    .url(url)
                    .headers(mHeaders)
                    .get()
                    .build();

            response = client.newCall(request).execute();

            out = response.body().string();

        } catch (IOException e) {
            Log.i(LOG, "error :: " + e);
        } catch (Exception e) {
            Log.i(LOG, "error :: " + e);
        }

        return out;
    }

    public String sendPostRequest(String url, Map<String, String> data, Map<String,
            String> headers) {

        String out = "";

        FormEncodingBuilder  form = new FormEncodingBuilder();
        OkHttpClient client = new OkHttpClient();
        Iterator it = data.keySet().iterator();
        String key;

        while (it.hasNext()) {
            key = (String)it.next();
            form.add(key, data.get(key));
        }

        Headers mHeaders = Headers.of(headers);
        RequestBody body = form.build();
        Request request;
        Response response;

        try {
            request = new Request.Builder()
                    .url(url)
                    .headers(mHeaders)
                    .post(body)
                    .build();

            response = client.newCall(request).execute();

            out = response.body().string();

        } catch(IOException e) {
            Log.i(LOG, "error :: " + e);
        } catch (Exception e) {
            Log.i(LOG, "error :: " + e);
        }

        return out;
    }

    public String sendPutRequest(String url, Map<String, String> data, Map<String,
            String> headers) {

        String out = "";

        FormEncodingBuilder  form = new FormEncodingBuilder();
        OkHttpClient client = new OkHttpClient();
        Iterator it = data.keySet().iterator();
        String key;

        while (it.hasNext()) {
            key = (String)it.next();
            form.add(key, data.get(key));
        }

        Headers mHeaders = Headers.of(headers);
        RequestBody body = form.build();
        Request request;
        Response response;

        try {
            request = new Request.Builder()
                    .url(url)
                    .headers(mHeaders)
                    .put(body)
                    .build();

            response = client.newCall(request).execute();

            out = response.body().string();

        } catch(IOException e) {
            Log.i(LOG, "error :: " + e);
        } catch (Exception e) {
            Log.i(LOG, "error :: " + e);
        }

        return out;
    }

}
