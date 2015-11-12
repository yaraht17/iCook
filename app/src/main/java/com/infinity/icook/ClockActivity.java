package com.infinity.icook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.infinity.clock.Entity;
import com.infinity.clock.MyAdapter;
import com.infinity.clock.MyAlarmService;
import com.infinity.data.Var;

import java.util.ArrayList;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private Typeface font_awesome, font_tony;
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private SharedPreferences sharedPreferences;

    ArrayList<Entity> arr = new ArrayList<Entity>();
    MyAdapter adapter = null;
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        Log.d("key", "c");
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(click);
        arr = new ArrayList<>();
        adapter = new MyAdapter(this, R.layout.my_item, arr);
        lv.setAdapter(adapter);

        NavTitle.setText("Clock");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        String timeEnd = sharedPreferences.getString(Var.CLOCK_TIME, "");
        Log.d("key", timeEnd);
        if (timeEnd.equals("") == false) {
            String[] timeResult = timeEnd.split(";");
            for (int i = 0; i < timeResult.length; i++) {
                Entity tmp = new Entity(timeResult[i], true);
                arr.add(tmp);
            }
            adapter.notifyDataSetChanged();
        }
    }


    AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            arr.remove(position);
            adapter.notifyDataSetChanged();
            delArr(position);
        }
    };

    public void delArr(int pos) {
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        String timeBefore = sharedPreferences.getString(Var.CLOCK_TIME, "");
        String str1 = sharedPreferences.getString(Var.STR_INTENT, "");
        String[] timeResult = timeBefore.split(";");
        String[] strResutl = str1.split(";");
        String tmp = timeResult[pos];
        String tmp_str = strResutl[pos];
        String timeApter = timeBefore.replaceFirst(tmp + ";", "");
        String str2 = str1.replaceFirst(tmp_str + ";", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Var.CLOCK_TIME, timeApter);
        editor.putString(Var.STR_INTENT, str2);
        editor.commit();
        Log.d("key", str2);
        int n = Integer.parseInt(tmp_str);
        Intent myIntent = new Intent(getApplicationContext(), MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), n, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                finish();
                break;
            default:
                break;
        }
    }

}
