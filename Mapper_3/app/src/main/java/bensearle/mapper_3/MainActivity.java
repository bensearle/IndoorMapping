package bensearle.mapper_3;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * All Ben Searle Code Below
     */

    public WifiManager Wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    public WifiInfo WifiConnection = Wifi.getConnectionInfo();
    public String ConnectedSSID = WifiConnection.getSSID(); // SSID of connected network

    public List<ScanResult> WAPs; // list of wireless access points

    public void button1_OnClick(View v) {
        // do something when the button is clicked
        Button button1 = (Button) v;
        ((Button) v).setText("clicked");


        boolean wifiEnabled = enableWifi();


        if(!wifiEnabled){
            // wifi is not enabled, do something
        } else {
            // wifi is enabled

        }


        ((Button) v).setText("sorted");
    }

    /**
     * enable the wifi, if not already enabled
     * @return true to signify that the wifi is enabled
     */
    private boolean enableWifi (){
        //Wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if(Wifi.isWifiEnabled()) {
            // wifi enabled
            return true;
        } else {
            Wifi.setWifiEnabled(true);
            Wifi.isWifiEnabled();
            return true;
        }
    }

    /**
     * get the SSID
     * @return the string of SSID
     */
    private String getSSID (){
        Log.d("wifiInfo", WifiConnection.toString());
        Log.d("SSID",WifiConnection.getSSID());

        return WifiConnection.getSSID();
    }


    /**
     * do a new scan for WAPs and collate them
     * @return true is it is successful
     */
    private boolean getWAP () {
        boolean scan = Wifi.startScan (); // scan for WAPs

        if(!scan){
            // scan failed, do something
            return false;
        } else {
            // scan success
            List<ScanResult> all_WAPs = Wifi.getScanResults(); // get list of WAPs
            List<ScanResult> filtered_WAPs = new ArrayList<ScanResult>(); // list of WAPs of SSID network

            for(Iterator<ScanResult> i = all_WAPs.iterator(); i.hasNext(); ) { // iterate list of WAPs
                ScanResult item = i.next();
                if (item.SSID == ConnectedSSID){ // if WAP has same SSID as connected network
                    filtered_WAPs.add(item); // add to list
                } else {
                    // do nothing
                }
            }

            WAPs = filtered_WAPs; // update public field
            return true;
        }
    }
}
