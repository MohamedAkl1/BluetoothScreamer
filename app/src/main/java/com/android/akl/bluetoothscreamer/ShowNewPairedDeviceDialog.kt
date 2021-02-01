package com.android.akl.bluetoothscreamer

import android.app.Activity
import android.os.Bundle

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
class ShowNewPairedDeviceDialog : Activity() {
    var device: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        device = intent.getStringExtra("DEVICE_NAME")
    }

    override fun onStart() {
        super.onStart()
        showDialog()
    }

    private fun showDialog() {
//        MaterialDialog.Builder(applicationContext)
//                .title("Device " + device + "discovered, would you like to add it to alerted devices?")
//                .positiveText("Yes")
//                .negativeText("No")
//                .onPositive { dialog, which -> }
//                .onNegative { dialog, which -> }
//                .build()
//                .show()
    }
}