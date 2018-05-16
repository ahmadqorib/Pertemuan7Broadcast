package com.gmail.ahmadqorib97.pertemuan7broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView txtBatterayL, statusCharg, statusHeal, statusJar;
    private ProgressBar pbBatterayL;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBatterayL = (TextView) findViewById(R.id.txtBL);
        statusCharg = (TextView) findViewById(R.id.statusC);
        statusHeal = (TextView) findViewById(R.id.statusH);
        statusJar = (TextView) findViewById(R.id.statusJ);
        pbBatterayL = (ProgressBar) findViewById(R.id.pbBW);
        mReceiver = new BatteryBroadcastReceiver();
    }

    public void playAudio(View view){
        Intent objIntent = new Intent(this, PlayAudio.class);
        startService(objIntent);
    }

    public void stopAudio(View view){
//        Intent objIntent = new Intent(this, StopAudio.class);
//        stopService(objIntent);
    }

    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            txtBatterayL.setText("Batteray Level Anda : " + level);
            pbBatterayL.setProgress(level);

            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batt = context.registerReceiver(null, filter);
            int status = batt.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            int chargePlug = batt.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            if (status == 2) {
                if (chargePlug == 2)
                    statusCharg.setText("Battery Charging dengan USB");
                else
                    statusCharg.setText("Battery Charging with AC Charging");
            } else {
                statusCharg.setText("Battery Not Charging");
            }

            if (level >= 80) {
                statusHeal.setText("Kuat");
            } else if (level >= 30) {
                statusHeal.setText("Sedang");
            } else if (level < 30) {
                statusHeal.setText("Lemah");
            }

            ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connect.getActiveNetworkInfo();
            boolean isWifi= info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
            boolean isData=info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE;
            if(isWifi==true){
                statusJar.setText("Connected to Internet Using WiFi");
            }else if(isData==true){
                statusJar.setText("Connected to Internet ("+ StatusConnection(info.getSubtype()) + ")");
            }
            else {
                statusJar.setText("Not Connected to Internet");
            }
        }
    }

    public static String StatusConnection(int subType) {
        switch (subType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_O";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return "UNKNOWN";
        }
    }
}
