package com.infinity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinity.data.Var;
import com.infinity.icook.R;
import com.infinity.model.UserItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListAdapter extends ArrayAdapter<UserItem> {
    private int resource;
    private Context context;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<UserItem> userList = new ArrayList<UserItem>();

    public UserListAdapter(Context context, int resource, ArrayList<UserItem> users) {
        super(context, resource, users);
        this.context = context;
        this.resource = resource;
        this.userList = users;
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public UserItem getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        UserItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_user, parent, false);
            viewHolder.name = (TextView) row
                    .findViewById(R.id.txt_user_name);
            viewHolder.birthdate = (TextView) row.findViewById(R.id.txt_birthdate);
            viewHolder.img = (ImageView) row.findViewById(R.id.img_user_avatar);
            viewHolder.height = (TextView) row.findViewById(R.id.txt_height);
            viewHolder.weight = (TextView) row.findViewById(R.id.txt_weight);
            viewHolder.bgAvt = (RelativeLayout) row.findViewById(R.id.bg_avt);
            viewHolder.txtNameAvt = (TextView) row.findViewById(R.id.name_avatar);
            viewHolder.favourite = (TextView) row.findViewById(R.id.txt_like);
            viewHolder.dislike = (TextView) row.findViewById(R.id.txt_dislike);
            viewHolder.symptom = (TextView) row.findViewById(R.id.txt_sickhistory);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.name.setText(item.getName());
        viewHolder.birthdate.setText(item.getBirthdate());
        viewHolder.height.setText("Chiều cao: " + String.valueOf(item.getHeight()));
        viewHolder.weight.setText("Cân nặng: " + String.valueOf(item.getWeight()));
        viewHolder.favourite.setText("Sở thích: " + item.getPersonal().getLike());
        viewHolder.dislike.setText("Sở ghét: " + item.getPersonal().getDislike());
        viewHolder.symptom.setText("Lịch sử bệnh: " + item.getPersonal().getSick());
        String name = item.getName();
        name = name.trim();
        String name_split[] = name.split(" ");
        int l = name_split.length;
        if (l >= 2) {
            String s = name_split[l - 2].charAt(0) + ""
                    + name_split[l - 1].charAt(0);
            s = s.toUpperCase(Locale.getDefault());
            viewHolder.txtNameAvt.setText(s);
        } else {
            viewHolder.txtNameAvt.setText(name.charAt(0) + "");

        }
        viewHolder.img.setBackgroundResource(Var.DRAWABLE_LIST[(position + 3) % 6]);
        return row;
    }


    private class ViewHolder {
        TextView name, birthdate, height, weight, favourite, dislike, symptom;
        ImageView img;
        //avatar
        RelativeLayout bgAvt;
        TextView txtNameAvt;

    }
}
