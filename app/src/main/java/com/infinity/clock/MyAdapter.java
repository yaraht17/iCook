package com.infinity.clock;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.infinity.icook.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 11/4/2015.
 */
public class MyAdapter extends ArrayAdapter<Entity> {
    Activity context = null;
    ArrayList<Entity> myArray = null;
    int layoutId;

    public MyAdapter(Activity context, int layoutId, ArrayList<Entity> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);
        final Entity tmp = myArray.get(position);
        final TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvTime.setText(tmp.getTime().toString());
        final ImageView imIcon = (ImageView) convertView.findViewById(R.id.imIcon);
        if (tmp.getState() == true) {
            imIcon.setImageResource(R.drawable.on);
        } else {
            imIcon.setImageResource(R.drawable.off);
        }

        return convertView;
    }
}
