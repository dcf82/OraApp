package com.imagination.technologies.ora.app.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String LOG = NetworkReceiver.class.getName();
    private static boolean isNetworkAvailable = false;

    String typeName;
    String subtypeName;

    CheckReachabilityTask currentTask;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo info = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (info == null)
                return;

            typeName = info.getTypeName();
            subtypeName = info.getSubtypeName();

            boolean tmpAvailable = info.isAvailable();

            Log.i(LOG, "Network Type: " + typeName + ", subtype: "
                    + subtypeName + ", available: " + isNetworkAvailable);

            if (isNetworkAvailable != tmpAvailable && currentTask == null) {
                isNetworkAvailable = tmpAvailable;
                currentTask = new CheckReachabilityTask();
                currentTask.execute();
            }
        }
    }

    private class CheckReachabilityTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Reachability.getSingleton().checkReachability();
            currentTask = null;
            return null;
        }
    }

    public static String getNetworkType() {
        return ConnectionUtil.getSingleton().getConectionType() ==
                ConnectivityManager.TYPE_MOBILE ? "Mobile"
                : ConnectionUtil.getSingleton().getConectionType() ==
                ConnectivityManager.TYPE_WIFI ? "Wifi"
                : "None";
    }
}
