package com.android.akl.bluetoothscreamer

import android.app.PendingIntent
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.akl.bluetoothscreamer.repository.DispagerRepository
import com.android.akl.bluetoothscreamer.util.Constants.Companion.ALL_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES

/**
 * Created by Mohamed Akl on 9/6/2018.
 */
class NewPairedDeviceReveiver : BroadcastReceiver() {

    private val repo: DispagerRepository = DispagerRepository()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
            if (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR) == BluetoothDevice.BOND_BONDED) {
                val allDevices = repo.getAllFromPref(context)
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null && !allDevices!!.contains(device.name)) {
                    val positiveIntent = Intent(context, NotificationIntentReceiver::class.java)
                    positiveIntent.putExtra("DEVICE_NAME", device.name)
                    positiveIntent.putExtra("PON", 1)
                    val positivePendingIntent = PendingIntent.getBroadcast(context, 1, positiveIntent, PendingIntent.FLAG_CANCEL_CURRENT)

                    val negativeIntent = Intent(context, NotificationIntentReceiver::class.java)
                    negativeIntent.putExtra("DEVICE_NAME", device.name)
                    negativeIntent.putExtra("PON", 0)
                    val negativePendingIntent = PendingIntent.getBroadcast(context, 0, negativeIntent, PendingIntent.FLAG_CANCEL_CURRENT)

                    val builder = NotificationCompat.Builder(context, "")
                            .setSmallIcon(R.drawable.ic_info_black_24dp)
                            .setContentTitle("Add to alerted devices?")
                            .setStyle(NotificationCompat.BigTextStyle().bigText("New paired device detected, Would you like to add it to alerted devices?"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .addAction(0, "Add", positivePendingIntent)
                            .addAction(0, "Ignore", negativePendingIntent)
                            .setAutoCancel(true)
                    val notificationManagerCompat = NotificationManagerCompat.from(context)
                    notificationManagerCompat.notify(11111, builder.build())
                }
                Toast.makeText(context, "new device paired", Toast.LENGTH_LONG).show()
            }
        }
    }
}