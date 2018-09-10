package com.android.akl.bluetoothscreamer;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
public class UnpairedDeviceReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
            if(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,BluetoothDevice.ERROR) == BluetoothDevice.BOND_NONE){
                SharedPreferences preferences = context.getSharedPreferences(MainActivity.SELECTED_PAIRED_DEVICES,Context.MODE_PRIVATE);
                Set<String> selectedDevices = preferences.getStringSet(MainActivity.SELECTED_DEVICES,null);
                Set<String> notSelectedDevices = preferences.getStringSet(MainActivity.NOT_SELECTED_DEVICES,null);
                Set<String> allDevices = preferences.getStringSet(MainActivity.ALL_DEVICES,null);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(selectedDevices.contains(device.getName())){
                    selectedDevices.remove(device.getName());
                    preferences.edit().putStringSet(MainActivity.SELECTED_DEVICES,selectedDevices).apply();
                    allDevices.remove(device.getName());
                    preferences.edit().putStringSet(MainActivity.ALL_DEVICES,allDevices).apply();
                }else if (notSelectedDevices.contains(device.getName())){
                    notSelectedDevices.remove(device.getName());
                    preferences.edit().putStringSet(MainActivity.NOT_SELECTED_DEVICES,notSelectedDevices).apply();
                    allDevices.remove(device.getName());
                    preferences.edit().putStringSet(MainActivity.ALL_DEVICES,allDevices).apply();
                }
            }
        }
    }
}
