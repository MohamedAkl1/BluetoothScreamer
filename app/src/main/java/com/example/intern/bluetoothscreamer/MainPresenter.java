package com.example.intern.bluetoothscreamer;

/**
 * Created by Mohamed Akl on 8/25/2018.
 */
public class MainPresenter {

    private MainView mMainView;

    public MainPresenter(MainView mainView) {
        mMainView = mainView;
    }

    public boolean checkSharedPreferences(String selectedPairedDevices) {
        return mMainView.isPrefAvailable(selectedPairedDevices);
    }

    public void createSharedPreferences(){
        mMainView.setupSharedPreferences();
    }

    public void displayDevices() {
        mMainView.displayBTDevicesInMainActivity();
    }
}
