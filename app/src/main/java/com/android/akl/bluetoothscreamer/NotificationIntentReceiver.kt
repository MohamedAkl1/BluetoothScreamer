package com.android.akl.bluetoothscreamer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.android.akl.bluetoothscreamer.ui.DispagerMainActivity
import com.android.akl.bluetoothscreamer.util.Constants.Companion.ALL_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.NOT_SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.SELECTED_DEVICES
import java.util.*

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
class NotificationIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val device = intent.getStringExtra("DEVICE_NAME")
        val pon = intent.getIntExtra("PON", 0)
        val sharedPreferences = context.getSharedPreferences(DISPAGER_DEVICES, Context.MODE_PRIVATE)
        if (pon == 1) {
            val newAllSet: MutableSet<String> = HashSet(Objects.requireNonNull(sharedPreferences.getStringSet(ALL_DEVICES, null)))
            val newSelectedList: MutableSet<String> = HashSet(Objects.requireNonNull(sharedPreferences.getStringSet(SELECTED_DEVICES, null)))
            newAllSet.add(device!!)
            newSelectedList.add(device)
            sharedPreferences.edit().putStringSet(ALL_DEVICES, newAllSet).apply()
            sharedPreferences.edit().putStringSet(SELECTED_DEVICES, newSelectedList).apply()
        } else {
            val newAllSet: MutableSet<String> = HashSet(Objects.requireNonNull(sharedPreferences.getStringSet(ALL_DEVICES, null)))
            val newNotSelectedList: MutableSet<String> = HashSet(Objects.requireNonNull(sharedPreferences.getStringSet(NOT_SELECTED_DEVICES, null)))
            newAllSet.add(device!!)
            newNotSelectedList.add(device)
            sharedPreferences.edit().putStringSet(ALL_DEVICES, newAllSet).apply()
            sharedPreferences.edit().putStringSet(NOT_SELECTED_DEVICES, newNotSelectedList).apply()
        }
        val managerCompat = NotificationManagerCompat.from(context)
        managerCompat.cancel(11111)
    }
}