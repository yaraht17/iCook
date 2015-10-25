package com.infinity.volley;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;


public interface VolleyCallback {
    void onSuccess(JSONObject response);

    void onSuccess(JSONArray response);

    void onError(VolleyError error);
}