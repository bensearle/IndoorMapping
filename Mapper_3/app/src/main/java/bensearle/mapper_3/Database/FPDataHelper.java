package bensearle.mapper_3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Iterator;
import java.util.Map;

import bensearle.mapper_3.Database.FPDataContract.FPDataEntry;
import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;

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


    /*
     * ACCESSING THE DATABASE
     */



    public void AddFP(Fingerprint fingerprint){
        // TODO check location is not null
        String fptag = fingerprint.GetTag();
        Point3D location = fingerprint.GetPosition();
        Map<String, Integer> waps = fingerprint.GetWAPs();

        // add each WAP to database, with fptag, location
        for(Iterator i = waps.entrySet().iterator(); i.hasNext();) {
            Map.Entry item = (Map.Entry) i.next();
            String bssid = (String) item.getKey();
            Integer rssi = (Integer) item.getValue();

            addFPData (fptag, location, bssid, rssi); // add to DB
        }
    }

    private boolean addFPData (String fptag, Point3D p, String bssid, Integer rssi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FPDataEntry.COLUMN_NAME_FPTAG, fptag);
        contentValues.put(FPDataEntry.COLUMN_NAME_X, p.X);
        contentValues.put(FPDataEntry.COLUMN_NAME_Y, p.Y);
        contentValues.put(FPDataEntry.COLUMN_NAME_Z, p.Z);
        contentValues.put(FPDataEntry.COLUMN_NAME_BSSID, bssid);
        contentValues.put(FPDataEntry.COLUMN_NAME_RSSI, rssi);
        long newRowID = db.insert(FPDataEntry.TABLE_NAME, null, contentValues);
        return true;
    }

    public void GetFingerprintByWAP(String wapBSSID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                FPDataEntry.COLUMN_NAME_FPTAG
        };

        String selection = FPDataEntry.COLUMN_NAME_BSSID+"=?";
        String selectionArgs[] = {wapBSSID};

        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = FPDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                FPDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                   // filter by row groups
                sortOrder               // sort order
        );
    }

}