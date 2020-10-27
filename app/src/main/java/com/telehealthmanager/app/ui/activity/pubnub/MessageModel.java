package com.telehealthmanager.app.ui.activity.pubnub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("senderId")
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
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("request_id")
    @Expose
    String request_id;
    @SerializedName("provider_id")
    @Expose
    String provider_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

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
