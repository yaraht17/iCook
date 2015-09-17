package com.infinity.icook;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mac on 9/10/15.
 */
public class CategoryDetails extends Fragment{
    TextView Title;
    String title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cat_details, container, false);
        Title = (TextView) view.findViewById(R.id.textView);
        Title.setText(title);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CategoryDetails(String text){
        title = text;
    }




}
