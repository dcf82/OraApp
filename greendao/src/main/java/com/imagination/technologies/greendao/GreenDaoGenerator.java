package com.imagination.technologies.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.imagination.technologies.ora.app.greendao");

        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        addChat(schema);
        addChatSession(schema);
        addLocations(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    public static void addChat(Schema schema) {
        Entity chatRoom = schema.addEntity("ChatRoom");
        chatRoom.addIdProperty().autoincrement();
        chatRoom.addIntProperty("kChatRoomId");
        chatRoom.addIntProperty("kUserId");
        chatRoom.addStringProperty("kChatRoomName");
    }

    public static void addChatSession(Schema schema) {
        Entity chatSession = schema.addEntity("ChatMessage");
        chatSession.addIdProperty().autoincrement();
        chatSession.addIntProperty("kChatId");
        chatSession.addIntProperty("kMessageId");
        chatSession.addIntProperty("kSenderId");
        chatSession.addStringProperty("kMessage");
        chatSession.addStringProperty("kUserName");
    }

    public static void addLocations(Schema schema) {
        Entity chatSession = schema.addEntity("Locations");
        chatSession.addIdProperty().autoincrement();
        chatSession.addStringProperty("kLongitude");
        chatSession.addStringProperty("kZipcode");
        chatSession.addStringProperty("kZipClass");
        chatSession.addStringProperty("kCounty");
        chatSession.addStringProperty("kCity");
        chatSession.addStringProperty("kState");
        chatSession.addStringProperty("kLatitude");
    }
}
