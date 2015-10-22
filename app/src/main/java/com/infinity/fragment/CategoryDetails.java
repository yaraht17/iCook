package com.infinity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.infinity.adapter.DishListAdapter;
import com.infinity.icook.DishDetailActivity;
import com.infinity.icook.R;
import com.infinity.model.DishItem;

import java.util.ArrayList;

public class CategoryDetails extends Fragment {
    String title;
    private ArrayList<DishItem> dishList = new ArrayList<>();
    private ListView dishLishView;
    private DishListAdapter dishListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cat_details, container, false);
        //set lai title khi dang xem category thoi
        dishLishView = (ListView) view.findViewById(R.id.listDish);

        dishListAdapter = new DishListAdapter(view.getContext(), R.layout.item_dish, createListDemo());
        dishLishView.setAdapter(dishListAdapter);
        dishLishView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DishDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private ArrayList createListDemo() {
        ArrayList<DishItem> list = new ArrayList<>();
        list.add(new DishItem(1, "Bánh Mỳ", "", "Ngon"));
        list.add(new DishItem(2, "Bánh GATO", "", "Cũng rất ngon"));
        return list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CategoryDetails(String text) {
        title = text;
    }


}
