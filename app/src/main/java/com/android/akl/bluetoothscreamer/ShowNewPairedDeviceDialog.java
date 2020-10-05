package com.android.akl.bluetoothscreamer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Mohamed Akl on 9/9/2018.
 */
public class ShowNewPairedDeviceDialog extends Activity{

    SharedPreferences sharedPreferences;
    String device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = getIntent().getStringExtra("DEVICE_NAME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDialog();
    }

    private void showDialog() {
        new MaterialDialog.Builder(getApplicationContext())
                .title("Device " + device + "discovered, would you like to add it to alerted devices?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .build()
                .show();
    }
}
