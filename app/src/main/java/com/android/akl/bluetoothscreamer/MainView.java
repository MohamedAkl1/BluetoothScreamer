package com.android.akl.bluetoothscreamer;

import java.util.List;

/**
 * Created by Mohamed Akl on 8/25/2018.
 */
public interface MainView {
    boolean isPrefAvailable(String selecteddPairedDevices);
    void addSharedPreferences(List<String> selectedDevices, List<String> notSelectedDevices);
    void setupSharedPreferences();
    void displayBTDevicesInMainActivity();
}
