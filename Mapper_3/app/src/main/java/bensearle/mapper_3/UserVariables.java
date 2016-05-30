package bensearle.mapper_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bensearle on 30/05/2016.
 */
public class UserVariables {
    public static int EUCLIDEAN_DISTANCE_POINTS = 5; // how many points/WAPs to compare when calculating Euclidean distance
    public static int DECREASING_TRIANGLES_FACTOR = 1; // the factor to apply to ED before decreasing the size of a triangle

    public static List<String> UNIVERSITY_NETWORKS = new ArrayList<String>(){{
        add("CityU WLAN (WPA)");
        add("CityUGuest");
        add("eduroam");
        add("Universities WiFi");
        add("Wi-Fi.HK via CityU");
    }};
}
