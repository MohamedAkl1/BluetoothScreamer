package com.example.intern.bluetoothscreamer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class DisconnectionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"BLUETOOTH DISCONNECTED EL7A2", Toast.LENGTH_LONG).show();
        MediaPlayer player = MediaPlayer.create(context,R.raw.siren_sound);
        player.start();
    }
}
