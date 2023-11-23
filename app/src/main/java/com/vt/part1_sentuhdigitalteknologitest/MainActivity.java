package com.vt.part1_sentuhdigitalteknologitest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vt.part1_sentuhdigitalteknologitest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ServiceCallbackListener {

    private ActivityMainBinding activityMainBinding;

    MyBackgroundService mBackgroundService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btnActivationService.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    activityMainBinding.requestPermission.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "This app requires the SYSTEM_ALERT_WINDOW permission", Toast.LENGTH_SHORT).show();
                } else {
                    activityMainBinding.requestPermission.setVisibility(View.GONE);
                    if (!mBound) {
                        myBackgroundService();
                    }
                }
            }
        });
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder) iBinder;
            mBackgroundService = binder.getService();
            mBackgroundService.setServiceCallbackListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    private void myBackgroundService() {
        Intent intent = new Intent(this, MyBackgroundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void dataServicePass(String data) {
        if (!data.isEmpty()) activityMainBinding.isServiceConnectedText.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mBound = false;
    }
}