package bensearle.mapper_3;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bensearle.mapper_3.Database.DataHelper;
import bensearle.mapper_3.Structures.CircleCluster;
import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;
import bensearle.mapper_3.Structures.Square3D;
import bensearle.mapper_3.Structures.Triangle3D;

/**
 * Created by bensearle on 21/06/2016.
 */
public class Testing {

    public void RunEDTests(DataHelper database){
        //TODO get test data and then execute
        Fingerprint fp = new Fingerprint();
        // get all fingeprints from db to test

        // test all test data
        testEuclideanDistance(fp, "testdata", database);

        // test all map data
        testEuclideanDistance(fp, "RPdata", database);

    }

    public void RunLocalisationTests(){
        //TODO get test data and then execute and add to db

    }

    private void testEuclideanDistance(Fingerprint currentFP, String inputType, DataHelper database){

        // get waps from fingerprint 1
        String[] strongestWAPs = currentFP.GetStrongestNWaps(UserVariables.EUCLIDEAN_DISTANCE_POINTS);

        /*
         * get RPs that can be used to estimate location
         */
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

        /*
         * calculate Euclidean distances between current position and each RPs
         */

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

            Point3D testLocation = currentFP.GetPosition();
            Point3D rpLocation = rpFingerprint.GetPosition();
            double map_distance = Algorithms.Distance(testLocation, rpLocation);
            //double geometric_distance = map_distance * 8; // maybe 8m is 1 grid width
            double euclidean_distance = Algorithms.Euclidean_Distance(rssiCurrentandRP); // get Euclidean distance
            Log.d("Euclidean Test,",
                            "Loc1"+","+testLocation+","+
                            "Loc2"+","+rpLocation+","+
                            "MapD"+","+map_distance+","+
                            "EucD"+","+euclidean_distance+",");

            // TODO log test results/ add to db
        }

    }

    private void testLocalisation(Fingerprint currentFP, DataHelper database){
        // get similar fingerprints of reference points (RP) from database
        // RP fingerprints must have at least n WAPs the same as current fingerprint, n=EUCLIDEAN_DISTANCE_POINTS
        // use top n strongest WAPs of current fingerprint
        String[] strongestWAPs = currentFP.GetStrongestNWaps(UserVariables.EUCLIDEAN_DISTANCE_POINTS);

        /*
         * get RPs that can be used to estimate location
         */
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

        /*
         * calculate Euclidean distances between current position and each RPs
         */

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
        List<Float> closest3 = sortedDistances.subList(0, Math.min(3, sortedDistances.size())); // get the first 3 RP distances
        Triangle3D triangleClosest3 = new Triangle3D();
        for (Float distance: closest3){
            Fingerprint fp = fingerprintAndDistance.get(distance);
            triangleClosest3.AddRP(fp.GetPosition(), distance); // add this position and distance to the triangle
        }
        while(triangleClosest3.DecreaseSize()); // while the triangle can decrease size, keep decreasing size
        Point3D estimatedPoint_DTClosest = triangleClosest3.GetCentroid();
        Log.d("TD", "TD_Closest3: " + estimatedPoint_DTClosest.toString());

        // using furthest 3 RPs
        List<Float> furthest3 = sortedDistances.subList(Math.max(sortedDistances.size() - 3, 0), sortedDistances.size()); // get the last 3 RP distances
        Triangle3D triangleFurthest3 = new Triangle3D();
        for (Float distance: furthest3){
            Fingerprint fp = fingerprintAndDistance.get(distance);
            triangleFurthest3.AddRP(fp.GetPosition(), distance); // add this position and distance to the triangle
        }
        while(triangleFurthest3.DecreaseSize()); // while the triangle can decrease size, keep decreasing size
        Point3D estimatedPoint_DTFurthest = triangleFurthest3.GetCentroid();
        Log.d("TD", "TD_Furthest3: " + estimatedPoint_DTFurthest.toString());


        /*
         * localization algorithm: overlapping circles
         */
        CircleCluster circlesAll = new CircleCluster(sortedDistances, fingerprintAndDistance);
        Point3D estimatedPoint_OCAll = circlesAll.Localise();

        List<Float> top4 = sortedDistances.subList(0, Math.min(4, sortedDistances.size())); // get the first 3 RP distances
        CircleCluster circlesTop4 = new CircleCluster(top4, fingerprintAndDistance);
        Point3D estimatedPoint_OCTop4 = circlesTop4.Localise();


        /*
         * localization algorithm: weighted coordinates
         */
        Square3D square = new Square3D(sortedDistances, fingerprintAndDistance);
        Point3D estimatedPoint_WC = square.Localise();
    }

}
