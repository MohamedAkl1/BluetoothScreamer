package com.android.akl.bluetoothscreamer

/**
 * Created by Mohamed Akl on 8/25/2018.
 */
interface MainView {
    fun isPrefAvailable(selecteddPairedDevices: String?): Boolean
    fun addSharedPreferences(selectedDevices: List<String?>?, notSelectedDevices: List<String?>?)
    fun setupSharedPreferences()
    fun displayBTDevicesInMainActivity()
}