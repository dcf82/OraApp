package com.imagination.technologies.ora.app.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.beans.LocationsWrapper;
import com.imagination.technologies.ora.app.beans.MyLocation;
import com.imagination.technologies.ora.app.beans.RegistrationResponse;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.controller.OraInteractiveApp;
import com.imagination.technologies.ora.app.events.LocationsProcessed;
import com.imagination.technologies.ora.app.events.SendData;
import com.imagination.technologies.ora.app.greendao.ChatMessage;
import com.imagination.technologies.ora.app.greendao.ChatMessageDao;
import com.imagination.technologies.ora.app.greendao.ChatRoom;
import com.imagination.technologies.ora.app.greendao.ChatRoomDao;
import com.imagination.technologies.ora.app.greendao.Locations;
import com.imagination.technologies.ora.app.greendao.LocationsDao;
import com.imagination.technologies.ora.app.interfaces.NotificationTask;
import com.imagination.technologies.ora.app.network.LoadServices;
import com.imagination.technologies.ora.app.network.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.dao.query.WhereCondition;
import de.greenrobot.event.EventBus;

public class Utility {
    private static final String LOG = Utility.class.getName();

    /**
     * @param drawableId : resource id of the given drawable
     */
    public static Drawable getDrawableById(int drawableId) {

        Drawable myDrawable;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            myDrawable = OraInteractiveApp.getApp().getAppResources().getDrawable(drawableId,
                    OraInteractiveApp.getApp().getTheme());
        } else {
            myDrawable = OraInteractiveApp.getApp().getAppResources().getDrawable(drawableId);
        }

