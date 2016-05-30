package bensearle.mapper_3.Database;

import android.provider.BaseColumns;

/**
 * Created by bensearle on 22/05/2016.
 */
public class FPDataContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FPDataContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FPDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "fpdata";
        public static final String COLUMN_NAME_FPTAG = "fptag"; // 001002003
        public static final String COLUMN_NAME_FPREF = "fpref"; // AC1-LT01
        public static final String COLUMN_NAME_X = "x"; // 1
        public static final String COLUMN_NAME_Y = "y"; // 2
        public static final String COLUMN_NAME_Z = "z"; // 3
        public static final String COLUMN_NAME_SSSID = "ssid"; // CityU WLAN (WPA)
        public static final String COLUMN_NAME_BSSID = "bssid"; // 00:1a:1e:97:02:b0
        public static final String COLUMN_NAME_RSSI = "rssi"; // -61
    }
}
