package com.infinity.clock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.infinity.data.Var;
import com.infinity.icook.R;


/**
 * Created by Administrator on 11/4/2015.
 */
public class InfoActivity extends Activity {

    private SharedPreferences sharedPreferences;
    Button btnExit;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.info);
        setFinishOnTouchOutside(false);
        Intent intent = getIntent();
        int rq = intent.getIntExtra("rq", 0);
//        Log.d("key", String.valueOf(rq));
        mediaPlayer = MediaPlayer.create(InfoActivity.this, R.raw.sound);
        mediaPlayer.start();
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        String timeEnd = sharedPreferences.getString(Var.CLOCK_TIME, "");
        String strNumber = sharedPreferences.getString(Var.STR_INTENT, "");
        String[] timeResult = timeEnd.split(";");
        String[] strResult = strNumber.split(";");
        if (timeResult.length > 1) {
            timeEnd = timeResult[1] + ";";
            strNumber = strResult[1] + ";";
            for (int i = 2; i < timeResult.length; i++) {
                timeEnd = timeEnd + timeResult[i] + ";";
                strNumber = strNumber + strResult[i] + ";";
            }
        } else {
            timeEnd = "";
            strNumber = "";
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Var.CLOCK_TIME, timeEnd);
        editor.putString(Var.STR_INTENT, strNumber);
        editor.commit();
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
