package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Objects;
import java.util.Set;

import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_PAIRED_DEVICES;

public class DisconnectionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), BluetoothDevice.ACTION_ACL_DISCONNECTED)){
            if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
                SharedPreferences sharedPreferences = context.getSharedPreferences(SELECTED_PAIRED_DEVICES,Context.MODE_PRIVATE);
                Set<String> selectedDevices = sharedPreferences.getStringSet(SELECTED_DEVICES,null);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                assert selectedDevices != null;
                if(selectedDevices.contains(device.getName())){
                    Intent i = new Intent(context,DisconnectionActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("device_name",device.getName());
                    context.startActivity(i);
                }
            }
        }
    }
}
