package com.android.akl.bluetoothscreamer.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.akl.bluetoothscreamer.R
import com.android.akl.bluetoothscreamer.WelcomeDialog
import com.android.akl.bluetoothscreamer.WelcomeDialog.OnDialogSubmit
import com.android.akl.bluetoothscreamer.adapter.BTNamesRecyclerAdapter
import com.android.akl.bluetoothscreamer.util.Constants.Companion.REQUEST_ENABLE_BT

class DispagerMainActivity : AppCompatActivity(), OnDialogSubmit {

    private val dispagerMainViewModel: DispagerMainViewModel by viewModels()
    private val tag = "DispagerMainActivity"
    private var mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var selectedNamesRecyclerView: RecyclerView
    private lateinit var notSelectedNamesRecyclerView: RecyclerView
    private lateinit var mBTSelectedNamesRecyclerAdapter: BTNamesRecyclerAdapter
    private lateinit var mBTNamesNotSelectedRecyclerAdapter: BTNamesRecyclerAdapter
    private lateinit var dialog: WelcomeDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        selectedNamesRecyclerView = findViewById(R.id.bt_devices_names_rv)
        notSelectedNamesRecyclerView = findViewById(R.id.not_selected_devices_rv)

        if (!dispagerMainViewModel.checkSharedPreferences(this)) {
            setupSharedPreferences()
        }
        observeValues()
    }

    private fun observeValues() {
        dispagerMainViewModel.allDevicesLiveData.observe(this, {
            if(dispagerMainViewModel.selectedDevicesLiveData.value == null &&
                    dispagerMainViewModel.notSelectedDevicesLiveData.value == null&&
                    it != null)
                displayWelcomeDialog(it)
        })

        dispagerMainViewModel.selectedDevicesLiveData.observe(this, {
            if(it != null && it.isNotEmpty())
                displaySelectedDevices(it.toList())
            else
                displaySelectedDevices(mutableListOf())
        })

        dispagerMainViewModel.notSelectedDevicesLiveData.observe(this, {
            if (it != null && it.isNotEmpty())
                displayNotSelectedDevices(it.toList())
            else
                displayNotSelectedDevices(mutableListOf())
        })
    }

    private fun displayWelcomeDialog(mutableSet: MutableSet<String>){
        dialog = WelcomeDialog(this, mutableSet.toSet())
        dialog.setDialogResult(this)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            mBluetoothAdapter.enable()
            dispagerMainViewModel.getPairedNames()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDialogSubmit(selectedNames: List<String>, notSelectedNames: List<String>) {
        dispagerMainViewModel.saveSelectedDevices(selectedNames, notSelectedNames)
        displaySelectedDevices(selectedNames)
        displayNotSelectedDevices(notSelectedNames)
        dialog.dismiss()
    }

    private fun setupSharedPreferences() {
        if (!mBluetoothAdapter.isEnabled) {
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
        } else {
            dispagerMainViewModel.getPairedNames()
        }
    }

    private fun displaySelectedDevices(selectedNames: List<String>?) {
        mBTSelectedNamesRecyclerAdapter = BTNamesRecyclerAdapter(this, selectedNames)
        selectedNamesRecyclerView.layoutManager = LinearLayoutManager(this)
        selectedNamesRecyclerView.adapter = mBTSelectedNamesRecyclerAdapter
        Log.d(tag, "onDisplay: selected size" + selectedNames!!.size)
    }

    private fun displayNotSelectedDevices(notSelectedNames: List<String>?) {
        mBTNamesNotSelectedRecyclerAdapter = BTNamesRecyclerAdapter(this, notSelectedNames)
        notSelectedNamesRecyclerView.layoutManager = LinearLayoutManager(this)
        notSelectedNamesRecyclerView.adapter = mBTNamesNotSelectedRecyclerAdapter
        Log.d(tag, "onDisplay: not selected size" + notSelectedNames!!.size)
    }
}
