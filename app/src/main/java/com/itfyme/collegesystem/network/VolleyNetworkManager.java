package com.itfyme.collegesystem.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.itfyme.collegesystem.controller.AppController;
import com.itfyme.collegesystem.helpers.NetworkUtility;
import com.itfyme.collegesystem.helpers.QueryParamsBuilder;
import com.itfyme.collegesystem.interfaces.ResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class VolleyNetworkManager {
    Context mContext;
    private int mVolleyTimeoutMS = 500000000;
    private int mMaxRetries = 3;

    public VolleyNetworkManager(Context context) {
        this.mContext = context;
    }

    public void getRequest(String url, HashMap<String, String> params, ResponseHandler responseHandler) {
        String queryUrl = QueryParamsBuilder.getQueryParameters(url, params);
        CustomJSONObjectRequest customJSONObjectRequest = null;
        try {
            customJSONObjectRequest = new CustomJSONObjectRequest(queryUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("GET Response Volley  :", response);
                            handleResp(response, responseHandler);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("GET Response Error  :", volleyError.toString());
                            responseHandler.onSuccess(volleyError.toString());
                        }
                    }
            );
            customJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(mVolleyTimeoutMS, mMaxRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(customJSONObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void postRequest(String url, HashMap<String, String> params, ResponseHandler responseHandler) {
        CustomJSONObjectRequest customJSONObjectRequest = null;
        try {
            customJSONObjectRequest = new CustomJSONObjectRequest(url, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("POST Response Volley  :", response);
                                handleResp(response, responseHandler);
                            } catch (Exception jsonExp) {
                                jsonExp.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("POST Response Error  :", volleyError.toString());
                            responseHandler.onSuccess(volleyError.toString());
                        }
                    }
            );
            customJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(mVolleyTimeoutMS, mMaxRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(customJSONObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void handleResp(String str, ResponseHandler callback) {
        try {
            if (NetworkUtility.isValidJSONObject(str)) {
                JSONObject result = new JSONObject(str);
                if (result.has("response_object")) {
                    String strRespObj = result.opt("response_object").toString();
                    if (strRespObj == null || strRespObj == "" || strRespObj == "null") {
                        callback.onNoData("No Data available");
                    } else {
                        callback.onSuccess(strRespObj);
                    }

                } else {
                    callback.onSuccess(result.toString());
                }

            } else {
                callback.onFail(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
