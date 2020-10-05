package com.android.akl.bluetoothscreamer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
public class NotificationIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String device = intent.getStringExtra("DEVICE_NAME");
        int pon = intent.getIntExtra("PON",0);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SELECTED_PAIRED_DEVICES,MODE_PRIVATE);

        if(pon == 1){
            Set<String> newAllSet = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(MainActivity.ALL_DEVICES, null)));
            Set<String> newSelectedList = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(MainActivity.SELECTED_DEVICES, null)));
            newAllSet.add(device);
            newSelectedList.add(device);
            sharedPreferences.edit().putStringSet(MainActivity.ALL_DEVICES,newAllSet).apply();
            sharedPreferences.edit().putStringSet(MainActivity.SELECTED_DEVICES,newSelectedList).apply();
        }else{
            Set<String> newAllSet = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(MainActivity.ALL_DEVICES, null)));
            Set<String> newNotSelectedList = new HashSet<>(Objects.requireNonNull(sharedPreferences.getStringSet(MainActivity.NOT_SELECTED_DEVICES, null)));
            newAllSet.add(device);
            newNotSelectedList.add(device);
            sharedPreferences.edit().putStringSet(MainActivity.ALL_DEVICES,newAllSet).apply();
            sharedPreferences.edit().putStringSet(MainActivity.NOT_SELECTED_DEVICES,newNotSelectedList).apply();
        }
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancel(11111);
    }
}
