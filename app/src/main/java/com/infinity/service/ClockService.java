package com.infinity.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.infinity.icook.R;

public class ClockService extends Service {
    public static final String TAG = "MyServiceTag";
    private int time;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    Thread thTime = new Thread(new Runnable() {
        @Override
        public void run() {
            while (time != 0) {
                try {
                    thTime.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = hTime.obtainMessage();
                message.arg1 = 1;
                hTime.sendMessage(message);
            }
        }
    });

    Handler hTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tmp = msg.arg1;
            if (tmp == 1) {
                time--;
            }
            if (time == 0) {
                MediaPlayer mediaPlayer = MediaPlayer.create(ClockService.this, R.raw.mariodie);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopService(new Intent(getBaseContext(), ClockService.class));
                    }
                });
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        time = intent.getExtras().getInt("time");
        thTime.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
