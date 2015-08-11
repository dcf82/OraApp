package com.imagination.technologies.ora.app.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatRoomDaoConfig;
    private final DaoConfig chatMessageDaoConfig;
    private final DaoConfig locationsDaoConfig;

    private final ChatRoomDao chatRoomDao;
    private final ChatMessageDao chatMessageDao;
    private final LocationsDao locationsDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatRoomDaoConfig = daoConfigMap.get(ChatRoomDao.class).clone();
        chatRoomDaoConfig.initIdentityScope(type);

        chatMessageDaoConfig = daoConfigMap.get(ChatMessageDao.class).clone();
        chatMessageDaoConfig.initIdentityScope(type);

        locationsDaoConfig = daoConfigMap.get(LocationsDao.class).clone();
        locationsDaoConfig.initIdentityScope(type);

        chatRoomDao = new ChatRoomDao(chatRoomDaoConfig, this);
        chatMessageDao = new ChatMessageDao(chatMessageDaoConfig, this);
        locationsDao = new LocationsDao(locationsDaoConfig, this);

        registerDao(ChatRoom.class, chatRoomDao);
        registerDao(ChatMessage.class, chatMessageDao);
        registerDao(Locations.class, locationsDao);
    }

    public void clear() {
        chatRoomDaoConfig.getIdentityScope().clear();
        chatMessageDaoConfig.getIdentityScope().clear();
        locationsDaoConfig.getIdentityScope().clear();
    }

    public ChatRoomDao getChatRoomDao() {
        return chatRoomDao;
    }

    public ChatMessageDao getChatMessageDao() {
        return chatMessageDao;
    }

    public LocationsDao getLocationsDao() {
        return locationsDao;
    }

}
