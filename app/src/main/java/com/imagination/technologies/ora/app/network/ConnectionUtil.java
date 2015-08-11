package com.imagination.technologies.ora.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.imagination.technologies.ora.app.controller.OraInteractiveApp;

public class ConnectionUtil {
    private static ConnectionUtil pConnectionUtil = new ConnectionUtil();

    private ConnectionUtil() {
    }

    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) OraInteractiveApp.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }

        return true;
    }

    public int getConectionType() {
        ConnectivityManager cM = (ConnectivityManager) OraInteractiveApp.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile
        State mobile = cM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        // wifi
        State wifi = cM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        return State.CONNECTED == mobile ? ConnectivityManager.TYPE_MOBILE
                : State.CONNECTED == wifi ? ConnectivityManager.TYPE_WIFI : -1;
    }

    public static ConnectionUtil getSingleton() {
        return pConnectionUtil;
    }
}
