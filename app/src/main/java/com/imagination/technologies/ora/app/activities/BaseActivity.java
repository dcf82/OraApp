package com.imagination.technologies.ora.app.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.imagination.technologies.ora.app.controller.AppScreenStatusService;
import com.imagination.technologies.ora.app.controller.OraInteractiveApp;
import com.imagination.technologies.ora.app.interfaces.NotificationTask;
import com.imagination.technologies.ora.app.network.Service;

public class BaseActivity extends AppCompatActivity implements NotificationTask {

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName cn, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName cn) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        OraInteractiveApp.getApp().onActivityResume();
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        OraInteractiveApp.getApp().onActivityPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        bindService(new Intent(this, AppScreenStatusService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindService(mConnection);
    }

    @Override
    public void completed(Service serviceResponse) {
    }
}
