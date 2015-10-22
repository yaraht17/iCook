package com.infinity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinity.icook.R;
import com.infinity.model.DishItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DishListAdapter extends ArrayAdapter<DishItem> {

    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<DishItem> sickList = new ArrayList<DishItem>();
    private List<DishItem> worldpopulationlist = new ArrayList<DishItem>();

    public DishListAdapter(Context context, int resource,
                           ArrayList<DishItem> sickItems) {
        super(context, resource, sickItems);
        this.context = context;
        this.resource = resource;
        this.sickList = sickItems;
        worldpopulationlist.addAll(sickItems);

    }


    public List<DishItem> getDishList() {
        return sickList;
    }

    public void setDishList(List<DishItem> sickList) {
        this.sickList = sickList;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public DishItem getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        DishItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_dish, parent, false);
            viewHolder.sickName = (TextView) row
                    .findViewById(R.id.txt_sick_name);
            viewHolder.img = (ImageView) row.findViewById(R.id.img_sick_avatar);
            viewHolder.des = (TextView) row.findViewById(R.id.txtDescription);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.sickName.setText(item.getName());
        viewHolder.img.setImageResource(R.drawable.cat_cake);
        viewHolder.des.setText(item.getDescription());
        return row;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(sickList);
        } else {
            for (DishItem wp : sickList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(
                        charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView sickName;
        ImageView img;
        TextView des;
    }

}
