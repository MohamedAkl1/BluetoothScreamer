package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.example.intern.bluetoothscreamer.MainActivity.ALL_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.NOT_SELECTED_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_PAIRED_DEVICES;

/**
 * Created by Mohamed Akl on 9/6/2018.
 */
public class NewPairedDeviceReveiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
            final SharedPreferences sharedPreferences = context.getSharedPreferences(SELECTED_PAIRED_DEVICES,Context.MODE_PRIVATE);
            Set<String> allDevices = sharedPreferences.getStringSet(ALL_DEVICES, null);
            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if(!allDevices.contains(device.getName())){
                new MaterialDialog.Builder(context)
                        .title("Device " + device.getName() + "discovered, would you like to add it to alerted devices?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Set<String> newAllSet = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(ALL_DEVICES, null)));
                                Set<String> newSelectedList = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(SELECTED_DEVICES, null)));
                                newAllSet.add(device.getName());
                                newSelectedList.add(device.getName());
                                sharedPreferences.edit().putStringSet(ALL_DEVICES,newAllSet).apply();
                                sharedPreferences.edit().putStringSet(SELECTED_DEVICES,newSelectedList).apply();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Set<String> newAllSet = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(ALL_DEVICES, null)));
                                Set<String> newNotSelectedList = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(NOT_SELECTED_DEVICES, null)));
                                newAllSet.add(device.getName());
                                newNotSelectedList.add(device.getName());
                                sharedPreferences.edit().putStringSet(ALL_DEVICES,newAllSet).apply();
                                sharedPreferences.edit().putStringSet(NOT_SELECTED_DEVICES,newNotSelectedList).apply();
                            }
                        })
                        .build()
                        .show();
            }
        }
    }
}
