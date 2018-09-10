package com.android.akl.bluetoothscreamer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class DisconnectionActivity extends AppCompatActivity {

    ConstraintLayout mConstraintLayout;
    TextView mTextView;
    Button mButton;
    int temp = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnection);

        mConstraintLayout = findViewById(R.id.disconnection_activity_layout);
        mTextView = findViewById(R.id.disconnection_activity_tv);
        mButton = findViewById(R.id.disconnection_activity_button);

        String deviceName = Objects.requireNonNull(getIntent().getExtras()).getString("device_name");
        mTextView.setText(deviceName + " disconnected");
        final MediaPlayer player = MediaPlayer.create(getApplicationContext(),R.raw.siren_sound);
        player.setVolume(10,10);
        player.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(temp == 0){
                    mConstraintLayout.setBackgroundColor(Color.RED);
                    temp = 1;
                }else{
                    mConstraintLayout.setBackgroundColor(Color.WHITE);
                    temp = 0;
                }
            }
        },1000);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                finish();
            }
        });
    }
}
