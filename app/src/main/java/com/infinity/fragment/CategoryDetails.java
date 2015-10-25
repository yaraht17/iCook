package com.infinity.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.infinity.adapter.DishListAdapter;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Var;
import com.infinity.icook.DishDetailActivity;
import com.infinity.icook.R;
import com.infinity.model.DishItem;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CategoryDetails extends Fragment {
    String title;
    private ArrayList<DishItem> dishList = new ArrayList<>();
    private ListView dishLishView;
    private DishListAdapter dishListAdapter;
    private ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cat_details, container, false);

        dishLishView = (ListView) view.findViewById(R.id.listDish);

        dishListAdapter = new DishListAdapter(view.getContext(), R.layout.item_dish, dishList);
        dishLishView.setAdapter(dishListAdapter);

        pDialog = new ProgressDialog(view.getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getCategory();

        dishLishView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DishDetailActivity.class);
                intent.putExtra(Var.DISH_EXTRA, dishList.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private void loading() {
        if (ConnectionDetector.isNetworkConnected(getActivity().getApplicationContext())) {
            try {
                APIConnection.demoGetAPI("", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        hidePDialog();
                    }

                    @Override
                    public void onSuccess(JSONArray response) {
                        hidePDialog();
                        Log.d("TienDH", "res success: " + response);
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                String name = obj.getString("title");
                                String url = obj.getString("image");

                                String des = obj.getString("releaseYear");

                                DishItem dish = new DishItem(1, name, url, des);
                                // adding movie to movies array
                                dishList.add(dish);
                                dishListAdapter = new DishListAdapter(getActivity().getApplicationContext(), R.layout.item_dish, dishList);
                                dishLishView.setAdapter(dishListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        hidePDialog();
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getCategory() {
        if (ConnectionDetector.isNetworkConnected(getActivity().getApplicationContext())) {
            try {
                APIConnection.getListByCatogery("2", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        hidePDialog();
                        dishList = APIConnection.parseDishList(response);
                        dishListAdapter = new DishListAdapter(getActivity().getApplicationContext(), R.layout.item_dish, dishList);
                        dishLishView.setAdapter(dishListAdapter);
                    }

                    @Override
                    public void onSuccess(JSONArray response) {
                        hidePDialog();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        hidePDialog();
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            hidePDialog();
            //show thog bao
        }
    }

    private ArrayList createListDemo() {
        ArrayList<DishItem> list = new ArrayList<>();
        list.add(new DishItem(1, "Bánh Mỳ", "", "Ngon"));
        list.add(new DishItem(2, "Bánh GATO", "", "Cũng rất ngon"));
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CategoryDetails(String text) {
        title = text;
    }


}
