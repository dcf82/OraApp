package com.imagination.technologies.ora.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.imagination.technologies.ora.app.controller.OraInteractiveApp;

public class Reachability {

    public enum ReachabilityState {
        ReachableWifi,
        ReachableMobile,
        NotReachable,
    }

    private static final String LOG = Reachability.class.getName();
    private static Reachability pSingleton = new Reachability();
    private ReachabilityState currentState;
    private ReachabilityCallback delegate;

    private Reachability() {
    }

    public static Reachability getSingleton() {
        return pSingleton;
    }

    public void setDelegate(ReachabilityCallback del) {
        this.delegate = del;
    }

    public void checkReachability() {
        ConnectivityManager connMgr = ((ConnectivityManager) OraInteractiveApp.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE));

        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        ReachabilityState tempState;
        State mobile = null;
        State wifi = null;

        netInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo != null)
            mobile = netInfo.getState();

        netInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfo != null)
            wifi = netInfo.getState();

        if (mobile == State.CONNECTED) {
            tempState = ReachabilityState.ReachableMobile;
            Log.d(LOG, "***** MOBILE NETWORK CONNECTED *****");
        } else if (wifi == State.CONNECTED) {
            tempState = ReachabilityState.ReachableWifi;
            Log.d(LOG, "***** WIFI NETWORK CONNECT *****");
        } else {
            tempState = ReachabilityState.NotReachable;
            Log.d(LOG, "***** NO NETWORK AVAILALE *****");
        }

        if (currentState != tempState && delegate != null) {
            currentState = tempState;
            delegate.reachabilityChanged(currentState);
        }
    }

    public static ReachabilityState getNetWorkType() {
        ReachabilityState type = ReachabilityState.NotReachable;

        ConnectivityManager cm = (ConnectivityManager) OraInteractiveApp.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.isConnected() && ni.isAvailable()) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        type = ReachabilityState.ReachableWifi;
                    } else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        type = ReachabilityState.ReachableMobile;
                    }
                }
            }
        }
        return type;
    }

    public static boolean isReachable() {
        return getSingleton().currentState == ReachabilityState.ReachableMobile
                || getSingleton().currentState == ReachabilityState.ReachableWifi;
    }

    public static ReachabilityState getCurrentReachabilityState() {
        return pSingleton.currentState;
    }

    public static void stopReachability() {
        getSingleton().setDelegate(null);
    }

    public interface ReachabilityCallback {
        public void reachabilityChanged(ReachabilityState newState);
    }
}
