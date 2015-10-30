package com.infinity.icook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            txtDes.setText("Không có nhắc nhở");
        }

        NavTitle.setText("Clock");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                finish();
                break;
            case R.id.btnSwitch:
                if (!timeEnd.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Var.CLOCK_TIME);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Tắt nhắc nhở!", Toast.LENGTH_SHORT).show();
                    txtDes.setText("Không có nhắc nhở");
                    stopService(ClockService.TAG);
                }
                break;
            default:
                break;
        }
    }

    public void stopService(final String tag) {
        Intent intent = (new Intent(getBaseContext(), ClockService.class));
        intent.addCategory(tag);
        stopService(intent);
    }
}
