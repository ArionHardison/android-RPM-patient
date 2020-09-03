package com.midokter.app.ui.activity.pubnub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("senderid")
    @Expose
    String senderId;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("time")
    @Expose
    String time;
    @SerializedName("message")
    @Expose
    String message;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
