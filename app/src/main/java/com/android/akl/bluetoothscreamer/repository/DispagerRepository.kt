package com.android.akl.bluetoothscreamer.repository

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.android.akl.bluetoothscreamer.util.Constants.Companion.ALL_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.NOT_SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.SELECTED_DEVICES
import com.android.akl.bluetoothscreamer.util.Constants.Companion.DISPAGER_DEVICES

/**
 * Created by Mohamed Akl on 1/22/2021.
 */
class DispagerRepository {

    private var sharedPreferences: SharedPreferences? = null
    var selectedDevices: MutableLiveData<MutableSet<String>?> = MutableLiveData<MutableSet<String>?>()
    var notSelectedDevices: MutableLiveData<MutableSet<String>?> = MutableLiveData<MutableSet<String>?>()
    var allDevices: MutableLiveData<MutableSet<String>?> = MutableLiveData<MutableSet<String>?>()
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun isPreferencesAvailable(context: Context): Boolean{
        sharedPreferences = context.getSharedPreferences(DISPAGER_DEVICES, AppCompatActivity.MODE_PRIVATE)
        return if (sharedPreferences != null){
            selectedDevices.value = sharedPreferences?.getStringSet(SELECTED_DEVICES, null)
            notSelectedDevices.value = sharedPreferences?.getStringSet(NOT_SELECTED_DEVICES, null)
            allDevices.value = sharedPreferences?.getStringSet(ALL_DEVICES, null)
            val one = allDevices.value != null && allDevices.value?.size == bluetoothAdapter.bondedDevices.size
            val two = selectedDevices.value != null || notSelectedDevices.value != null
            one && two
        }
        else
            false
    }

    fun isBTOn(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun getPairedNames() {
        val devices = bluetoothAdapter.bondedDevices

        if (allDevices.value == null){
            allDevices.value = mutableSetOf()
        }
        allDevices.value?.clear()
        for (device in devices) {
            allDevices.value!!.add(device.name)
        }
    }

    fun saveSelectedDevices(selectedNames: List<String>, notSelectedNames: List<String>) {
        selectedDevices.value = selectedNames.toMutableSet()
        notSelectedDevices.value = notSelectedNames.toMutableSet()
        addToSharedPreferences()
    }

    private fun addToSharedPreferences() {
        sharedPreferences!!.edit(commit = true){
            putStringSet(SELECTED_DEVICES, selectedDevices.value)
            putStringSet(NOT_SELECTED_DEVICES, notSelectedDevices.value)
            putStringSet(ALL_DEVICES, allDevices.value)
        }
    }
}