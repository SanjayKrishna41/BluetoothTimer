package com.sanjay.bluetoothtimer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    customBroadcastReceiver receiver;
    ConstraintLayout constraintLayout;
    CheckBox serviceEnable;
    Spinner timeSpinner;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);

        sharedpreferences = getSharedPreferences("save_config", Context.MODE_PRIVATE);

        initilizeUI();

        //get data from shared preferences
        boolean dataSaved = sharedpreferences.getBoolean("config_saved", false);
        String timerSaved = sharedpreferences.getString("time", "Select Timer");

        if (dataSaved) {
            serviceEnable.setChecked(true);
            serviceEnable.setText(R.string.enable);
            timeSpinner.setSelection(getIndex(timeSpinner, timerSaved));
        }

        //check if bluetooth is on or off
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth ON", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bluetooth OFF", Toast.LENGTH_SHORT).show();
        }
    }

    private void initilizeUI() {
        serviceEnable = findViewById(R.id.serviceCheckBox);
        timeSpinner = findViewById(R.id.timerSpinner);

        serviceEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serviceEnable.setText(R.string.enable);
                } else {
                    serviceEnable.setText(R.string.disable);
                }
            }
        });
    }

    public void saveConfig(View v) {

        if (serviceEnable.isChecked()) {
            if (timeSpinner.getSelectedItem().toString().equals("Select Timer")) {
                Toast.makeText(this, "please select proper time", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Time selected " + timeSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //create an object for the receiver
                receiver = new customBroadcastReceiver(constraintLayout, getApplicationContext(), timeSpinner.getSelectedItem().toString());
                //register service
                registerBluetoothService();
                //save timer data to shared preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("config_saved", true);
                editor.putString("time", timeSpinner.getSelectedItem().toString());
                editor.apply();
            }
        } else {
            //bluetooth serive timer disabled
            Toast.makeText(this, "Service disabled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Function to register service
     */
    public void registerBluetoothService() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerBluetoothService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        registerBluetoothService();
    }

    //function to get index of a spinner
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

}