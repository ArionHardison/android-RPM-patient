package com.telehealthmanager.app.ui.activity.pubnub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.telehealthmanager.app.BaseApplication;
import com.telehealthmanager.app.R;
import com.telehealthmanager.app.data.PreferenceHelper;
import com.telehealthmanager.app.data.PreferenceKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;


/**
 * Tranxit Technology
 */

public class ChatMessageAdapter extends ArrayAdapter<MessageModel> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;

    @androidx.annotation.NonNull
    PreferenceHelper preferenceHelper = new PreferenceHelper(BaseApplication.baseApplication);

    ChatMessageAdapter(@androidx.annotation.NonNull Context activity, @androidx.annotation.NonNull List<MessageModel> data) {
        super(activity, R.layout.item_mine_message, data);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel item = getItem(position);
        String str = String.valueOf(preferenceHelper.mPref.getInt(PreferenceKey.PATIENT_ID, 0));
        if (item != null && item.senderId != null && item.getSenderId().equals(str)) {
            return MY_MESSAGE;
        } else
            return OTHER_MESSAGE;
    }

    @androidx.annotation.NonNull
    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        int viewType = getItemViewType(position);
        final MessageModel chat = getItem(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);
            TextView textView = convertView.findViewById(R.id.text);
            if (chat != null) {
                textView.setText(chat.getMessage());
            }
            TextView timestamp = convertView.findViewById(R.id.timestamp);
            if (chat != null && chat.getTime() != null && !chat.getTime().isEmpty()) {
                String today = chat.getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //date null check
                if (date != null) {
                    long dateInLong = date.getTime();
                    timestamp.setText(getDisplayableTime(dateInLong));
                }
            }

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);
            TextView textView = convertView.findViewById(R.id.text);
            if (chat != null) {
                textView.setText(chat.getMessage());
            }
            TextView timestamp = convertView.findViewById(R.id.timestamp);
            if (chat != null && chat.getTime() != null && !chat.getTime().isEmpty()) {
                String today = chat.getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(today);
                    long dateInLong = date.getTime();
                    timestamp.setText(String.valueOf(getDisplayableTime(dateInLong)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return convertView;
    }

    private String getDisplayableTime(long value) {

        long difference;
        long mDate = System.currentTimeMillis();

        if (mDate > value) {
            difference = mDate - value;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 86400) {
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                return formatter.format(new Date(value));
                //return "not yet";
            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return "now";
    }
}
