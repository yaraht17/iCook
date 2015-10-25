package com.infinity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.infinity.icook.R;
import com.infinity.model.DishItem;
import com.infinity.volley.MySingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DishListAdapter extends ArrayAdapter<DishItem> {

    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<DishItem> dishList = new ArrayList<DishItem>();
    private List<DishItem> worldpopulationlist = new ArrayList<DishItem>();
    private ImageLoader imageLoader;
    public DishListAdapter(Context context, int resource,
                           ArrayList<DishItem> sickItems) {
        super(context, resource, sickItems);
        this.context = context;
        this.resource = resource;
        this.dishList = sickItems;
        worldpopulationlist.addAll(sickItems);
        imageLoader = MySingleton.getInstance(context).getImageLoader();

    }


    public List<DishItem> getDishList() {
        return dishList;
    }

    public void setDishList(List<DishItem> dishList) {
        this.dishList = dishList;
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
            if (imageLoader == null)
                imageLoader = MySingleton.getInstance(context).getImageLoader();
            row = inflater.inflate(R.layout.item_dish, parent, false);
            viewHolder.dishName = (TextView) row
                    .findViewById(R.id.txt_dish_name);
            viewHolder.img = (NetworkImageView) row.findViewById(R.id.img_dish_avatar);
            viewHolder.des = (TextView) row.findViewById(R.id.txtDescription);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.dishName.setText(item.getName());
//        viewHolder.img.setImageResource(R.drawable.cat_cake);
        viewHolder.img.setImageUrl(item.getAvatar(), imageLoader);
        viewHolder.des.setText(item.getDescription());
        return row;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(dishList);
        } else {
            for (DishItem wp : dishList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(
                        charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView dishName;
        NetworkImageView img;
        TextView des;
    }

}
