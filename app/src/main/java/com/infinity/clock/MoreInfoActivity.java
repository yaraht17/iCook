package com.infinity.clock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.infinity.data.Database;
import com.infinity.data.Var;
import com.infinity.icook.R;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoreInfoActivity extends Activity implements View.OnClickListener {
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
    private SharedPreferences sharedPreferences;
    private String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_notification);

        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");

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
        Database dbDish = new Database(this);
        arr = dbDish.getListToday();
        adapter = new MyAdapterInfo(this, R.layout.my_item2, arr);
        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(this);
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
                if (text.equals("") || text == null) return;
                Entity tmp = new Entity(text, true);
                arr.add(0, tmp);
                adapter.notifyDataSetChanged();

                etAdd.setText("");
                break;
            case R.id.btnSent:
                List<String> list = new ArrayList<>();

                for (Entity i : adapter.getCheckBox()) {
                    if (i.getState()) {
                        list.add(i.getTime());
                    }
                }
                Log.d("TienDH", "Sent " + list.size());
                if (list.size() != 0) {
                    String data = "";
                    for (int i = 0; i < list.size() - 1; i++) {
                        data = data + list.get(i) + ", ";
                    }
                    data = data + list.get(list.size() - 1);
                    Log.d("TienDH", "gửi api " + data);
                    try {
                        APIConnection.sendLog(this, accessToken, data, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    int code = response.getInt("code");
                                    if (code == 200) {
                                        Toast.makeText(getApplicationContext(), "Đã lưu lại lịch sử!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }

    }


}
