package com.android.akl.bluetoothscreamer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.android.akl.bluetoothscreamer.repository.DispagerRepository
import com.android.akl.bluetoothscreamer.util.Constants.Companion.ALL_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.NOT_SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.SELECTED_DEVICES

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
class NotificationIntentReceiver : BroadcastReceiver() {
    private val repo: DispagerRepository = DispagerRepository()
    override fun onReceive(context: Context, intent: Intent) {
        val device = intent.getStringExtra("DEVICE_NAME")
        val pon = intent.getIntExtra("PON", 0)
        val newAllSet = repo.getAllFromPref(context)
        if(newAllSet != null && device != null){
            if (pon == 1) {
                val newSelectedList = repo.getSelectedFromPref(context)
                if(newSelectedList != null){
                    newSelectedList.add(device)
                    repo.saveSelectedFromNotification(newAllSet, selectedDevices = newSelectedList)
                }
            } else {
                val newNotSelectedList = repo.getNotSelectedFromPref(context)
                if(newNotSelectedList != null){
                    newNotSelectedList.add(device)
                    repo.saveSelectedFromNotification(newAllSet, notSelectedDevices = newNotSelectedList)
                }
            }
            newAllSet.add(device)
            val managerCompat = NotificationManagerCompat.from(context)
            managerCompat.cancel(11111)
        }
    }
}