package com.android.akl.bluetoothscreamer.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.akl.bluetoothscreamer.repository.DispagerRepository

/**
 * Created by Mohamed Akl on 1/22/2021.
 */
class DispagerMainViewModel() : ViewModel() {

    private val dispagerRepository: DispagerRepository = DispagerRepository()
    val selectedDevicesLiveData: LiveData<MutableSet<String>?> = dispagerRepository.selectedDevices
    val notSelectedDevicesLiveData: LiveData<MutableSet<String>?> = dispagerRepository.notSelectedDevices
    val allDevicesLiveData: LiveData<MutableSet<String>?> = dispagerRepository.allDevices

    fun checkSharedPreferences(context: Context): Boolean {
        return dispagerRepository.isPreferencesAvailable(context)
    }

    fun getPairedNames(){
        dispagerRepository.getPairedNames()
    }

    fun saveSelectedDevices(selectedNames: List<String>, notSelectedNames: List<String>) {
        dispagerRepository.saveSelectedDevices(selectedNames, notSelectedNames)
    }

}