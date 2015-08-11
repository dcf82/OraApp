package com.imagination.technologies.ora.app.controller;

public class Config {
    // Base URL for Services
    public static final String BASE_URL = "http://raml.orainteractive.com:8081";

    // Database Name
    public static final String DATABASE_NAME = "OraInteractive.db";

    // User Profile
    public static final String USER_EMAIL = "userEmail";
    public static final String FIRST_NAME = "firstName";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_ID = "user_id";
    public static final String IS_USER_LOGGED = "isUserLogged";
    public static final String CHAT_ID = "chat_id";

    // Types of Request
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;

    // Services
    public static final String POST_USER_REGISTER = "/users";
    public static final String POST_USER_LOGIN = "/users/login";
    public static final String PAG_USER_CURRENT = "/users/_current";
    public static final String GAP_USER_CHATS = "/chats";
    public static final String GAP_USER_CHAT_BY_ID = "/messages";

    // Service Codes
    public static final int POST_USER_REGISTER_CODE = 1;
    public static final int POST_USER_LOGIN_CODE = 2;
    public static final int POST_USER_CURRENT_CODE = 3;
    public static final int GET_USER_CURRENT_CODE = 4;
    public static final int POST_USER_CHATS_CODE = 5;
    public static final int GET_USER_CHATS_CODE = 6;
    public static final int POST_USER_CHAT_BY_ID = 7;
    public static final int GET_USER_CHAT_BY_ID = 8;
    public static final int GET_LOCATIONS = 9;

    // Menu Options
    public static final int SIGN_IN = 1;
    public static final int SIGN_UP = 2;
    public static final int SIGN = 3;
    public static final int HOME = 4;
    public static final int HOME_NEW = 5;
    public static final int MENU = 6;
    public static final int PROFILE = 7;
    public static final int MY_LOCATIONS = 8;
    public static final int LOCATIONS_PROVIDER = 9;
    public static final int VISA_CHECKOUT = 10;

    // Open Infinite ProgressBar
    public static final String ACTION = "action.to.do";
    public static final int INDETERMINATE_PROGRESS_BAR = 1;
    public static final String OPEN_INDETERMINATE_PROGRESS_BAR = "action.open.indeterminate.progress.bar";

    // Actions
    public static final String CHAT_ROOMS_UPDATED = "android.intent.action.chat.rooms.updated";
    public static final String NO_SESSION_APP = "android.intent.action.no.session.app";
    public static final String SESSION_APP = "android.intent.action.session.app";
    public static final String OPEN_USER_PROFILE = "android.intent.action.open.user.profile";
}
