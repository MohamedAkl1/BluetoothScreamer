package com.example.intern.bluetoothscreamer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.intern.bluetoothscreamer.adapter.BTNamesRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements WelcomeDialog.OnDialogSubmit, MainView {

    static final String SELECTED_PAIRED_DEVICES = "selected_paired_devices";
    static final int REQUEST_ENABLE_BT = 112;
    static final String SELECTED_DEVICES = "selected_devices";
    static final String NOT_SELECTED_DEVICES = "not_selected_devices";


    List<String> selectedDevices;
    List<String> notSelectedDevices;
    MainPresenter mMainPresenter;
    SharedPreferences mSharedPreferences;
    BluetoothAdapter mBluetoothAdapter;
    RecyclerView selectedNamesRecyclerView, notSelectedNamesRecyclerView;
    BTNamesRecyclerAdapter mBTSelectedNamesRecyclerAdapter, mBTNamesNotSelectedRecyclerAdapter;
    Set<String> bTDeviceNames;
    WelcomeDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new WelcomeDialog(getApplicationContext(),null);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mMainPresenter = new MainPresenter(this);
        bTDeviceNames = new HashSet<>();

        if(mMainPresenter.checkSharedPreferences(SELECTED_PAIRED_DEVICES)){
            mMainPresenter.displayDevices();
        }else{
            mMainPresenter.createSharedPreferences();
        }
        dialog.setDialogResult(this);
        dialog.setMainViewListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit){
            if(item.getTitle().toString().toLowerCase().equals("edit")){
                displaySelectedDevices(selectedDevices,true);
                displayNotSelectedDevices(notSelectedDevices,true);
                item.setTitle("Submit");
            }else{

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogSubmit(List<String> selectedNames, List<String> notSelectedNames) {
        this.selectedDevices = selectedNames;
        this.notSelectedDevices = notSelectedNames;
        displaySelectedDevices(selectedNames,false);
        displayNotSelectedDevices(notSelectedNames,false);
        dialog.dismiss();
    }


    @Override
    public boolean isPrefAvailable(String selectedPairedDevices) {

        mSharedPreferences = getSharedPreferences(SELECTED_PAIRED_DEVICES,MODE_PRIVATE);
        Set<String> retrievedSelectedDeviceNames = mSharedPreferences.getStringSet(SELECTED_DEVICES,null);
        Set<String> retrievedNotSelectedDeviceNames = mSharedPreferences.getStringSet(NOT_SELECTED_DEVICES,null);

        return retrievedSelectedDeviceNames != null || retrievedNotSelectedDeviceNames != null;
    }

    @Override
    public void addSharedPreferences(List<String> selectedDevices, List<String> notSelectedDevices) {
        mSharedPreferences.edit().putStringSet(SELECTED_DEVICES, new HashSet<>(selectedDevices)).apply();
        mSharedPreferences.edit().putStringSet(NOT_SELECTED_DEVICES,new HashSet<>(notSelectedDevices)).apply();
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

        List<String> selectedDevices = new ArrayList<>(retrievedSelectedDeviceNames);
        List<String> notSelectedDevices = new ArrayList<>(retrievedNotSelectedDeviceNames);

        this.selectedDevices = selectedDevices;
        this.notSelectedDevices = notSelectedDevices;

        displaySelectedDevices(selectedDevices,false);
        displayNotSelectedDevices(notSelectedDevices,false);
    }

    void getPairedNames(){
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device: devices){
            bTDeviceNames.add(device.getName());
        }
        dialog = new WelcomeDialog(this,bTDeviceNames);
        dialog.show();

    }

    void displaySelectedDevices(List<String> selectedNames,boolean editMode){
        mBTSelectedNamesRecyclerAdapter = new BTNamesRecyclerAdapter(this,selectedNames,editMode);
        selectedNamesRecyclerView = findViewById(R.id.bt_devices_names_rv);
        selectedNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedNamesRecyclerView.setAdapter(mBTSelectedNamesRecyclerAdapter);
    }

    void displayNotSelectedDevices(List<String> notSelectedNames, boolean editMode){
        mBTNamesNotSelectedRecyclerAdapter = new BTNamesRecyclerAdapter(this,notSelectedNames,editMode);
        notSelectedNamesRecyclerView = findViewById(R.id.not_selected_devices_rv);
        notSelectedNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notSelectedNamesRecyclerView.setAdapter(mBTNamesNotSelectedRecyclerAdapter);
    }
}
