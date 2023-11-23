package com.vt.part1_sentuhdigitalteknologitest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

public class MyBackgroundService extends Service {

    private final IBinder binder = new LocalBinder();
    private WindowManager windowManager;
    private View view;
    ServiceCallbackListener listener;

    public void setServiceCallbackListener(ServiceCallbackListener listener) {
        this.listener = listener;
    }

    public class LocalBinder extends Binder {
        MyBackgroundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyBackgroundService.this;
        }
    }

    public MyBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MyBackgroundService.class.getName(), "Service Dijalankan");
        return START_STICKY;
    }

    @SuppressLint("InflateParams")
    private void createView() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if (inflater != null) {
            WindowManager.LayoutParams layoutParams = getLayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            layoutParams.x = 0;
            layoutParams.y = 100;
            view = inflater.inflate(R.layout.background_service_page, null);

            Button button = view.findViewById(R.id.btnConnectService);

            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.dataServicePass("Service is Connected");
                }
            });

            windowManager.addView(view, layoutParams);
        }
    }

    @NotNull
    static WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams params;
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
        return params;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            windowManager.removeView(view);
        }
    }

}