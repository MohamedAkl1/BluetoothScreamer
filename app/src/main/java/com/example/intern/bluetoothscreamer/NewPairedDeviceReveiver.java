package com.example.intern.bluetoothscreamer;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Objects;
import java.util.Set;

import static com.example.intern.bluetoothscreamer.MainActivity.ALL_DEVICES;
import static com.example.intern.bluetoothscreamer.MainActivity.SELECTED_PAIRED_DEVICES;

/**
 * Created by Mohamed Akl on 9/6/2018.
 */
public class NewPairedDeviceReveiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
            if(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,BluetoothDevice.ERROR) == BluetoothDevice.BOND_BONDED){
                final SharedPreferences sharedPreferences = context.getSharedPreferences(SELECTED_PAIRED_DEVICES,Context.MODE_PRIVATE);
                Set<String> allDevices = sharedPreferences.getStringSet(ALL_DEVICES, null);
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                assert allDevices != null;
                if(!allDevices.contains(device.getName())){

                    Intent positiveIntent = new Intent(context,NotificationIntentReceiver.class);
                    positiveIntent.putExtra("DEVICE_NAME",device.getName());
                    positiveIntent.putExtra("PON", 1);

                    PendingIntent positivePendingIntent = PendingIntent.getBroadcast(context,1, positiveIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    Intent negativeIntent = new Intent(context,NotificationIntentReceiver.class);
                    negativeIntent.putExtra("DEVICE_NAME", device.getName());
                    negativeIntent.putExtra("PON",0);

                    PendingIntent negativePendingIntent = PendingIntent.getBroadcast(context,0,negativeIntent,PendingIntent.FLAG_CANCEL_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"")
                            .setSmallIcon(R.drawable.ic_info_black_24dp)
                            .setContentTitle("Add to alerted devices?")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("New paired device detected, Would you like to add it to alerted devices?"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .addAction(0,"Add",positivePendingIntent)
                            .addAction(0,"Ignore",negativePendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    notificationManagerCompat.notify(11111,builder.build());
                }
                Toast.makeText(context,"new device paired",Toast.LENGTH_LONG).show();
            }
        }
    }
}