        return myDrawable;
    }

    /**
     * @param dp : Number of DP to convert
     */
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                OraInteractiveApp.getApp().getAppResources().getDisplayMetrics());
    }

    public static Display getDeviceDisplay() {
        WindowManager wm = (WindowManager) OraInteractiveApp.getApp().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    /**
     * Get Locations available
     */
    public static List<Locations> getMyLocations() {
        return OraInteractiveApp.getApp().getDAOSession().getLocationsDao().loadAll();
    }

    /**
     * Get Locations available
     */
    public static void storeLocations(List<Locations> locs) {
        LocationsDao dao = OraInteractiveApp.getApp().getDAOSession().getLocationsDao();
        dao.deleteAll();
        dao.insertInTx(locs);
    }

    /**
     * Get Chat Rooms available
     */
    public static List<ChatRoom> getChatRooms() {
        return OraInteractiveApp.getApp().getDAOSession().getChatRoomDao().loadAll();
    }

    /**
     * Store the new Chat Rooms
     * @param chatRoom Chat Room
     */
    public static void storeChatRoom(ChatRoom chatRoom) {
        ChatRoomDao dao = OraInteractiveApp.getApp().getDAOSession().getChatRoomDao();
        dao.insertInTx(chatRoom);
    }

    /**
     * Store the new Chat Rooms
     * @param chatRooms Chat Rooms to Store
     */
    public static void storeChatRooms(List<ChatRoom> chatRooms) {
        ChatRoomDao dao = OraInteractiveApp.getApp().getDAOSession().getChatRoomDao();
        dao.deleteAll();
        dao.insertInTx(chatRooms);
    }

    /**
     * Get Chat Room By Id
     * @param id Id of the Chat Room
     */
    public static ChatRoom getChatRoomById(int id) {
        ChatRoomDao dao = OraInteractiveApp.getApp().getDAOSession().getChatRoomDao();

        WhereCondition where = ChatRoomDao.Properties.KChatRoomId.eq(id);
        List<ChatRoom> rooms = dao.queryBuilder().where(where).list();
        if (rooms.size() > 0)
            return rooms.get(0);
        return null;
    }


    /**
     * Get all messages per session
     * @param chatId : Session Id of the chat
     */
    public static List<ChatMessage> getChatMessagesByChatId(int chatId) {

        ChatMessageDao dao = OraInteractiveApp.getApp().getDAOSession().getChatMessageDao();

        WhereCondition where = ChatMessageDao.Properties.KChatId.eq(chatId);

        List<ChatMessage> list = dao.queryBuilder().where(where)
                .orderAsc(ChatMessageDao.Properties.KMessageId)
                .list();

        return list;
    }

    /**
     * Get all messages per session
     * @param messages Messages to store
     * @param chatId : Chat Id
     */
    public static void fillChatMessagesByChatId(List<ChatMessage> messages, int chatId) {
        ChatMessageDao dao = OraInteractiveApp.getApp().getDAOSession().getChatMessageDao();
        WhereCondition where = ChatMessageDao.Properties.KChatId.eq(chatId);
        List<ChatMessage> list = dao.queryBuilder().where(where).list();
        dao.deleteInTx(list);
        dao.insertInTx(messages);
    }

    public static void fillChatMessageByChatId(ChatMessage message) {
        ChatMessageDao dao = OraInteractiveApp.getApp().getDAOSession().getChatMessageDao();
        dao.insert(message);
    }

    public static <T> void writeValueToProfile(Class<? extends T> type, String key, T value) {
        SharedPreferences.Editor editor = OraInteractiveApp.getApp()
                .getOIProfiles().edit();
        Method method;
        String methodName = "put" + getType(type);
        Log.i(LOG, "Method Name :: " + methodName);

        try {
            method = editor.getClass().getMethod(methodName, String.class, type);
            method.invoke(editor, key, value);
            editor.commit();
        } catch (Exception e) {
            Log.i(LOG, "error :: " + e);
        }
    }

    public static <T> T readValueFromProfile(Class<? extends T> type, String key, T defValue) {
        SharedPreferences sh = OraInteractiveApp.getApp().getOIProfiles();
        Method method;
        T value = null;
        String methodName = "get" + getType(type);
        Log.i(LOG, "Method Name :: " + methodName);

        try {
            method = sh.getClass().getMethod(methodName, String.class, type);
            value = (T)method.invoke(sh, key, defValue);
        } catch (Exception e) {
            Log.i(LOG, "error :: " + e);
        }
        return value;
    }

    private static <T> String getType(Class<? extends T> type) {
        String t = "String";

        if (type == int.class) {
            t = "Int";
        } else if (type == boolean.class) {
            t = "Boolean";
        } else if (type == float.class) {
            t = "Float";
        } else if (type == long.class) {
            t = "Long";
        } else if (type == String.class) {
            t = "String";
        } else if (type == Set.class.asSubclass(String.class)) {
            t = "StringSet";
        }

        return t;
    }

    /**
     * Parses JSON String and transforms it to the desired type
     * @param json : Json String
     * @param type : Type of Object to generate
     */
    public static <T> T parseJSON(String json, Class<T> type) {
        T r = null;

        try {
            r = new Gson().fromJson(json, type);
        } catch (Exception e) {
            Log.i(LOG, "error while parsing :: " + e);
        }

        return r;
    }

    /**
     * Stores user profile
     * @param userProfile : USer Profile to save
     */
    public static void storeUserProfile(RegistrationResponse userProfile) {
        writeValueToProfile(String.class, Config.USER_EMAIL, userProfile.
                getData().getEmail());
        writeValueToProfile(String.class, Config.FIRST_NAME, userProfile.
                getData().getName());
        writeValueToProfile(int.class, Config.USER_ID, userProfile.
                getData().getId());
        writeValueToProfile(String.class, Config.USER_TOKEN, userProfile.
                getData().getToken());
        writeValueToProfile(boolean.class, Config.IS_USER_LOGGED, true);
    }

    /**
     * Deletes user profile
     */
    public static void deleteUserProfile() {
        SharedPreferences.Editor editor = OraInteractiveApp.getApp().getOIProfiles().edit();
        editor.remove(Config.USER_EMAIL);
        editor.remove(Config.FIRST_NAME);
        editor.remove(Config.USER_ID);
        editor.remove(Config.USER_TOKEN);
        editor.remove(Config.IS_USER_LOGGED);
        editor.commit();
    }

    public static Map<String, String> getJsonAccess() {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        return map;
    }

    public static Map<String, String> getJsonAccessAndAuthorization(String auth) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Authorization", auth);
        return map;
    }

    public static void sendEvent(String action, Intent info) {
        Intent intent = new Intent();
        if (info != null)
            intent.putExtras(info);
        intent.setAction(action);
        OraInteractiveApp.getApp().sendBroadcast(intent);
    }

    public static void showToast(String message) {
        Toast toast = Toast.makeText(OraInteractiveApp.getApp(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void loadLocations(NotificationTask listener) {
        Service mService = new Service();
        mService.setServiceCode(Config.GET_LOCATIONS);
        mService.setBaseURL(OraInteractiveApp.getApp().getString(R.string.locations));
        mService.setServiceType(Config.GET);
        mService.setHeaders(Utility.getJsonAccess());
        mService.setNotificationTask(listener);
        new LoadServices().loadOnExecutor(mService);
    }

    public static void storeLocations(String res) {
        String input = res.replace("(", "").replace(")", "");
        LocationsWrapper wrapper = Utility.parseJSON(input, LocationsWrapper.class);
        List<Locations> locs;
        LocationsProcessed.RESPONSE response;

        if (wrapper != null) {

            locs = new ArrayList<>();

            // Store data in Data Base
            Locations loc;
            for (MyLocation location : wrapper.getMyLocations()) {
                loc = new Locations();
                loc.setKLatitude(location.getLatitude());
                loc.setKLongitude(location.getLongitude());
                loc.setKZipClass(location.getZipClass());
                loc.setKZipcode(location.getZipcode());
                loc.setKCounty(location.getCounty());
                loc.setKCity(location.getCity());
                loc.setKState(location.getState());
                locs.add(loc);
            }

            // Store in DB
            Utility.storeLocations(locs);

            // Refresh UI
            response = LocationsProcessed.RESPONSE.SUCCESSFUL;
        } else {
            // Refresh UI on error as needed
            response = LocationsProcessed.RESPONSE.FAILURE;
        }
        EventBus.getDefault().post(new LocationsProcessed(response));
    }

    /**
     * Makes a cleanup of the Fragment Stack up to but not including the Fragment
     * that you want to put at the top of the stack
     * @param fm Fragment Manager to be used
     * @param className Fragment to be created
     * @param container container where the Fragment will be put
     */
    public static void replaceFragment (FragmentManager fm, String className, int container, Bundle bundle) {
        //boolean emptyFragmentStack = bundle != null && bundle.getBoolean("emptyFragmentStack" , false);
        boolean fragmentPopped = //emptyFragmentStack ?
                //fm.popBackStackImmediate ("", FragmentManager.POP_BACK_STACK_INCLUSIVE) :
                fm.popBackStackImmediate (className, 0);
        Fragment fragment = fm.findFragmentByTag(className);

        if (!fragmentPopped && fragment == null) {
            fragment = buildFragment(className, "newInstance");
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                    R.anim.slide_in_left,R.anim.slide_out_left);
            ft.replace(container, fragment, className);
            ft.addToBackStack(className);
            ft.commit();
        }

        if (fragment != null && bundle != null) {
            // It will be executed at some point in the feature
            EventBus.getDefault().postSticky(new SendData(bundle));
        }
    }

    /**
     * Fragment Builder for getting a new instance of a Fragment, considering
     * that we are using an static builder method and also not arguments are
     * passed to it
     * @param className Fragment to be build
     * @param methodName method to be called
     */
    public static Fragment buildFragment(String className, String methodName) {
        Fragment f = null;
        Class<?> myClass;
        Method method;

        try {
            myClass = Class.forName(className);
            /** Find the method inside the Class and by passing the method name & the types of
             * arguments for that method, example :
             * method = myClass.getMethod(methodName, Context.class, Integer.class);
             */
            method = myClass.getMethod(methodName);

            /**
             * If the previous line of code was successful we can execute that method over
             * that particular class instance built at the beginning with the real values to be used
             * so, the method receives the listener, which is normally the given instance plus the
             * values to be used by the method to work, if the method is static just put NULL as
             * the first parameter
             */
            f = (Fragment) method.invoke(null);
        } catch (Exception e) {
            Log.i(LOG, "ERROR ::: " + e);
        }

        return f;
    }
}
