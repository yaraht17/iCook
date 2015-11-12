package com.infinity.clock;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.infinity.icook.R;

import java.util.ArrayList;

public class MyAdapterInfo extends ArrayAdapter<Entity> {
    Activity context = null;
    ArrayList<Entity> myArray = null;
    int layoutId;
    private ViewHolder viewHolder;

    public MyAdapterInfo(Activity context, int layoutId, ArrayList<Entity> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        viewHolder = new ViewHolder();
        Entity tmp = getItem(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_item2, parent, false);
            viewHolder.dishName = (TextView) view.findViewById(R.id.tv);
            viewHolder.cb = (CheckBox) view.findViewById(R.id.cb);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.dishName.setText(tmp.getTime().toString());
        viewHolder.cb.setChecked(tmp.getState());
        return view;
    }

    private class ViewHolder {
        TextView dishName;
        CheckBox cb;
    }
}
