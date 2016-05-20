package bensearle.mapper_3.Structures;

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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by bensearle on 19/05/2016.
 */
public class Fingerprint {

    public String MapReference; // description of location
    private Point3D location; // location in 3D space


    //public List<Pair<String, Integer>> WAPS = new ArrayList<Pair<String, Integer>>(); // BSSID & RSSI of a WAP

    public Map<String, Integer> WAPS = new LinkedHashMap<String, Integer>(); // BSSID & RSSI of a WAP


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
            WAPS.put(BSSID,RSSI);
        }
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
            WAPS.put(BSSID, RSSI);
        }
    }

    //nothing useful
    public void IterateMap (){
        for(Iterator i = WAPS.entrySet().iterator(); i.hasNext(); ) { // iterate list of WAPs
            Map.Entry item = (Map.Entry) i.next();
            String BSSID = (String) item.getKey();
            int RSSI = (int) item.getValue();
        }

        for (Map.Entry<String, Integer> entry : WAPS.entrySet())
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

    // get
    public Point3D GetPosition() { return location; }
    public Integer GetRSSI(String BSSID) { return WAPS.get(BSSID); };

    /**
     * get the strongest n WAPs for this fingerprint
     * @return array of BSSIDs
     */
    public String[] GetStrongestNWaps(){

        int n = 5; // top n WAPS
        int[] topRSSI = new int[n]; // list of top RSSI
        String[] topBSSID = new String[n]; // list of top BSSID

        Map<String, Integer> sortedWaps = sortMap(WAPS); // sort the map

        // get first n
        int count = 0;
        for(Iterator i = WAPS.entrySet().iterator(); i.hasNext();) { // iterate list of WAPs
            if (count<n) {
                Map.Entry item = (Map.Entry) i.next();
                topBSSID[count] = (String) item.getKey();
            } else { break; }
        }
        return topBSSID;
    }


    /**
     * Does this fingerprint use these WAPS
     * @param keys BSSID of WAPS to check
     * @return true if contains all
     */
    public boolean UsesWAPS (String[] keys){
        for (String key: keys){ // check all keys
            if (!WAPS.containsKey(key)) return false; // false if any key is not present
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
    public boolean UsesWAPS (String k1, String k2, String k3, String k4, String k5){
        return WAPS.containsKey(k1) &&
                WAPS.containsKey(k2) &&
                WAPS.containsKey(k3) &&
                WAPS.containsKey(k4) &&
                WAPS.containsKey(k5);
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