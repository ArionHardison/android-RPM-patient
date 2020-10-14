package com.midokter.app.ui.twilio;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.midokter.app.BaseApplication;
import com.midokter.app.BuildConfig;
import com.midokter.app.R;
import com.midokter.app.data.PreferenceHelper;
import com.midokter.app.repositary.ApiInterface;
import com.midokter.app.repositary.AppRepository;
import com.midokter.app.repositary.model.VideoCallCancelResponse;

import retrofit2.Call;
import retrofit2.Callback;


public class IncomingVideoCallActivity extends AppCompatActivity implements View.OnClickListener {

    TextView lblName;
    ImageView imgEndCall;
    ImageView imgAcceptCall;
    TextView callType;

    String chatPath, name,sender;
    String isVideo = "1";
    Ringtone ringtone;
    public static IncomingVideoCallActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        instance = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        lblName=findViewById(R.id.lblName);
        imgEndCall=findViewById(R.id.imgEndCall);
        imgAcceptCall=findViewById(R.id.imgAcceptCall);
        callType=findViewById(R.id.call_type);
        imgEndCall.setOnClickListener(this);
        imgAcceptCall.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        name = extras.getString("name");
        sender = extras.getString("sender");
        chatPath = extras.getString("chat_path");
        isVideo = extras.getString("is_video", "1");
        lblName.setText(name);

        if(isVideo.equalsIgnoreCase("1")){
            callType.setText("Incoming video call...");
        }else {
            callType.setText("Incoming audio call...");
        }

        playRingtone();
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEndCall:
                stopRingtone();
                cancelVideoCall();
                break;
            case R.id.imgAcceptCall:
                stopRingtone();
                Intent i = new Intent(getApplicationContext(), TwilloVideoActivity.class);
                i.putExtra("chat_path", chatPath);
                i.putExtra("is_video", isVideo);
                i.putExtra("sender", sender);
                startActivity(i);
                finish();
                break;
        }
    }


    private void playRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (notification != null) {
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }
    }

    private AppRepository appRepository = new AppRepository();

    private void cancelVideoCall() {
        Call<VideoCallCancelResponse> call = appRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface.class).cancelVideoCall(chatPath);
        call.enqueue(new Callback<VideoCallCancelResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideoCallCancelResponse> call, @NonNull retrofit2.Response<VideoCallCancelResponse> response) {
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<VideoCallCancelResponse> call, @NonNull Throwable t) {
            }
        });

    }


    private void stopRingtone() {
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

}
