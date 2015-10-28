package com.infinity.volley;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infinity.data.Var;
import com.infinity.model.DishItem;
import com.infinity.model.MaterialItem;
import com.infinity.model.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIConnection {


    public static void getListByCatogery(Context context, String value, final VolleyCallback callback) throws UnsupportedEncodingException {
        String url = "";
        String query = URLEncoder.encode(value, "utf-8");
        url = Var.API_GET_LIST + query;


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
                    }
                });
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void demoGetAPI(Context context, String value, final VolleyCallback callback) throws UnsupportedEncodingException {
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

    public static void login(Context context, final String id, final VolleyCallback callback) throws JSONException {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Var.API_LOGIN, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "res err: " + error);
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Var.LOGIN_ID, id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void loginString(Context context, final String id, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Var.API_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TienDH", "res string: " + response);
                try {
                    callback.onSuccess(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
                Log.d("TienDH", "res err " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Var.LOGIN_ID, id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    public static void addUser(Context context, final UserItem user, String token, final VolleyCallback callback) throws JSONException {

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Var.USER_NAME, user.getName());
        jsonBody.put(Var.USER_BIRTH, user.getBirthdate());
        jsonBody.put(Var.USER_SEX, user.getSex());
        jsonBody.put(Var.USER_HEIGHT, user.getHeight());
        jsonBody.put(Var.USER_WEIGHT, user.getWeight());
        jsonBody.put(Var.ACCESS_TOKEN, token);
        Log.d("TienDH", "json send: " + jsonBody.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Var.API_ADD_USER, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TienDH", "Add user: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Add user err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
//                params.put("charset", "utf-8");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void getUser(Context context, String token, final VolleyCallback callback) throws UnsupportedEncodingException {
        String url = "";
        url = Var.API_GET_USER + token;


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
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void getChat(Context context, String message, final VolleyCallback callback) throws UnsupportedEncodingException {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.API_CHAT + URLEncoder.encode(message, "utf-8"), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());
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

    public static void sendToken(Context context, String token, final VolleyCallback callback) throws UnsupportedEncodingException {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Var.API_SEND_TOKEN + token, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TienDH", "Res Error" + error.toString());
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static ArrayList parseUser(JSONObject response) {
        ArrayList<UserItem> list = new ArrayList<>();
        try {
            JSONArray arrayData = response.getJSONArray("data");
            for (int i = 0; i < arrayData.length(); i++) {
                JSONObject object = arrayData.getJSONObject(i);
                int id = object.getInt(Var.USER_ID);
                String name = object.getString(Var.USER_NAME);
                String birthdate = object.getString(Var.USER_BIRTH);
                int sex = object.getInt(Var.USER_SEX);
                double height = object.getDouble(Var.USER_HEIGHT);
                double weight = object.getDouble(Var.USER_WEIGHT);

                UserItem user = new UserItem(id, name, birthdate, height, weight, sex);
                list.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<DishItem> parseDishList(JSONObject response) {
        ArrayList<DishItem> list = new ArrayList<>();

        try {
            JSONArray arrayData = response.getJSONArray("data");
            JSONObject objectData = arrayData.getJSONObject(0);
            JSONArray array = objectData.getJSONArray("dish");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt(Var.DISH_ID);
                String name = object.getString(Var.DISH_NAME);
                String introduce = object.getString(Var.DISH_INTRODUCE);
                String image = object.getString(Var.DISH_IMAGE);
                String instruction = object.getString(Var.DISH_INSTRUCTION);
                int aop = object.getInt(Var.DISH_AOP);
                ArrayList<MaterialItem> materials = APIConnection.parseMaterialList(object.getJSONArray(Var.DISH_MATERIALS));
                DishItem dish = new DishItem(id, name, introduce, image, instruction, aop, materials);
                Log.d("TienDH", dish.getName());
                list.add(dish);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<MaterialItem> parseMaterialList(JSONArray jsonArray) {
        ArrayList<MaterialItem> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt(Var.MATERIAL_ID);
                String name = jsonObject.getString(Var.MATERIAL_NAME);
                String amount = jsonObject.getString(Var.MATERIAL_AMOUNT);
                String unit = jsonObject.getString(Var.MATERIAL_UNIT);
                MaterialItem material = new MaterialItem(id, name, amount, unit);
                list.add(material);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

