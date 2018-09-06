package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.Set;

import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_PAIRED_DEVICES;

public class DisconnectionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(BluetoothAdapter.STATE_DISCONNECTED)){
            SharedPreferences sharedPreferences = context.getSharedPreferences(SELECTED_PAIRED_DEVICES,Context.MODE_PRIVATE);
            Set<String> selectedDevices = sharedPreferences.getStringSet(SELECTED_DEVICES,null);
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            assert selectedDevices != null;
            if(selectedDevices.contains(device.getName())){
                Toast.makeText(context,"BLUETOOTH DISCONNECTED EL7A2", Toast.LENGTH_LONG).show();
                MediaPlayer player = MediaPlayer.create(context,R.raw.siren_sound);
                player.setVolume(10,10);
                player.start();
            }
        }
    }
}
