package bensearle.mapper_3;

import android.content.Context;
import android.database.Cursor;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bensearle.mapper_3.Database.FPDataHelper;
import bensearle.mapper_3.Structures.Circle3D;
import bensearle.mapper_3.Structures.CircleCluster;
import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;
import bensearle.mapper_3.Algorithms;
import bensearle.mapper_3.Structures.Triangle3D;
import bensearle.mapper_3.Structures.Square3D;

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



    FPDataHelper database; // = new FPDataHelper(getApplicationContext());

    public int testX;
    public int testY;
    public int testZ;

    public void test(){

        // initialize the database and use this to access / add, get, etc
        FPDataHelper database = new FPDataHelper(getApplicationContext());
        // FPDataHelper database = new FPDataHelper(this);
        // FPDataHelper database = new FPDataHelper(geContext());
        // FPDataHelper database = new FPDataHelper(getContext());





    }

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
            fp.SetPostion(inputX, inputY, inputZ);

            // add fingerprint to database
            database.AddFP(fp);

            // show acknowledgement to use that point has been added successfully
            // TO DO

            // clear text boxes
            ((EditText) findViewById(R.id.InputX)).setText("");
            ((EditText) findViewById(R.id.InputY)).setText("");
            ((EditText) findViewById(R.id.InputZ)).setText("");

            testX = inputX;
            testY = inputY;
            testZ = inputZ;
            Log.d("AddPoint","Out ("+testX+","+testY+","+testZ+")");
        } else {
            // input of point not complete, must contain X,Y,Z
        }
    }

    public void GetPoint(View v){

        // Log.d("GetPoint","In  ("+testX+","+testY+","+testZ+")");

        // create current fingerprint with WAPs
        Fingerprint currentFP = new Fingerprint(getWAPs());

        // get similar fingerprints of reference points (RP) from database
        // RP fingerprints must have at least n WAPs the same as current fingerprint, n=EUCLIDEAN_DISTANCE_POINTS
        // use top n strongest WAPs of current fingerprint
        String[] strongestWAPs = currentFP.GetStrongestNWaps(UserVariables.EUCLIDEAN_DISTANCE_POINTS);

        ArrayList<String> rpFingerprintTags = new ArrayList<>(); // RP fingerprints that use n WAPs same as current

        for (int i = 0; i < UserVariables.EUCLIDEAN_DISTANCE_POINTS; i++){ // iterate the top n strongest WAPs
            if (i == 0){
                rpFingerprintTags = database.GetFingerprintByWAP(strongestWAPs[i]);
            } else {
                ArrayList<String> rpFingerprintTags_next = database.GetFingerprintByWAP(strongestWAPs[i]); // fp tags for next WAP in list
                ArrayList<String> both = new ArrayList<>(); // list of WAPs in both rpFingerprintTags lists

                for (String tag: rpFingerprintTags){ // iterate list one
                    if (rpFingerprintTags_next.contains(tag)){ // if tag is also in list 2
                        both.add(tag); // add to both list
                    }
                }
                rpFingerprintTags = both; // update master list
            }
        }

        // get Euclidean distance between each RP fingerprint and current fingerprint
        // store RP fingerprints and distances to current fingerprint, TreeMap sorted by key
        Map<Float,Fingerprint> fingerprintAndDistance = new TreeMap<Float,Fingerprint>(Collections.reverseOrder());

        for (String fptag: rpFingerprintTags){ // iterate list one
            int[][] rssiCurrentandRP = new int[UserVariables.EUCLIDEAN_DISTANCE_POINTS][2]; // row for each ED point, columns for 2 fingerprints
            Fingerprint rpFingerprint = new Fingerprint(database.GetFingerprintByTag(fptag)); // get RP fingerprint from DB

            int count = 0;
            for (String wap: strongestWAPs){
                rssiCurrentandRP[count][0] = currentFP.GetRSSI(wap);
                rssiCurrentandRP[count][1] = rpFingerprint.GetRSSI(wap);
                count++;
            }

            float distance = (float) Algorithms.Euclidean_Distance(rssiCurrentandRP); // get Euclidean distance

            // make sure distance is not already in map, as no duplicate keys. precision is lost
            while(fingerprintAndDistance.containsKey(distance)){
                distance += Float.MIN_VALUE;
            }

            fingerprintAndDistance.put(distance, rpFingerprint); // store fingerprint and distance
        }





        List<Float> sortedDistances = new ArrayList(fingerprintAndDistance.keySet());
        Collections.sort(sortedDistances);

        /*
         * localization algorithm: decreasing triangles
         */

        // using closest 3 RPs
        List<Float> closest3 = sortedDistances.subList(0, 3); // get the first 3 RP distances
        Triangle3D triangleClosest3 = new Triangle3D();
        for (Float distance: closest3){
            Fingerprint fp = fingerprintAndDistance.get(distance);
            triangleClosest3.AddRP(fp.GetPosition(), distance); // add this position and distance to the triangle
        }
        while(triangleClosest3.DecreaseSize()); // while the triangle can decrease size, keep decreasing size
        Point3D estimatedPoint_DTClosest = triangleClosest3.GetCentroid();
        Log.d("TD","TD_Closest3: " + estimatedPoint_DTClosest.toString());

        // using furthest 3 RPs
        List<Float> furthest3 = sortedDistances.subList(Math.max(sortedDistances.size() - 3, 0), sortedDistances.size()); // get the last 3 RP distances
        Triangle3D triangleFurthest3 = new Triangle3D();
        for (Float distance: furthest3){
            Fingerprint fp = fingerprintAndDistance.get(distance);
            triangleFurthest3.AddRP(fp.GetPosition(), distance); // add this position and distance to the triangle
        }
        while(triangleFurthest3.DecreaseSize()); // while the triangle can decrease size, keep decreasing size
        Point3D estimatedPoint_DTFurthest = triangleFurthest3.GetCentroid();
        Log.d("TD","TD_Furthest3: " + estimatedPoint_DTFurthest.toString());

        // output estimate to gui
        double estX_DT = Math.round (estimatedPoint_DTClosest.X * 100.0) / 100.0;
        double estY_DT = Math.round (estimatedPoint_DTClosest.Y * 100.0) / 100.0;
        double estZ_DT = Math.round (estimatedPoint_DTClosest.Z * 100.0) / 100.0;
        ((TextView) findViewById(R.id.OutputX_DT)).setText("" + estX_DT);
        ((TextView) findViewById(R.id.OutputY_DT)).setText("" + estY_DT);
        ((TextView) findViewById(R.id.OutputZ_DT)).setText("" + estZ_DT);

        /*
        Triangle3D triangle = new Triangle3D();
        int count = 0;
        for(Iterator i = fingerprintAndDistance.entrySet().iterator(); i.hasNext();) { // iterate list of WAPs
            if (count<3) {
                Map.Entry item = (Map.Entry) i.next();
                Fingerprint fp = (Fingerprint) item.getValue(); // get the fingerprint fromt the map
                Float distance = (Float) item.getKey();
                triangle.AddRP(fp.GetPosition(), distance); // add this position and distance to the triangle
            } else { break; }
        }
        while(triangle.DecreaseSize()); // while the triangle can decrease size, keep decreasing size
        Point3D estimatedPoint_DT = triangle.GetCentroid();


        // output estimated position
        double estX_DT = Math.round (estimatedPoint_DT.X * 100.0) / 100.0;
        double estY_DT = Math.round (estimatedPoint_DT.Y * 100.0) / 100.0;
        double estZ_DT = Math.round (estimatedPoint_DT.Z * 100.0) / 100.0;

        ((TextView) findViewById(R.id.OutputX_DT)).setText(""+estX_DT);
        ((TextView) findViewById(R.id.OutputY_DT)).setText(""+estY_DT);
        ((TextView) findViewById(R.id.OutputZ_DT)).setText(""+estZ_DT);
        */

        /*
         * localization algorithm: overlapping circles
         */
        CircleCluster circles = new CircleCluster(sortedDistances, fingerprintAndDistance);
        Point3D estimatedPoint_OC = circles.Localise();

        // output estimate to gui
        double estX_OC = Math.round (estimatedPoint_OC.X * 100.0) / 100.0;
        double estY_OC = Math.round (estimatedPoint_OC.Y * 100.0) / 100.0;
        double estZ_OC = Math.round (estimatedPoint_OC.Z * 100.0) / 100.0;
        ((TextView) findViewById(R.id.OutputX_OC)).setText("" + estX_OC);
        ((TextView) findViewById(R.id.OutputY_OC)).setText("" + estY_OC);
        ((TextView) findViewById(R.id.OutputZ_OC)).setText("" + estZ_OC);

        /*
        TextView outputX = (TextView)findViewById(R.id.OutputX);
        TextView outputY = (TextView)findViewById(R.id.OutputY);
        TextView outputZ = (TextView)findViewById(R.id.OutputZ);

        outputX.setText(""+testX);
        outputY.setText(""+testY);
        outputZ.setText(""+testZ);
*/
        /*
         * localization algorithm: weighted coordinates
         */
        Square3D square = new Square3D(sortedDistances, fingerprintAndDistance);



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
            Log.d("RefreshNetwork", "Wifi: " + WifiConnection.toString());

            String ssid = WifiConnection.getSSID(); // SSID of connected network
            ConnectedSSID = ssid.replaceAll("^\"|\"$", "");; // remove "quote marks"


            TextView outputSSID = (TextView)findViewById(R.id.OutputSSID);
            outputSSID.setText(""+ConnectedSSID);

            Log.d("RefreshNetwork", "Network Refreshed");
        } else {
            // no wifi network
            Log.d("RefreshNetwork", "No Network");
        }

        Log.d("RefreshNetwork", "Initialise database");
        // connect to database
        database = new FPDataHelper(getApplicationContext());
        Cursor databasedata = database.GetAll();

        Log.d("RefreshNetwork", "Database initialised");
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
                //if (item.SSID.equals(ConnectedSSID)){ // if WAP has same SSID as connected network
                if (UserVariables.UNIVERSITY_NETWORKS.contains(item.SSID)){ // if WAP is one one of the allowed network
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
