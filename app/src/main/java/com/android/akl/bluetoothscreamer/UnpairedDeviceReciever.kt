package com.android.akl.bluetoothscreamer

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.akl.bluetoothscreamer.util.Constants.Companion.ALL_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.NOT_SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.SELECTED_DEVICES

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
class UnpairedDeviceReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
            if (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR) == BluetoothDevice.BOND_NONE) {
                val preferences = context.getSharedPreferences(DISPAGER_DEVICES, Context.MODE_PRIVATE)
                val selectedDevices = preferences.getStringSet(SELECTED_DEVICES, null)
                val notSelectedDevices = preferences.getStringSet(NOT_SELECTED_DEVICES, null)
                val allDevices = preferences.getStringSet(ALL_DEVICES, null)
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (selectedDevices!!.contains(device!!.name)) {
                    selectedDevices.remove(device.name)
                    preferences.edit().putStringSet(SELECTED_DEVICES, selectedDevices).apply()
                    allDevices!!.remove(device.name)
                    preferences.edit().putStringSet(ALL_DEVICES, allDevices).apply()
                } else if (notSelectedDevices!!.contains(device.name)) {
                    notSelectedDevices.remove(device.name)
                    preferences.edit().putStringSet(NOT_SELECTED_DEVICES, notSelectedDevices).apply()
                    allDevices!!.remove(device.name)
                    preferences.edit().putStringSet(ALL_DEVICES, allDevices).apply()
                }
            }
        }
    }
}