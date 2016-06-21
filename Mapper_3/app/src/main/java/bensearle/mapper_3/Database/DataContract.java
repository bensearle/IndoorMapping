package bensearle.mapper_3.Database;

import android.provider.BaseColumns;

/**
 * Created by bensearle on 22/05/2016.
 */
public class DataContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DataContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FPDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "fpdata";
        public static final String COLUMN_NAME_FPTAG = "fptag"; // 00100.00200.00300
        public static final String COLUMN_NAME_FPREF = "fpref"; // AC1-LT01
        public static final String COLUMN_NAME_X = "x"; // 1
        public static final String COLUMN_NAME_Y = "y"; // 2
        public static final String COLUMN_NAME_Z = "z"; // 3
        public static final String COLUMN_NAME_SSSID = "ssid"; // CityU WLAN (WPA)
        public static final String COLUMN_NAME_BSSID = "bssid"; // 00:1a:1e:97:02:b0
        public static final String COLUMN_NAME_RSSI = "rssi"; // -61
    }

    /* Inner class that defines the table contents */
    public static abstract class TestDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "testdata";
        public static final String COLUMN_NAME_FPTAG = "fptag"; // 00100.00200.00300
        public static final String COLUMN_NAME_FPREF = "fpref"; // AC1-LT01
        public static final String COLUMN_NAME_X = "x"; // 1
        public static final String COLUMN_NAME_Y = "y"; // 2
        public static final String COLUMN_NAME_Z = "z"; // 3
        public static final String COLUMN_NAME_SSSID = "ssid"; // CityU WLAN (WPA)
        public static final String COLUMN_NAME_BSSID = "bssid"; // 00:1a:1e:97:02:b0
        public static final String COLUMN_NAME_RSSI = "rssi"; // -61
    }

    /* Inner class that defines the table contents */
    public static abstract class TestResults implements BaseColumns {
        public static final String TABLE_NAME = "testresult";
        public static final String COLUMN_NAME_FPTAG = "fptag"; // 00100.00200.00300
        public static final String COLUMN_NAME_ACTUAL_X = "actualX";
        public static final String COLUMN_NAME_ACTUAL_Y = "actualY";

        public static final String COLUMN_NAME_EST_X_DTa = "estX_DTa";
        public static final String COLUMN_NAME_EST_Y_DTa = "estY_DTa";
        public static final String COLUMN_NAME_EST_X_DTb = "estX_DTb";
        public static final String COLUMN_NAME_EST_Y_DTb = "estY_DTb";

        public static final String COLUMN_NAME_EST_X_OCa = "estX_OCa";
        public static final String COLUMN_NAME_EST_Y_OCa = "estY_OCa";
        public static final String COLUMN_NAME_EST_X_OCb = "estX_OCb";
        public static final String COLUMN_NAME_EST_Y_OCb = "estY_OCb";

        public static final String COLUMN_NAME_EST_X_WCa = "estX_WCa";
        public static final String COLUMN_NAME_EST_Y_WCa = "estY_WCa";
        public static final String COLUMN_NAME_EST_X_WCb = "estX_WCb";
        public static final String COLUMN_NAME_EST_Y_WCb = "estY_WCb";
    }
}
