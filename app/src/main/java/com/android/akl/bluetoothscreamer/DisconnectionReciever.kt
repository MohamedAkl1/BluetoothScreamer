package com.android.akl.bluetoothscreamer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.akl.bluetoothscreamer.util.Constants.Companion.SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES

class DisconnectionReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                val sharedPreferences = context.getSharedPreferences(DISPAGER_DEVICES, Context.MODE_PRIVATE)
                val selectedDevices = sharedPreferences.getStringSet(SELECTED_DEVICES, null)
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (selectedDevices!!.contains(device?.name)) {
                    val i = Intent(context, DisconnectionActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.putExtra("device_name", device?.name)
                    context.startActivity(i)
                }
            }
        }
    }
}