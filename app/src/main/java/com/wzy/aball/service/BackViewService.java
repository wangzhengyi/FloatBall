package com.wzy.aball.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wzy.aball.engine.FloatViewManager;

public class BackViewService extends Service {
    private FloatViewManager mFloatViewManager;

    public BackViewService() {
    }

    @Override
    public void onCreate() {
        mFloatViewManager = FloatViewManager.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFloatViewManager.showFloatBall();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
