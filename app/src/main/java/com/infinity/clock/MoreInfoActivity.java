package com.infinity.clock;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.infinity.icook.R;

import java.util.ArrayList;
import java.util.List;

public class MoreInfoActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private ListView lv;
    private EditText etAdd;
    private TextView imAdd;
    private Button btnSent;
    ArrayList<Entity> arr = new ArrayList<Entity>();
    MyAdapterInfo adapter = null;
    private Typeface font_awesome, font_tony;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_notification);
        Log.d("key", "c");
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);
        NavTitle.setText("Subscribe");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.lvDish);
        etAdd = (EditText) findViewById(R.id.etAdd);
        imAdd = (TextView) findViewById(R.id.imAdd);
        imAdd.setTypeface(font_awesome);
        imAdd.setOnClickListener(this);
        btnSent = (Button) findViewById(R.id.btnSent);
        btnSent.setTypeface(font_tony);
        btnSent.setOnClickListener(this);
        arr = new ArrayList<>();
        arr.add(new Entity("Bánh mỳ", false));
        arr.add(new Entity("Cocacola", true));
        adapter = new MyAdapterInfo(this, R.layout.my_item2, arr);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                finish();
                break;
            case R.id.imAdd:
                String text = etAdd.getText().toString();
                Entity tmp = new Entity(text, true);
                arr.add(0, tmp);
                adapter.notifyDataSetChanged();
                etAdd.setText("");
                break;
            case R.id.btnSent:
                List<String> list = new ArrayList<>();
                for (Entity i : arr) {
                    if (i.getState()) {
                        list.add(i.getTime());
                    }
                }
                // tra ve list
                Log.d("TienDH", " size : " + list.size());
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TienDH", "Item click");
        CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
        boolean check = cb.isChecked();
        if (check == true) {
            arr.get(position).setState(false);
        } else {
            arr.get(position).setState(true);
        }
        adapter.notifyDataSetChanged();
    }


}
