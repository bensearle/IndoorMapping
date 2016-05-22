package bensearle.mapper_3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bensearle.mapper_3.Database.FPDataContract.FPDataEntry;
/**
 * Created by bensearle on 22/05/2016.
 */

public class FPDataHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FingerprintDatabase.db";

    private static final String TYPE_TEXT = " TEXT ";
    private static final String TYPE_INTEGER = " INTEGER ";
    private static final String COMMA = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FPDataEntry.TABLE_NAME + " (" +
                    FPDataEntry._ID + " INTEGER PRIMARY KEY," +
                    FPDataEntry.COLUMN_NAME_FPTAG + TYPE_TEXT + COMMA +
                    FPDataEntry.COLUMN_NAME_X + TYPE_INTEGER + COMMA +
                    FPDataEntry.COLUMN_NAME_Y + TYPE_INTEGER + COMMA +
                    FPDataEntry.COLUMN_NAME_Z + TYPE_INTEGER + COMMA +
                    FPDataEntry.COLUMN_NAME_BSSID + TYPE_TEXT + COMMA +
                    FPDataEntry.COLUMN_NAME_RSSI + TYPE_INTEGER +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FPDataEntry.TABLE_NAME;

    public FPDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}