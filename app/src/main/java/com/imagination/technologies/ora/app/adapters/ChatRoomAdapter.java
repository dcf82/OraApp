package com.imagination.technologies.ora.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.greendao.ChatRoom;

import java.util.ArrayList;

public class ChatRoomAdapter extends ArrayAdapter<ChatRoom> {
    ArrayList<ChatRoom> chatRooms;
    ChatRoom chatRoom;
    LayoutInflater li;
    Holder holder;

    public ChatRoomAdapter(Context ctx, int textViewResourceId,
                           ArrayList<ChatRoom> e) {
        super(ctx, textViewResourceId, e);
        chatRooms = e;
        li = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null || !(view.getTag() instanceof Holder)) {
            view = li.inflate(R.layout.chat_room_item, parent, false);
            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.chatRoom);
            holder.logo = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        chatRoom = chatRooms.get(position);
        holder.name.setText(chatRoom.getKChatRoomName());

        return view;
    }

    private class Holder {
        TextView name;
        ImageView logo;
    }
}
