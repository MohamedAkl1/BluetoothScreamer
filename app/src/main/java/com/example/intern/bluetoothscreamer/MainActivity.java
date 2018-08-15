package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "paired_bluetooth_devices";
    SharedPreferences mSharedPreferences;
    BluetoothAdapter mBluetoothAdapter;
    RecyclerView bTNamesRecyclerView;
    BTNamesRecyclerAdapter mBTNamesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        Set<String> bTDeviceNames = new HashSet<>();
        for(BluetoothDevice device: devices){
            bTDeviceNames.add(device.getName());
        }
        bTNamesRecyclerView = findViewById(R.id.bt_devices_names_rv);
        bTNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBTNamesRecyclerAdapter = new BTNamesRecyclerAdapter(this,bTDeviceNames);
        bTNamesRecyclerView.setAdapter(mBTNamesRecyclerAdapter);

        mSharedPreferences.edit().putStringSet("bluetoothDevices", bTDeviceNames).apply();
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "firstRunFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {


            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}
