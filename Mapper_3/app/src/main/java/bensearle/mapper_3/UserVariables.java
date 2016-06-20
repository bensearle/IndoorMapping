package bensearle.mapper_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bensearle on 30/05/2016.
 */
public class UserVariables {
    public static int EUCLIDEAN_DISTANCE_POINTS = 5; // how many points/WAPs to compare when calculating Euclidean distance
    public static double DECREASING_TRIANGLES_FACTOR = 0.1; // the factor to apply to ED before decreasing the size of a triangle

    public static double INCREASING_CIRCLES_FACTOR = 1; // the factor to apply to ED before increasing the size of a circle
    public static int INCREASING_CIRCLES_PERCENT_OVERLAPPING = 80; // what percent of circles must overlap before estimating position

    //TODO
    // change to 1, set as 9 for testing****
    public static int GRID_THICKNESS = 1; // what is the x/y distance between RPs

    public static List<String> UNIVERSITY_NETWORKS = new ArrayList<String>(){{
        add("CityU WLAN (WPA)");
        add("CityUGuest");
        add("eduroam");
        add("Universities WiFi");
        add("Wi-Fi.HK via CityU");
    }};
}
