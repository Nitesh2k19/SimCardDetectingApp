package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<SubscriptionInfo> subInfoList;
    private String LOCATION;
    private SharedPreferences sp = null;
    String firstRun;
    TextView text;
    private int successCode = 1088;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        LOCATION = "SimApplication";
         sp = getSharedPreferences(LOCATION, MODE_PRIVATE);
        Log.v("IMPORTANT",LoadString("Sim"+0));

        firstRun = LoadString("firstRun");
        if (firstRun.equals("Default")) {
            SaveString("firstRun", "Done");
            checkPermission(Manifest.permission.READ_PHONE_STATE, successCode);
        } else {
            SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            try {
                subInfoList = subManager.getActiveSubscriptionInfoList();
            } catch (SecurityException e) {
                Toast.makeText(MainActivity.this, "Permission is required", Toast.LENGTH_LONG).show();
            }
            if (subInfoList != null) {
                for (int i = 0; i < subInfoList.size(); i++) {
                    String subID = subInfoList.get(i).getIccId();
                    String id = LoadString("Sim" + i);
                    Log.v("TTTTTTT", id + " " + subID+" "+i);
                    if (!(subID.equals(id))) {
                        text.setText("Sim Card changed at Slot " + (i + 1) + "\n");
                        SaveString("Sim"+i,subID);
                    } else
                        text.setText("No Sim Change Detected");
                }

            }
            else
                text.setText("No Sim Card Found");
        }
        }


    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[]{permission},
                            requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == successCode) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

               try {
                   subInfoList = subManager.getActiveSubscriptionInfoList();
               }
               catch(SecurityException e){
                   Toast.makeText(MainActivity.this,"Permission is required",Toast.LENGTH_LONG).show();
               }
                if (subInfoList != null) {

                    for (int i = 0; i < subInfoList.size(); i++) {
                        String subID = subInfoList.get(i).getIccId();
                        SaveString("Sim" + i, subID);
                    }
                    text.setText("First Run\nSim Details Saved");
                }
                else
                    text.setText("No Sim Card Found");
                Log.v("IMPORTANT",LoadString("Sim"+0));
                // Showing the toast message
                /*Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();*/
            }
            else
            Toast.makeText(MainActivity.this,
                        "Permission is Required",
                        Toast.LENGTH_LONG)
                        .show();
        }
    }
    public void SaveString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String LoadString(String key) {
       // SharedPreferences sp = getSharedPreferences(LOCATION, MODE_PRIVATE);
        String myStringValue = sp.getString(key, "Default");
        return myStringValue;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (firstRun.equals("Default")) {
            text.setText("First Run\nSim Details Saved");
        } else {
            SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            try {
                subInfoList = subManager.getActiveSubscriptionInfoList();
            } catch (SecurityException e) {
                Toast.makeText(MainActivity.this, "Permission is required", Toast.LENGTH_LONG).show();
            }
            if (subInfoList != null) {
                for (int i = 0; i < subInfoList.size(); i++) {
                    String subID = subInfoList.get(i).getIccId();
                    String id = LoadString("Sim" + i);
                    Log.v("TTTTTTT", id + " " + subID+" "+i);
                    if (!(subID.equals(id))) {
                        text.setText("Sim Card changed at Slot " + (i + 1) + "\n");
                       SaveString("Sim"+i,subID);
                    }
                }

            }
            else
                text.setText("No Sim Card Found");
        }
    }
}
