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
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;

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

    public WifiManager Wifi;// = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    public WifiInfo WifiConnection;// = Wifi.getConnectionInfo();
    public String ConnectedSSID;// = WifiConnection.getSSID(); // SSID of connected network

    public List<ScanResult> WAPs; // list of wireless access points

    public static int EUCLIDEAN_DISTANCE_POINTS = 5; // how many points/WAPs to compare when calculating Euclidean distance

    public int testX;
    public int testY;
    public int testZ;

    public void AddPoint(View v){
        Log.d("AddPoint","In  ("+testX+","+testY+","+testZ+")");

        // get the input fields
        EditText viewX = (EditText) findViewById(R.id.InputX);
        EditText viewY = (EditText) findViewById(R.id.InputY);
        EditText viewZ = (EditText) findViewById(R.id.InputZ);

        // Check that X,Y,Z have been inputted /not null
        if(viewX.length()>0 && viewY.length()>0 && viewZ.length()>0 ){
            // get the position values from the input text fields
            Integer inputX = Integer.parseInt(viewX.getText().toString());
            Integer inputY = Integer.parseInt(viewY.getText().toString());
            Integer inputZ = Integer.parseInt(viewZ.getText().toString());

            // create fingerprint with WAPs and position
            Fingerprint fp = new Fingerprint(getWAPs());
            fp.SetPostion(inputX,inputY,inputZ);

            // add fingerprint to database
            // TO DO

            // show acknowledgement to use that point has been added successfully
            // TO DO

            testX = inputX;
            testY = inputY;
            testZ = inputZ;
            Log.d("AddPoint","Out ("+testX+","+testY+","+testZ+")");
        } else {
            // input of point not complete, must contain X,Y,Z
        }
    }

    public void GetPoint(View v){

        Log.d("GetPoint","In  ("+testX+","+testY+","+testZ+")");

        // create current fingerprint with WAPs
        Fingerprint fp = new Fingerprint(getWAPs());

        // get similar fingerprints of reference points (RP) from database
        // RP fingerprints must have at least n WAPs the same as current fingerprint, n=EUCLIDEAN_DISTANCE_POINTS
        // TO DECIDE: use top n WAPs of current fingerprint or any n fingerprints?
        // TO DO

        // get Euclidean distance between each RP fingerprint and current fingerprint
        // TO DO

        // localization algorithm: decreasing triangles
        // use 3 closest RP
        // TO DO

        // localization algorithm: another one
        // TO DO

        // output estimated position
        TextView outputX = (TextView)findViewById(R.id.OutputX);
        TextView outputY = (TextView)findViewById(R.id.OutputY);
        TextView outputZ = (TextView)findViewById(R.id.OutputZ);

        outputX.setText(""+testX);
        outputY.setText(""+testY);
        outputZ.setText(""+testZ);

        Log.d("GetPoint", "Out  (" + testX + "," + testY + "," + testZ + ")");

        //((Button) v).setText("executed");
    }

    /*
     * Network Connectivity
     */

    /**
     * Refresh the network to initialize/update network fields and get SSID
     * @param v from button click
     */
    public void RefreshNetwork(View v){
        Log.d("RefreshNetwork", "Start Refreshing Network");
        Wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (enableWifi()){
            WifiConnection = Wifi.getConnectionInfo();
            ConnectedSSID = WifiConnection.getSSID(); // SSID of connected network
            Log.d("RefreshNetwork", "Wifi: "+WifiConnection.toString());

            TextView outputSSID = (TextView)findViewById(R.id.OutputSSID);
            outputSSID.setText(""+outputSSID);
            Log.d("RefreshNetwork", "Network Refreshed");


        } else {
            // no wifi network
            Log.d("RefreshNetwork", "No Network");

        }
    }

    /**
     * enable the wifi, if not already enabled
     * @return true to signify that the wifi is enabled
     */
    private boolean enableWifi (){
        if(Wifi.isWifiEnabled()) {
            // wifi enabled
            return true;
        } else {
            Wifi.setWifiEnabled(true);
            if(Wifi.isWifiEnabled()){
                return true;
            }
            // wifi still not enabled
            return false;
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
     * do a new scan for WAPs and collate them, then filter to only contain WAP with same SSID
     * @return true is it is successful
     */
    private List<ScanResult> getWAPs () {
        boolean scan = Wifi.startScan(); // scan for WAPs

        if(!scan){
            // scan failed, do something
            return new ArrayList<ScanResult>();
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
            return filtered_WAPs;
        }
    }
}
