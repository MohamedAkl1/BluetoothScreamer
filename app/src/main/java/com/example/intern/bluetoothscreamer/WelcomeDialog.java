package com.example.intern.bluetoothscreamer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.intern.bluetoothscreamer.adapter.WelcomeRecyclerAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.intern.bluetoothscreamer.adapter.WelcomeRecyclerAdapter.getChosenNames;
import static com.example.intern.bluetoothscreamer.adapter.WelcomeRecyclerAdapter.getUnchoosenNames;

/**
 * Created by Mohamed Akl on 8/22/2018.
 */
public class WelcomeDialog extends Dialog {

    private RecyclerView devicesRV;
    private WelcomeRecyclerAdapter mWelcomeRecyclerAdapter;
    private Set<String> bluetoothPairedNames;
    Button submitButton;
    private static OnDialogSubmit mOnDialogSubmit;
    private MainView mMainView;

    public WelcomeDialog(@NonNull Context context, Set<String> bTnames) {
        super(context);
        bluetoothPairedNames = new HashSet<>();
        this.bluetoothPairedNames = bTnames;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_welcome);

        devicesRV = findViewById(R.id.welcome_bt_devices_rv);
        mWelcomeRecyclerAdapter = new WelcomeRecyclerAdapter(getContext(),bluetoothPairedNames);
        devicesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        devicesRV.setAdapter(mWelcomeRecyclerAdapter);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> selectedDevices = getChosenNames();
                List<String> notSelectedDevices = getUnchoosenNames();
                mOnDialogSubmit.onDialogSubmit(selectedDevices,notSelectedDevices);
                mMainView.addSharedPreferences(selectedDevices,notSelectedDevices);
            }
        });
    }

    public interface OnDialogSubmit{
        void onDialogSubmit(List<String> selectedNames, List<String> notSelectedNames);
    }

    public void setDialogResult(OnDialogSubmit dialogResult){
        mOnDialogSubmit = dialogResult;
    }

    public void setMainViewListener(MainView mainViewListener){
        this.mMainView = mainViewListener;
    }
}
