package bensearle.mapper_3.Structures;

import android.database.Cursor;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by bensearle on 19/05/2016.
 */
public class Fingerprint {

    public String MapReference; // description of location
    private Point3D location = new Point3D(); // location in 3D space


    //public List<Pair<String, Integer>> WAPS = new ArrayList<Pair<String, Integer>>(); // BSSID & RSSI of WAPs

    public Map<String, Integer> WAPs = new LinkedHashMap<String, Integer>(); // BSSID & RSSI of a WAP


    Pair<Integer, String> simplePair = new Pair<>(42, "Second");

    /**
     * Initializer for fingerprint, where location is unknown
     * @param waps wireless access points (BSSID & RSSI)
     */
    public Fingerprint(List<ScanResult> waps) {
        // get the name and rssi of the waps and add to fingerprint
        for(Iterator<ScanResult> i = waps.iterator(); i.hasNext(); ) { // iterate list of WAPs
            ScanResult item = i.next();
            String BSSID = item.BSSID;
            Integer RSSI = item.level;
            //WAPS.add(new Pair<>(BSSID, RSSI));
            WAPs.put(BSSID,RSSI);
        }
    }

    public Fingerprint(Cursor dbResults) {
        // TODO initialize Fingerprint from DB results
    }

    /**
     * Initializer for fingerprint, where location is known
     * @param coord coordinates of the location
     * @param waps wireless access points (BSSID & RSSI)
     */
    public Fingerprint(Point3D coord, List<ScanResult> waps) {
        location = coord; // set coordinates for location
        // get the name and rssi of the waps and add to fingerprint
        for(Iterator<ScanResult> i = waps.iterator(); i.hasNext(); ) { // iterate list of WAPs
            ScanResult item = i.next();
            String BSSID = item.BSSID;
            Integer RSSI = item.level;
            WAPs.put(BSSID, RSSI);
        }
    }

    //nothing useful
    public void IterateMap (){
        for(Iterator i = WAPs.entrySet().iterator(); i.hasNext(); ) { // iterate list of WAPs
            Map.Entry item = (Map.Entry) i.next();
            String BSSID = (String) item.getKey();
            int RSSI = (int) item.getValue();
        }

        for (Map.Entry<String, Integer> entry : WAPs.entrySet())
        {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }



    // set
    public void SetPostion(double x, double y, double z){
        location.X = x;
        location.Y = y;
        location.Z = z;
    }

    public void SetPostion(Point3D p){
        location.X = p.X;
        location.Y = p.Y;
        location.Z = p.Z;
    }

    // get
    public Point3D GetPosition() { return location; }
    public Map<String, Integer> GetWAPs(){
        return WAPs;
    }
    public Integer GetRSSI(String BSSID) { return WAPs.get(BSSID); } // return wap RSSI, given BSSID


    /**
     * get a tag for this fingerprint
     * @return string in form XXXYYYZZZ
     */
    public String GetTag(){
        String x = threeDigit(location.X);
        String y = threeDigit(location.Y);
        String z = threeDigit(location.Z);
        String xyz = x+y+z;
        return "" + threeDigit(location.X) + threeDigit(location.Y) + threeDigit(location.Z);
    }

    /**
     * create string of length 3 for int. 1 --> 001
     * @param n is the number to be converted
     * @return 3 digit string of number
     */
    private String threeDigit(double n){
        int number = (int) n;
        String s = ""+number;
        int length = s.length();

        if (length<1){
            return "000";
        } else if (length<2){
            return "00" + number;
        } else if (length<3){
            return "0" + number;
        } else if (length<4){
            return ""+ number;
        } else {
            // number is longer than 3 digits
            return "***";
        }
    }

    /**
     * get the strongest n WAPs for this fingerprint
     * @return array of BSSIDs
     */
    public String[] GetStrongestNWaps(int n){

        //int n = 5; // top n WAPS
        int[] topRSSI = new int[n]; // list of top RSSI
        String[] topBSSID = new String[n]; // list of top BSSID

        Map<String, Integer> sortedWaps = sortMap(WAPs); // sort the map

        // get first n
        int count = 0;
        for(Iterator i = WAPs.entrySet().iterator(); i.hasNext();) { // iterate list of WAPs
            if (count<n) {
                Map.Entry item = (Map.Entry) i.next();
                topBSSID[count] = (String) item.getKey();
                count++;
            } else { break; }
        }
        return topBSSID;
    }


    public Object[][] GetWapArray(){
        int n = 15; // top n WAPS
        Object[][] waps = new Object[n][2]; // 2 columns (bssid & rssi)

        int[] topRSSI = new int[n]; // list of top RSSI
        String[] topBSSID = new String[n]; // list of top BSSID

        Map<String, Integer> sortedWaps = sortMap(WAPs); // sort the map

        // get first n
        int count = 0;
        for(Iterator i = WAPs.entrySet().iterator(); i.hasNext();) { // iterate list of WAPs
            if (count<n) {
                Map.Entry item = (Map.Entry) i.next();
                String bssid = (String) item.getKey();
                Integer rssi = (Integer) item.getValue();
                waps[count][0] = bssid;
                waps[count][1] = rssi;
            } else { break; }
        }
        return waps;
    }

    /**
     * Does this fingerprint use these WAPS
     * @param keys BSSID of WAPS to check
     * @return true if contains all
     */
    public boolean UsesWAPs (String[] keys){
        for (String key: keys){ // check all keys
            if (!WAPs.containsKey(key)) return false; // false if any key is not present
        }
        return true; // true if fingerprint contains all keys
    }

    /**
     * Does this fingerprint use these 5 WAPS
     * @param k1 // BSSID of WAP
     * @param k2
     * @param k3
     * @param k4
     * @param k5
     * @return true if contains all
     */
    public boolean UsesWAPs (String k1, String k2, String k3, String k4, String k5){
        return WAPs.containsKey(k1) &&
                WAPs.containsKey(k2) &&
                WAPs.containsKey(k3) &&
                WAPs.containsKey(k4) &&
                WAPs.containsKey(k5);
    }

    //http://stackoverflow.com/questions/1448369/how-to-sort-a-treemap-based-on-its-values
    private static Map<String, Integer> sortMap(Map<String, Integer> unsortedMap)
    {
        final boolean ascending = false; //

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (ascending)
                { return o1.getValue().compareTo(o2.getValue()); }
                else
                { return o2.getValue().compareTo(o1.getValue()); }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}