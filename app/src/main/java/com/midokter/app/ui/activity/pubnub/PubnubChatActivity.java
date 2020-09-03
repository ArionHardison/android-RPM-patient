package com.midokter.app.ui.activity.pubnub;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.midokter.app.BuildConfig;
import com.midokter.app.R;
import com.midokter.app.repositary.model.chatmodel.Chat;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.TextView.OnEditorActionListener;

public class PubnubChatActivity extends AppCompatActivity {
    EditText message;
    ListView chatLv;
    ImageView send;
    ImageView profileImage;
    ImageView ivToolbarBack;
    TextView tvToolbarName;

    private static final String TAG = "PUBNUB_CHAT_USER";
    public static final int TYPE = 1;

    public static final String PUBNUB_PUBLISH_KEY = "pub-c-b07e3612-dbd2-4e0c-9394-c5d7430c9606";
    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-6b66e3cc-25eb-11e8-97e5-2e7e45341bc1";

    @Nullable
    public String PUBNUB_CHANNEL_NAME = "Queenbeee";
    PubNub pubnub;
    ChatMessageAdapter mAdapter;
    @NonNull
    Gson gson = new Gson();
    @NonNull
    String chat_request_id = "";
    @NonNull
    String chat_provider_id = "";
    @NonNull
    String chat_provider_name = "";
    @NonNull
    String chat_device_token = "";
    @Nullable
    String current_Member = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubnub_chat);
        chatLv = findViewById(R.id.chat_lv);
        send = findViewById(R.id.send);
        message = findViewById(R.id.message);
        ivToolbarBack = findViewById(R.id.backArrow);
        tvToolbarName = findViewById(R.id.lblTitle);
        profileImage = findViewById(R.id.profileImage);

        Chat chatData = (Chat) getIntent().getSerializableExtra("chat_data");
        assert chatData != null;
        chat_request_id = "" + chatData.getPatientId();
        if(chatData.getHospital()!=null) {
            chat_provider_id = "" + chatData.getHospital().getId();
            chat_provider_name = "" + chatData.getHospital().getFirst_name() + " " + chatData.getHospital().getLast_name();
        }
        chat_device_token = "";

        if (chatData != null&&chatData.getHospital()!=null) {
            current_Member = chatData.getHospital().getFirst_name();
            Glide.with(this)
                    .load(BuildConfig.BASE_URL + "storage/" + chatData.getHospital().getDoctor_profile().getProfile_pic())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.user_placeholder)
                            .error(R.drawable.user_placeholder))
                    .into(profileImage);
        } else {
            current_Member = getResources().getString(R.string.unknown);
        }


        PUBNUB_CHANNEL_NAME = chatData.getChennel();
        tvToolbarName.setText(current_Member);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.length() > 0) {
                    sendMessage(message.getText().toString());
                }
            }
        });

        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //  PUBNUB_CHANNEL_NAME = "autolinks_app_" + chat_request_id;

        initSetup();
    }

    void initSetup() {
        mAdapter = new ChatMessageAdapter(this, new ArrayList<MessageModel>());
        chatLv.setAdapter(mAdapter);
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSubscribeKey(PUBNUB_SUBSCRIBE_KEY);
        pubnub = new PubNub(pnConfiguration);
        pubnub.history()
                .channel(PUBNUB_CHANNEL_NAME)
                .count(100)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(@Nullable PNHistoryResult result, @NonNull PNStatus status) {
                        System.out.println(result + " History " + status);
                       /* pubnub.addPushNotificationsOnChannels()
                                .pushType(PNPushType.GCM)
                                .channels(Collections.singletonList(PUBNUB_CHANNEL_NAME))
                                .deviceId(chat_device_token)
                                .async(new PNCallback<PNPushAddChannelResult>() {
                                    @Override
                                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                                        Log.e(TAG, "onResponse: test" + result);
                                    }
                                });*/
                        if (!status.isError() && result != null) {
                            List<PNHistoryItemResult> list = result.getMessages();
                            for (PNHistoryItemResult object : list) {
                                System.out.println(object.getEntry());
                                try {
                                    String message = object.getEntry().toString();
                                    Log.e(TAG, "onResponse: History" + message);
                                    MessageModel messageObject = gson.fromJson(message, MessageModel.class);
                                    addToAdapter(messageObject);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                });

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, @NonNull PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, @NonNull PNMessageResult message) {
                if (message.getChannel() != null) {
                    System.out.println(message.getMessage());
                    Log.e(TAG, "message: receiving " + message);
                    try {
                        String mess = message.getMessage().toString();
                        Log.e(TAG, "message: mess" + mess);
                        final MessageModel messageObject = gson.fromJson(mess, MessageModel.class);
                        Log.e(TAG, "message: MyMessage" + messageObject);
                        addToAdapter(messageObject);
                    } catch (@NonNull IllegalStateException | JsonSyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Collections.singletonList(PUBNUB_CHANNEL_NAME)).execute();

        message.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String dd = message.getText().toString().trim();
                    if (dd.length() > 0) {
                        send.setVisibility(View.VISIBLE);
                        sendMessage(dd);
                    } else {
                        send.setVisibility(View.GONE);
                    }
                    handled = true;
                }
                return handled;
            }
        });


    }


    void addToAdapter(final MessageModel messageObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.add(messageObject);
            }
        });
    }


    private void sendMessage(String messageStr) {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> gcmPayload = new HashMap<>();
        Map<String, Object> apnPayload = new HashMap<>();
        //Message
        JsonObject jObj = new JsonObject();

        jObj.addProperty("type", "text");
        jObj.addProperty("message", messageStr);

        long val = System.currentTimeMillis();
        Date date = new Date(val);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jObj.addProperty("time", dateFormat.format(date));
        jObj.addProperty("senderid", chat_request_id);


      /*  jObj.addProperty("read", 0);
        jObj.addProperty("created_at", System.currentTimeMillis());
        jObj.addProperty("dispatcher_id", chat_request_id);
        jObj.addProperty("driver_id", chat_provider_id);
        //For iOS Push
        JsonObject apsjObj = new JsonObject();
        JsonObject apsAlert = new JsonObject();
        apsAlert.addProperty("title", current_Member);
        apsAlert.addProperty("body", messageStr);
        apsjObj.add("alert", apsAlert);
        apsjObj.addProperty("badge", 0);
        apsjObj.addProperty("sound", "default");
        //For Android Push
        JsonObject notificationObj = new JsonObject();
        notificationObj.addProperty("title", current_Member);
        notificationObj.addProperty("body", messageStr);
        *//*Payload for both Android and iOS*//*
        //For android pn_gcm
        gcmPayload.put("notification", notificationObj);
        gcmPayload.put("data", jObj);
        //For ios pn_apns
        apnPayload.put("aps", apsjObj);
        apnPayload.put("message", jObj);
        //Load payload with both Android and iOS data
        payload.put("pn_apns", apnPayload);
        payload.put("pn_gcm", gcmPayload);*/
        //For iOS Payload Message
        Log.e(TAG, "Final payload: " + jObj);
        pubnub.publish().channel(PUBNUB_CHANNEL_NAME).message(jObj).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, @NonNull PNStatus status) {
                if (!status.isError()) {
                    message.setText("");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        pubnub.subscribe().channels(Collections.singletonList(PUBNUB_CHANNEL_NAME)).execute();
        // getAppInstance().putString(AutolinksApplication.Keyname.IS_CHAT_OPEN, AutolinksApplication.Keyname.TRUE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        Log.d(TAG, "subscribed");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DESTROYED" + System.currentTimeMillis());
        // getAppInstance().putString(AutolinksApplication.Keyname.IS_CHAT_OPEN, AutolinksApplication.Keyname.FALSE);
        pubnub.unsubscribe().channels(Collections.singletonList(PUBNUB_CHANNEL_NAME)).execute();
        // getAppInstance().putString(PUBNUB_CHANNEL_NAME, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //   getAppInstance().putString(AutolinksApplication.Keyname.IS_CHAT_OPEN, AutolinksApplication.Keyname.FALSE);
    }

}
