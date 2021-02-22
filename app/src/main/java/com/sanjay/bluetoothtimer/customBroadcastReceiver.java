package com.sanjay.bluetoothtimer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class customBroadcastReceiver extends BroadcastReceiver {
    ConstraintLayout constraintLayout;
    Context context;
    long bluetoothOffTime = 900000; //15 minutes
    long testingtime = 5000; //5 seconds (Testing purpose)
    CountDownTimer countDownTimer;
    String timer;

    public customBroadcastReceiver(ConstraintLayout constraintLayout, Context context,String timer) {
        this.constraintLayout = constraintLayout;
        this.context = context;
        this.timer = timer;
    }

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        try
        {
            String action = intent.getAction();

            if(timer.startsWith("30 S")){
                bluetoothOffTime = 30 * 1000;
            }
            else if(timer.startsWith("1 M")){
                bluetoothOffTime = 60 * 1000;
            }
            else if(timer.startsWith("2 M")){
                bluetoothOffTime = 120 * 1000;
            }
            else if(timer.startsWith("3 M")){
                bluetoothOffTime = 180 * 1000;
            }
            else if(timer.startsWith("4 M")){
                bluetoothOffTime = 240 * 1000;
            }
            else if(timer.startsWith("5 M")){
                bluetoothOffTime = 300 * 1000;
            }
            else if(timer.startsWith("10 M")){
                bluetoothOffTime = 600 * 1000;
            }
            else if(timer.startsWith("15 M")){
                bluetoothOffTime = 900 * 1000;
            }
            else if(timer.startsWith("20 M")){
                bluetoothOffTime = 1200 * 1000;
            }
            else if(timer.startsWith("25 M")){
                bluetoothOffTime = 1500 * 1000;
            }
            else if(timer.startsWith("30 M")){
                bluetoothOffTime = 1800 * 1000;
            }
            else if(timer.startsWith("1 H")){
                bluetoothOffTime = 3600 * 1000;
            }

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected, end timer
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                } else {
                    Toast.makeText(context, "Bluetooth is connected to a device", Toast.LENGTH_SHORT).show();
                }

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Toast.makeText(context, "Device is offline", Toast.LENGTH_SHORT).show();
                //Device has disconnected, start timer
                countDownTimer = new CountDownTimer(bluetoothOffTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        //switch off bluetooth
                        //Disable bluetooth
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            mBluetoothAdapter.disable();
                            Toast.makeText(context, "Bluetooth disconnected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "bluetooth disconnected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
