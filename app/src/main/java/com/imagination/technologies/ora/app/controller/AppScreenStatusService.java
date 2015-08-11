package com.imagination.technologies.ora.app.controller;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AppScreenStatusService extends Service {
    public static final String LOG = AppScreenStatusService.class.getName();
    private Intent mFocusIntent;
    private IBinder mBinder = new LocalBinder();
    private SharedPreferences.Editor mProfileEditor;
    private BReceiver mScreenStatusReceiver = new BReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        mFocusIntent = new Intent(LOG + "FOCUS_CHANGED_ACTION");
        mProfileEditor = OraInteractiveApp.getApp().getOIProfiles().edit();

        onFocus();

        /**
         * Register a receiver to receive screen status updates.
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, filter);
    }

    /**
     * onUnbind is only called when all the activities have unbind
     * from the service, which is perfect for what we need to do
     */
    @Override
    public boolean onUnbind(Intent intent) {
        onUnfocus();
        unregisterReceiver(mScreenStatusReceiver);
        return false;
    }

    /**
     * We save the status in the shared preferences to
     * retrieve whenever we need it.
     */
    private void onFocus() {
        mProfileEditor.putBoolean(LOG + "HAS_FOCUS", true);
        mProfileEditor.commit();
        mFocusIntent.putExtra(LOG + "EXTRA_KEY_HAS_FOCUS", true);
        sendBroadcast(mFocusIntent);
    }

    private void onUnfocus() {
        mProfileEditor.putBoolean(LOG + "HAS_FOCUS", false);
        mProfileEditor.commit();
        mFocusIntent.putExtra(LOG + "EXTRA_KEY_HAS_FOCUS", false);
        sendBroadcast(mFocusIntent);
    }

    private class BReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                onFocus();
                Log.i(LOG, "SCREEN_ON");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                onUnfocus();
                Log.i(LOG, "SCREEN_OFF");
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        AppScreenStatusService getService() {
            return AppScreenStatusService.this;
        }
    }
}
