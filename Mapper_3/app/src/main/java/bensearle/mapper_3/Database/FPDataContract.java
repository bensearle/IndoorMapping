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
        public static final String COLUMN_NAME_FPTAG = "fptag";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        public static final String COLUMN_NAME_Z = "z";
        public static final String COLUMN_NAME_BSSID = "bssid";
        public static final String COLUMN_NAME_RSSI = "rssi";
    }
}
