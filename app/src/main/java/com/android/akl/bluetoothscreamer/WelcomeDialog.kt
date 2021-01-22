package com.android.akl.bluetoothscreamer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.akl.bluetoothscreamer.adapter.WelcomeRecyclerAdapter

/**
 * Created by Mohamed Akl on 8/22/2018.
 */
class WelcomeDialog(context: Context?, bTnames: Set<String>?) : Dialog(context!!) {

    private var bluetoothPairedNames: Set<String> = bTnames!!
    private lateinit var mOnDialogSubmit: OnDialogSubmit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_welcome)

        val devicesRV: RecyclerView = findViewById(R.id.welcome_bt_devices_rv)
        val welcomeRecyclerAdapter = WelcomeRecyclerAdapter(context, bluetoothPairedNames)
        devicesRV.layoutManager = LinearLayoutManager(context)
        devicesRV.adapter = welcomeRecyclerAdapter
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val selectedDevices: List<String> = welcomeRecyclerAdapter.selectedNames
            val notSelectedDevices: List<String> = welcomeRecyclerAdapter.notSelectedNames
            mOnDialogSubmit!!.onDialogSubmit(selectedDevices, notSelectedDevices)
        }
    }

    interface OnDialogSubmit {
        fun onDialogSubmit(selectedNames: List<String>, notSelectedNames: List<String>)
    }

    fun setDialogResult(dialogResult: OnDialogSubmit) {
        mOnDialogSubmit = dialogResult
    }
}