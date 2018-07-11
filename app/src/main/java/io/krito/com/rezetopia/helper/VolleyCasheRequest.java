package io.krito.com.rezetopia.helper;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by Mona Abdallh on 7/1/2018.
 */

public class VolleyCasheRequest extends Request {

    private final Response.Listener mListener;
    private final Response.ErrorListener mErrorListener;
    private Class responseClass;
    private Gson gson;

    public VolleyCasheRequest(int method, String url, Class responseClass, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        gson = new Gson();
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.responseClass=responseClass;
    }


    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
        if (cacheEntry == null) {
            cacheEntry = new Cache.Entry();
        }
        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        long now = System.currentTimeMillis();
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;
        cacheEntry.data = response.data;
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;
        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;

        try {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(gson.fromJson(jsonString,responseClass), cacheEntry);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

        //return Response.success(response, cacheEntry);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(Object response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
}
