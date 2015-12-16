package com.infinity.icook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infinity.data.Var;

public class SettingActivity extends Activity implements View.OnClickListener {
    private Button btnSetting;
    private boolean active;
    private Typeface font_awesome, font_tony;
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        btnSetting = (Button) findViewById(R.id.btnSetting);
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);

        NavTitle.setText("Cài đặt");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        active = sharedPreferences.getBoolean(Var.ACTIVE_VOICE, false);

        if (!active) {
            btnSetting.setBackgroundResource(R.drawable.off);
        }

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (active) {
                    active = false;
                    btnSetting.setBackgroundResource(R.drawable.off);
                    editor.putBoolean(Var.ACTIVE_VOICE, false);
                } else {
                    active = true;
                    btnSetting.setBackgroundResource(R.drawable.on);
                    editor.putBoolean(Var.ACTIVE_VOICE, true);
                }
                editor.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        active = sharedPreferences.getBoolean(Var.ACTIVE_VOICE, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
