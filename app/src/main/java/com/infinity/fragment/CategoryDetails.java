package com.infinity.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.infinity.adapter.DishListAdapter;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Data;
import com.infinity.data.Var;
import com.infinity.icook.DishDetailActivity;
import com.infinity.icook.R;
import com.infinity.model.DishItem;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

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
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        int index = Integer.parseInt(title) - 1;
        if (Data.dishCache[index] != null) {
            hidePDialog();
            dishList = Data.dishCache[index];
            dishListAdapter = new DishListAdapter(view.getContext(), R.layout.item_dish, dishList);
            dishLishView.setAdapter(dishListAdapter);
        } else {
            getCategory(title);
        }
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

    private void getCategory(String id) {
        if (ConnectionDetector.isNetworkConnected(getActivity().getApplicationContext())) {
            try {
                APIConnection.getListByCatogery(getActivity().getApplicationContext(), id, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        hidePDialog();
                        dishList = APIConnection.parseDishList(response);
                        Data.dishCache[Integer.parseInt(title) - 1] = dishList;
                        dishListAdapter = new DishListAdapter(getActivity().getApplicationContext(), R.layout.item_dish, dishList);
                        dishLishView.setAdapter(dishListAdapter);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getActivity().getApplicationContext(), "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            hidePDialog();
            //show thog bao
            Toast.makeText(getActivity().getApplicationContext(), "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
        }
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
