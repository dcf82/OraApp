package com.imagination.technologies.ora.app.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.adapters.ChatAdapter;
import com.imagination.technologies.ora.app.beans.ChatMessageResponse;
import com.imagination.technologies.ora.app.beans.ChatMessagesResponse;
import com.imagination.technologies.ora.app.beans.Message;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.greendao.ChatMessage;
import com.imagination.technologies.ora.app.greendao.ChatRoom;
import com.imagination.technologies.ora.app.network.LoadServices;
import com.imagination.technologies.ora.app.network.Service;
import com.imagination.technologies.ora.app.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {
    ChatRoom room;
    @Bind(R.id.chats)
    ListView chats;
    @Bind(R.id.chatMsg)
    EditText chatMsg;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    boolean isProcessing;
    ChatAdapter mAdapter;
    ArrayList<ChatMessage> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        loadUI1();

        if (room == null) {
            finish();
            return;
        }

        showGlobalContextActionBar(room.getKChatRoomName());

        loadUI2();

        fillBuffer();

        getMessagesByChatId();
    }

    protected void loadUI1() {
        loadToolBar();
        setUpActionBar();
        loadChatRoom();
    }

    protected void loadUI2() {
        mAdapter = new ChatAdapter(this, R.layout.chat_local_item, list);
        chats.setAdapter(mAdapter);
    }

    protected void loadToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void loadChatRoom() {
        int id = getIntent().getIntExtra(Config.CHAT_ID, -1);
        room = Utility.getChatRoomById(id);
    }

    protected void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @OnClick({ R.id.sendMsg })
    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.sendMsg:
                String msg = chatMsg.getText().toString().trim();
                if (msg.equals("")) return;
                Map map = new HashMap();
                map.put("message", msg);
                addMessage(map);
                break;
        }
    }

    protected void addMessage(Map message) {
        if (isProcessing) return;

        Service mService = new Service();
        mService.setServiceCode(Config.POST_USER_CHAT_BY_ID);
        mService.setServiceName(Config.GAP_USER_CHATS + "/" + room.getKChatRoomId()
                + Config.GAP_USER_CHAT_BY_ID);
        mService.setServiceType(Config.POST);
        mService.setHeaders(Utility.getJsonAccessAndAuthorization(Utility.
                readValueFromProfile(String.class, Config.USER_TOKEN, "")));
        mService.setServiceInput(message);
        mService.setNotificationTask(this);
        new LoadServices().loadOnExecutor(mService);

        isProcessing = true;
        openIndeterminateBar(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showGlobalContextActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    protected void getMessagesByChatId() {
        if (isProcessing) return;

        Service mService = new Service();
        mService.setServiceCode(Config.GET_USER_CHAT_BY_ID);
        mService.setServiceName(Config.GAP_USER_CHATS + "/" + room.getKChatRoomId()
                + Config.GAP_USER_CHAT_BY_ID);
        mService.setServiceType(Config.GET);
        mService.setHeaders(Utility.getJsonAccessAndAuthorization(Utility.
                readValueFromProfile(String.class, Config.USER_TOKEN, "")));
        mService.setNotificationTask(this);
        new LoadServices().loadOnExecutor(mService);

        isProcessing = true;
        openIndeterminateBar(true);
    }


    @Override
    public void completed(Service response) {
        isProcessing = false;
        openIndeterminateBar(false);

        switch(response.getServiceCode()) {

            case Config.GET_USER_CHAT_BY_ID: {
                ChatMessagesResponse rr;

                try {
                    rr = Utility.parseJSON(response.getOutput(),
                            ChatMessagesResponse.class);
                    if (rr == null) {
                        Utility.showToast("server not responded");
                    } else {
                        if (rr.isSuccess()) {
                            fillBuffer(rr.getData());
                        }
                    }
                } catch (Exception e) {
                    Utility.showToast("server not responded");
                }
            }
            break;
            case Config.POST_USER_CHAT_BY_ID:
                ChatMessageResponse rr;

                try {
                    rr = Utility.parseJSON(response.getOutput(),
                            ChatMessageResponse.class);
                    if (rr == null) {
                        Utility.showToast("server not responded");
                    } else {
                        if (rr.isSuccess() && fillMessage(rr.getData())) {
                            chatMsg.setText("");
                        } else {
                            Utility.showToast("server did not respond properly");
                        }
                    }
                } catch (Exception e) {
                    Utility.showToast("server not responded");
                }
                break;
            default:
                break;
        }
    }

    protected void fillBuffer(ArrayList<Message> messages) {
        List<ChatMessage> bufferMessages = null;
        ChatMessage dbMessage;

        if (messages != null && messages.size() > 0) {
            bufferMessages = new ArrayList<>();

            for (Message message : messages) {
                dbMessage = new ChatMessage();
                dbMessage.setKChatId(room.getKChatRoomId());
                dbMessage.setKMessage(message.getMessage());
                dbMessage.setKMessageId(message.getId());
                dbMessage.setKUserName(message.getUser().getName());
                dbMessage.setKSenderId(message.getUser_id());
                bufferMessages.add(dbMessage);
            }

            Utility.fillChatMessagesByChatId(bufferMessages,
                    bufferMessages.get(0).getKChatId());

            fillBuffer();
        }
    }

    protected boolean fillMessage(Message message) {
        ChatMessage dbMessage;

        dbMessage = new ChatMessage();
        dbMessage.setKChatId(room.getKChatRoomId());
        dbMessage.setKMessage(message.getMessage());
        dbMessage.setKMessageId(message.getId());
        dbMessage.setKUserName(message.getUser().getName());
        dbMessage.setKSenderId(message.getUser_id());


        if (list.contains(dbMessage)) return false;

        Utility.fillChatMessageByChatId(dbMessage);

        fillBuffer();

        return true;
    }

    protected void fillBuffer() {
        mAdapter.clear();
        mAdapter.addAll(Utility.getChatMessagesByChatId(
                room.getKChatRoomId()));
    }

    public void openIndeterminateBar(boolean open) {
        if (open) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
