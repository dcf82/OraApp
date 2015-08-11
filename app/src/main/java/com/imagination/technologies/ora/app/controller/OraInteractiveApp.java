package com.imagination.technologies.ora.app.controller;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.imagination.technologies.ora.app.BuildConfig;
import com.imagination.technologies.ora.app.beans.RegistrationErrorWrapper;
import com.imagination.technologies.ora.app.beans.RegistrationResponse;
import com.imagination.technologies.ora.app.beans.RoomChat;
import com.imagination.technologies.ora.app.beans.RoomChatsResponse;
import com.imagination.technologies.ora.app.events.Message;
import com.imagination.technologies.ora.app.events.MessageBackgroundThread;
import com.imagination.technologies.ora.app.events.MessageSameThread;
import com.imagination.technologies.ora.app.events.MessageUIThread;
import com.imagination.technologies.ora.app.greendao.ChatRoom;
import com.imagination.technologies.ora.app.greendao.DaoMaster;
import com.imagination.technologies.ora.app.greendao.DaoSession;
import com.imagination.technologies.ora.app.interfaces.NotificationTask;
import com.imagination.technologies.ora.app.network.LoadServices;
import com.imagination.technologies.ora.app.network.Reachability;
import com.imagination.technologies.ora.app.network.Service;
import com.imagination.technologies.ora.app.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class OraInteractiveApp extends Application implements NotificationTask,
        Reachability.ReachabilityCallback {

    private static String LOG = OraInteractiveApp.class.getName();

    private static OraInteractiveApp thiz;

    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private SharedPreferences mOIProfiles;

    boolean activityVisible;

    public void onCreate() {
        super.onCreate();

        // Save this context
        thiz = this;

        // Load Data Base
        openSQLiteDatabase();

        // Load App Profiles
        mOIProfiles = PreferenceManager.getDefaultSharedPreferences(this);

        // Load EventBus library
        loadEventBus();

        // Reachability of network data
        Reachability.getSingleton().setDelegate(this);
    }

    public Resources getAppResources() {
        return thiz.getResources();
    }

    public static OraInteractiveApp getApp() {
        return thiz;
    }


    public SQLiteDatabase openSQLiteDatabase() throws SQLiteException {
        if (database == null) {
            database = new DaoMaster.DevOpenHelper(this, Config.DATABASE_NAME,
                    null).getWritableDatabase();
        } else if (!database.isOpen()) {
            database = new DaoMaster.DevOpenHelper(this, Config.DATABASE_NAME,
                    null).getWritableDatabase();
        }
        return database;
    }

    public DaoSession getDAOSession() {
        DaoMaster dm = getDAOMaster();
        if (daoSession == null) {
            daoSession = dm.newSession();
        }
        return daoSession;
    }

    public DaoMaster getDAOMaster() {
        SQLiteDatabase d = openSQLiteDatabase();
        if (daoMaster == null) {
            daoMaster = new DaoMaster(d);
        } else if (daoMaster.getDatabase() != d) {
            daoMaster = new DaoMaster(d);
        }
        return daoMaster;
    }

    public SQLiteDatabase getDB() {
        return openSQLiteDatabase();
    }

    public void closeDB() {
        try {
            database.close();
        } catch (Exception e) {
            Log.i(LOG, "error when try to close the db(" + e + ")");
        } finally {
            database = null;
            daoMaster = null;
            daoSession = null;
        }
    }

    public SharedPreferences getOIProfiles() {
        return mOIProfiles;
    }

    public void loadServices() {
        Service[] services;
        if (Utility.readValueFromProfile(boolean.class, Config.IS_USER_LOGGED, false)) {
            services = new Service[2];
            services[0] = getProfile();
            services[1] = getChatRooms();
            new LoadServices().loadOnExecutor(services);
        }
        Utility.loadLocations(this);
    }

    private void loadEventBus() {
        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus();
        EventBus.getDefault().register(this);
    }

    /**
     * Current User Profile
     */
    public Service getProfile() {
        Service mService = new Service();
        mService.setServiceCode(Config.GET_USER_CURRENT_CODE);
        mService.setServiceName(Config.PAG_USER_CURRENT);
        mService.setServiceType(Config.GET);
        mService.setHeaders(Utility.getJsonAccessAndAuthorization(Utility.
                readValueFromProfile(String.class, Config.USER_TOKEN, "")));
        mService.setNotificationTask(this);
        return mService;
    }

    /**
     * Get Available Rooms
     */
    public Service getChatRooms() {
        Service mService = new Service();
        mService.setServiceCode(Config.GET_USER_CHATS_CODE);
        mService.setServiceName(Config.GAP_USER_CHATS);
        mService.setServiceType(Config.GET);
        mService.setHeaders(Utility.getJsonAccessAndAuthorization(Utility.
                readValueFromProfile(String.class, Config.USER_TOKEN, "")));
        mService.setNotificationTask(this);
        return mService;
    }

    @Override
    public void completed(Service response) {
        switch(response.getServiceCode()) {
            case Config.GET_USER_CURRENT_CODE: {
                RegistrationResponse rr;
                RegistrationErrorWrapper errorWrapper;

                try {
                    rr = Utility.parseJSON(response.getOutput(),
                            RegistrationResponse.class);
                    if (rr == null) {
                        errorWrapper = Utility.parseJSON(response.getOutput(),
                                RegistrationErrorWrapper.class);

                        // Error Query
                        if (errorWrapper != null) {
                            Log.i(LOG, "error :: " + errorWrapper.getError().getMessage());
                        } else {
                            Log.i(LOG, "server not responded");
                        }
                    } else {
                        Log.i(LOG, "Successful call");

                        // Successful query
                        Utility.storeUserProfile(rr);
                    }
                } catch (Exception e) {
                    Log.i(LOG, "error while parsing JSON :: " + e);
                }
            }
                break;
            case Config.GET_USER_CHATS_CODE: {
                RoomChatsResponse rr;

                try {
                    rr = Utility.parseJSON(response.getOutput(),
                            RoomChatsResponse.class);
                    if (rr != null) {
                        Log.i(LOG, "Successful call");

                        // Update Data Base
                        ChatRoom rc;
                        List<ChatRoom> rooms = new ArrayList<>();
                        for (RoomChat chat : rr.getData()) {
                            rc = new ChatRoom();
                            rc.setKChatRoomId(chat.getId());
                            rc.setKUserId(chat.getUser_id());
                            rc.setKChatRoomName(chat.getName());
                            rooms.add(rc);
                        }

                        // Publish Changes to UI
                        if (rooms.size() > 0) {
                            Utility.storeChatRooms(rooms);
                            Utility.sendEvent(Config.CHAT_ROOMS_UPDATED, null);
                        }
                    } else {
                        Log.i(LOG, "server not responded");
                    }
                } catch (Exception e) {
                    Log.i(LOG, "error while parsing JSON :: " + e);
                }
            }
                break;
            case Config.GET_LOCATIONS:{

                if (response.getOutput() == null || response.getOutput().equals("")) return;

                MessageBackgroundThread message = new MessageBackgroundThread();
                message.setOperationCode(Message.OPERATION.LOCATIONS);
                message.setResponse(response.getOutput());
                EventBus.getDefault().post(message);
            }
                break;
        }
    }

    // Events in the same thread as the poster
    public void onEvent(MessageSameThread event) {
    }

    // Events in UI thread
    public void onEventMainThread(MessageUIThread event) {
    }

    // Events in the background thread
    public void onEventBackgroundThread(MessageBackgroundThread event) {
        switch(event.getOperationCode()) {
            case LOCATIONS: {
                Utility.storeLocations(event.getResponse());
            }
            break;
        }
    }

    @Override
    public void reachabilityChanged(Reachability.ReachabilityState newState) {
        Log.d(LOG, "Reachability Changed: " + newState);

        boolean isInBackground = mOIProfiles.getBoolean(
                AppScreenStatusService.LOG + "HAS_FOCUS", false);

        if (!isInBackground && Reachability.isReachable()) {
            Log.d(LOG, "*** LOADING ALL ***");
            loadServices();
        }
    }

    public void onActivityResume() {
        activityVisible = true;
        boolean comingFromBackground = !mOIProfiles.getBoolean(
                AppScreenStatusService.LOG + "HAS_FOCUS", false);

        if (comingFromBackground) {
            loadServices();
            Log.d(LOG, "Executing from background");
        }
    }

    public void onActivityPause() {
        activityVisible = false;
    }
}
