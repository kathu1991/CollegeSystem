package com.itfyme.collegesystem.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karthik on 05/12/2021.
 */
public class CustomJSONObjectRequest extends Request<String> {

    private Response.Listener<String> listener;
    private Map<String, String> params;
    private static Map httpHeaders;

    public CustomJSONObjectRequest(String url,
                                   Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;

    }
    public CustomJSONObjectRequest(String url, Map<String, String> params,
                                   Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }


    @Override
    public Map getHeaders() throws AuthFailureError {
        HashMap<String, String> headersParam = new HashMap<String, String>();
        headersParam.put("User-Agent", "android_unique_request");



        return headersParam;
    }


    protected Map getParams()
            throws AuthFailureError {
        return params;
    }

    ;
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("response", jsonString);
            setCookie(response.headers, jsonString);
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

    private static void setCookie(Map headers, String jsonStr) {

    }
}

