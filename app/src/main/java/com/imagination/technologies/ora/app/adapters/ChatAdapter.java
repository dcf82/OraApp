package com.imagination.technologies.ora.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.greendao.ChatMessage;
import com.imagination.technologies.ora.app.utilities.Utility;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private static final String TAG = ChatAdapter.class.getName();
    public static final int LOCAL_USER = 1;
    public static final int REMOTE_USER = 2;
    private static final int TYPE_VIEW_COUNT = 2;

    ArrayList<ChatMessage> chats;
    ChatMessage chat;
    LayoutInflater li;
    Holder holder;

    public ChatAdapter(Context ctx, int textViewResourceId,
                           ArrayList<ChatMessage> e) {
        super(ctx, textViewResourceId, e);
        chats = e;
        li = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getViewTypeCount() {
        return TYPE_VIEW_COUNT;
    }

    public int getItemViewType(int position) {
        return determineSenderPosition(chats.get(position).getKSenderId());
    }

    protected int determineSenderPosition(int senderId) {
        int userId = Utility.readValueFromProfile(int.class, Config.USER_ID, 0);
        Log.i(TAG, "USER ID :: " + userId);
        return (userId == senderId ? 1 : 0);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null || !(view.getTag() instanceof Holder)) {
            view = buildView(position, parent);

            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.msg = (TextView)view.findViewById(R.id.message);
            holder.photo = (ImageView) view.findViewById(R.id.icon);

            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        chat = chats.get(position);

        holder.name.setText(getUserName());
        holder.photo.setImageResource(getUserLogo());
        holder.msg.setText(chat.getKMessage());

        return view;
    }

    private View buildView(int position, ViewGroup parent) {
        View view;

        switch(chats.get(position).getKSenderId()) {
            case LOCAL_USER:
                view = li.inflate(R.layout.chat_local_item, parent, false);
                break;
            case REMOTE_USER:
            default:
                view = li.inflate(R.layout.chat_remote_item, parent, false);
                break;
        }

        return view;
    }

    private String getUserName() {
        String name;

        switch(chat.getKSenderId()) {
            case LOCAL_USER:
                name = Utility.readValueFromProfile(String.class, Config.FIRST_NAME, "Me");
                break;
            case REMOTE_USER:
            default:
                name = chat.getKUserName();
                break;
        }

        return name;
    }

    private int getUserLogo() {
        int logo;

        switch(chat.getKSenderId()) {
            case LOCAL_USER:
                logo = R.drawable.ic_action_user;
                break;
            case REMOTE_USER:
            default:
                logo = R.drawable.ic_action_user;
                break;
        }

        return logo;
    }

    private class Holder {
        TextView msg;
        TextView name;
        ImageView photo;
    }
}
