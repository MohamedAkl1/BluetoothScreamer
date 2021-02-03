package com.android.akl.bluetoothscreamer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.akl.bluetoothscreamer.repository.DispagerRepository
import com.android.akl.bluetoothscreamer.util.Helper

class DisconnectionReciever : BroadcastReceiver() {
    val repo: DispagerRepository = DispagerRepository()
    override fun onReceive(context: Context, intent: Intent) {
        if(!Helper.isOnOffHours(context)){
            if (intent.action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    val selectedDevices = repo.getSelectedFromPref(context)
                    if(selectedDevices != null){
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        if (selectedDevices.contains(device?.name)) {
                            val i = Intent(context, DisconnectionActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            i.putExtra("device_name", device?.name)
                            context.startActivity(i)
                        }
                    }
                }
            }
        }
    }
}