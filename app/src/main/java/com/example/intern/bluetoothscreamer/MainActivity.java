package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.intern.bluetoothscreamer.adapter.BTNamesRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements WelcomeDialog.OnDialogSubmit, MainView {

    static final String SELECTED_PAIRED_DEVICES = "selected_paired_devices";
    static final int REQUEST_ENABLE_BT = 112;
    static final String SELECTED_DEVICES = "selected_devices";
    static final String NOT_SELECTED_DEVICES = "not_selected_devices";
    static final String ALL_DEVICES = "all_devices";
    private static final String TAG = "MainActivity";

    List<String> allDevices;
    List<String> selectedDevices;
    List<String> notSelectedDevices;
    MainPresenter mMainPresenter;
    SharedPreferences mSharedPreferences;
    BluetoothAdapter mBluetoothAdapter;
    RecyclerView selectedNamesRecyclerView, notSelectedNamesRecyclerView;
    BTNamesRecyclerAdapter mBTSelectedNamesRecyclerAdapter, mBTNamesNotSelectedRecyclerAdapter;
    Set<String> bTDeviceNames;
    WelcomeDialog dialog;
    List<Integer> devicesIndices;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new WelcomeDialog(getApplicationContext(),null);
        dialog.setMainViewListener(this);
        dialog.setDialogResult(this);

        devicesIndices = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mMainPresenter = new MainPresenter(this);
        bTDeviceNames = new HashSet<>();

        if(mMainPresenter.checkSharedPreferences(SELECTED_PAIRED_DEVICES)){
            mMainPresenter.displayDevices();
        }else{
            mMainPresenter.createSharedPreferences();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            mBluetoothAdapter.enable();
            getPairedNames();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == R.id.edit){
            if(item.getTitle().toString().toLowerCase().equals("edit")){
                new MaterialDialog.Builder(this)
                        .items(allDevices)
                        .cancelable(false)
                        .itemsCallbackMultiChoice(getSetOfIndices(selectedDevices, allDevices),
                                new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                selectedDevices.clear();
                                for (Integer aWhich : which) {
                                    selectedDevices.add(allDevices.get(aWhich));
                                }
                                mSharedPreferences.edit().putStringSet(SELECTED_DEVICES, new HashSet<>(selectedDevices)).apply();
                                List<String> mockList = new ArrayList<>(allDevices);
                                mockList.removeAll(selectedDevices);
                                mSharedPreferences.edit().putStringSet(NOT_SELECTED_DEVICES, new HashSet<>(mockList)).apply();
                                notSelectedDevices = mockList;
                                displaySelectedDevices(selectedDevices);
                                displayNotSelectedDevices(notSelectedDevices);
                                dialog.dismiss();
                                item.setTitle("edit");
                                return true;
                            }
                        })
                        .positiveText("Submit")
                        .build().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogSubmit(List<String> selectedNames, List<String> notSelectedNames) {
        selectedDevices = selectedNames;
        notSelectedDevices = notSelectedNames;
        displaySelectedDevices(selectedNames);
        displayNotSelectedDevices(notSelectedNames);
        dialog.dismiss();
    }


    @Override
    public boolean isPrefAvailable(String selectedPairedDevices) {

        mSharedPreferences = getSharedPreferences(SELECTED_PAIRED_DEVICES,MODE_PRIVATE);
        Set<String> retrievedSelectedDeviceNames = mSharedPreferences.getStringSet(SELECTED_DEVICES,null);
        Set<String> retrievedNotSelectedDeviceNames = mSharedPreferences.getStringSet(NOT_SELECTED_DEVICES,null);
        Set<String> retrievedAllDevicesNames = mSharedPreferences.getStringSet(ALL_DEVICES,null);
        int x = mBluetoothAdapter.getBondedDevices().size();
        boolean one = retrievedAllDevicesNames != null && retrievedAllDevicesNames.size() == mBluetoothAdapter.getBondedDevices().size();
        boolean two = retrievedSelectedDeviceNames != null || retrievedNotSelectedDeviceNames != null;

        return  (one) && (two);
    }

    @Override
    public void addSharedPreferences(List<String> selectedDevices, List<String> notSelectedDevices) {
        mSharedPreferences.edit().putStringSet(SELECTED_DEVICES, new HashSet<>(selectedDevices)).apply();
        mSharedPreferences.edit().putStringSet(NOT_SELECTED_DEVICES,new HashSet<>(notSelectedDevices)).apply();
        mSharedPreferences.edit().putStringSet(ALL_DEVICES, new HashSet<>(allDevices)).apply();
    }

    private Integer[] getSetOfIndices(List<String> selectedDevices, List<String> allDevices) {
        devicesIndices.clear();
        for(String device : selectedDevices){
            devicesIndices.add(allDevices.indexOf(device));
        }
        Integer[] temp = new Integer[devicesIndices.size()];
        return devicesIndices.toArray(temp);
    }

    @Override
    public void setupSharedPreferences() {
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent,REQUEST_ENABLE_BT);
        }else{
            getPairedNames();
        }
    }

    @Override
    public void displayBTDevicesInMainActivity() {
        mSharedPreferences = getSharedPreferences(SELECTED_PAIRED_DEVICES,MODE_PRIVATE);
        Set<String> retrievedSelectedDeviceNames = mSharedPreferences.getStringSet(SELECTED_DEVICES,null);
        Set<String> retrievedNotSelectedDeviceNames = mSharedPreferences.getStringSet(NOT_SELECTED_DEVICES,null);
        Set<String> retrievedAllDevicesNames = mSharedPreferences.getStringSet(ALL_DEVICES,null);

        assert retrievedAllDevicesNames != null;
        allDevices = new ArrayList<>(retrievedAllDevicesNames);
        assert retrievedSelectedDeviceNames != null;
        selectedDevices = new ArrayList<>(retrievedSelectedDeviceNames);
        assert retrievedNotSelectedDeviceNames != null;
        notSelectedDevices = new ArrayList<>(retrievedNotSelectedDeviceNames);

        displaySelectedDevices(selectedDevices);
        displayNotSelectedDevices(notSelectedDevices);
    }

    void getPairedNames(){
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device: devices){
            bTDeviceNames.add(device.getName());
        }
        dialog = new WelcomeDialog(this,bTDeviceNames);
        allDevices = new ArrayList<>(bTDeviceNames);
        dialog.show();

    }

    public void displaySelectedDevices(List<String> selectedNames){
        mBTSelectedNamesRecyclerAdapter = new BTNamesRecyclerAdapter(this,selectedNames);
        selectedNamesRecyclerView = findViewById(R.id.bt_devices_names_rv);
        selectedNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedNamesRecyclerView.setAdapter(mBTSelectedNamesRecyclerAdapter);
        Log.d(TAG, "onDisplay: selected size" + selectedDevices.size());
        Log.d(TAG, "onDisplay: not selected size" + notSelectedDevices.size());
        Log.d(TAG, "onDisplay: all size" + allDevices.size());
    }

    public void displayNotSelectedDevices(List<String> notSelectedNames){
        mBTNamesNotSelectedRecyclerAdapter = new BTNamesRecyclerAdapter(this,notSelectedNames);
        notSelectedNamesRecyclerView = findViewById(R.id.not_selected_devices_rv);
        notSelectedNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notSelectedNamesRecyclerView.setAdapter(mBTNamesNotSelectedRecyclerAdapter);
    }


}
