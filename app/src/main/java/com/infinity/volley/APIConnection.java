package com.infinity.volley;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infinity.data.Var;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class APIConnection {
    private static Context context;

    public APIConnection(Context context) {
        this.context = context;
    }

    public static void getListByCatogery(String value, final VolleyCallback callback) throws UnsupportedEncodingException {
        String url = "";
        String query = URLEncoder.encode(value, "utf-8");
        url = Var.URL_HOST + query;


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TienDH", "res : " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                        Log.d("TienDH", "Res Error" + error.toString());
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void demoGetAPI(String value, final VolleyCallback callback) throws UnsupportedEncodingException {
        String url = "";
        String query = URLEncoder.encode(value, "utf-8");
        url = Var.URL_HOST + query;


        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("TienDH", "res ok: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TienDH", "res err: " + error);
                callback.onError(error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(movieReq);
    }
}

