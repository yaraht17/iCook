package com.infinity.clock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


public class MyAlarmService extends Service {


    @Override
    public void onCreate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int rq = intent.getIntExtra("rq", 0);
        Intent info = new Intent(MyAlarmService.this, InfoActivity.class);
        info.setAction(Intent.ACTION_VIEW);
        info.addCategory(Intent.CATEGORY_LAUNCHER);
        info.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        info.putExtra("rq", rq);
        startActivity(info);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}