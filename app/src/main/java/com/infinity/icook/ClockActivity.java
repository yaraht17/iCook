package com.infinity.icook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infinity.data.Var;
import com.infinity.service.ClockService;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private Typeface font_awesome, font_tony;
    //nav
    private Button barbtn;
    private TextView NavTitle, txtDes;
    private SharedPreferences sharedPreferences;
    private String timeEnd;
    private Button btnSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);
        txtDes = (TextView) findViewById(R.id.desClock);
        btnSwitch = (Button) findViewById(R.id.btnSwitch);

        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        timeEnd = sharedPreferences.getString(Var.CLOCK_TIME, "");

        if (!timeEnd.equals("")) {
            txtDes.setText(timeEnd);
        } else {
            txtDes.setText("");
        }

        NavTitle.setText("Clock");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                finish();
                break;
            case R.id.btnSwitch:

            default:
                break;
        }
    }

    public void stopService() {
        Intent intent = (new Intent(getBaseContext(), ClockService.class));
        stopService(intent);
    }
}
