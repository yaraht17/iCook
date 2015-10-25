package com.infinity.volley;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infinity.data.Var;
import com.infinity.model.DishItem;
import com.infinity.model.MaterialItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class APIConnection {
    private static Context context;

    public APIConnection(Context context) {
        this.context = context;
    }

    public static void getListByCatogery(String value, final VolleyCallback callback) throws UnsupportedEncodingException {
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
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }) {

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

