package io.krito.com.reze.helper;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class VolleyCustomRequest extends Request {

    private Response.Listener listener;
    private Gson gson;
    private Class responseClass;

    public VolleyCustomRequest(int method, String url,Class responseClass, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        gson = new Gson();
        this.listener=listener;
        this.responseClass=responseClass;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(gson.fromJson(jsonString,responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }

}

