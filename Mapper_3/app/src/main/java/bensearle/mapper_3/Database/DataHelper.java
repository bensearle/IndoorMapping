package bensearle.mapper_3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import bensearle.mapper_3.Database.DataContract.*;
import bensearle.mapper_3.Structures.Fingerprint;
import bensearle.mapper_3.Structures.Point3D;

/**
 * Created by bensearle on 22/05/2016.
 */

public class DataHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FingerprintDatabase.db";

    private static final String TYPE_TEXT = " TEXT ";
    private static final String TYPE_INTEGER = " INTEGER ";
    private static final String TYPE_REAL = " REAL ";
    private static final String COMMA = ",";

    private static final String SQL_CREATE_RPDATA_TABLE =
            "CREATE TABLE " + FPDataEntry.TABLE_NAME + " (" +
                    FPDataEntry._ID + " INTEGER PRIMARY KEY," +
                    FPDataEntry.COLUMN_NAME_FPTAG + TYPE_TEXT + COMMA +
                    FPDataEntry.COLUMN_NAME_X + TYPE_REAL + COMMA +
                    FPDataEntry.COLUMN_NAME_Y + TYPE_REAL + COMMA +
                    FPDataEntry.COLUMN_NAME_Z + TYPE_REAL + COMMA +
                    FPDataEntry.COLUMN_NAME_BSSID + TYPE_TEXT + COMMA +
                    FPDataEntry.COLUMN_NAME_RSSI + TYPE_INTEGER +
            " )";

    private static final String SQL_CREATE_TESTDATA_TABLE =
            "CREATE TABLE " + TestDataEntry.TABLE_NAME + " (" +
                    TestDataEntry._ID + " INTEGER PRIMARY KEY," +
                    TestDataEntry.COLUMN_NAME_FPTAG + TYPE_TEXT + COMMA +
                    TestDataEntry.COLUMN_NAME_X + TYPE_REAL + COMMA +
                    TestDataEntry.COLUMN_NAME_Y + TYPE_REAL + COMMA +
                    TestDataEntry.COLUMN_NAME_Z + TYPE_REAL + COMMA +
                    TestDataEntry.COLUMN_NAME_BSSID + TYPE_TEXT + COMMA +
                    TestDataEntry.COLUMN_NAME_RSSI + TYPE_INTEGER +
                    " )";

    private static final String SQL_DELETE_RPDATA_ENTRIES =
            "DROP TABLE IF EXISTS " + FPDataEntry.TABLE_NAME;

    private static final String SQL_DELETE_TESTDATA_ENTRIES =
            "DROP TABLE IF EXISTS " + TestDataEntry.TABLE_NAME;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //onUpgrade(this.getWritableDatabase(),1,1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RPDATA_TABLE);
        db.execSQL(SQL_CREATE_TESTDATA_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_RPDATA_ENTRIES);
        db.execSQL(SQL_DELETE_TESTDATA_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public ArrayList<String> getTableNames() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        ArrayList<String> tables = new ArrayList<String>();
        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            // get string of data in column 0
            tables.add(results.getString(0));
        }

        return tables;

    }

    /*
     * ACCESSING THE REFERENCE POINT DATA
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

    public ArrayList<String> GetFingerprintByWAP(String wapBSSID){
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
                filter,                 // filter by row groups
                sortOrder               // sort order
        );

        ArrayList<String> fpTAGs = new ArrayList<String>();
        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            // get string of data in column 0
            fpTAGs.add(results.getString(0));
        }

        return fpTAGs;
    }

    public Cursor GetFingerprintByTag(String tag){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                FPDataEntry.COLUMN_NAME_FPTAG,
                FPDataEntry.COLUMN_NAME_X,
                FPDataEntry.COLUMN_NAME_Y,
                FPDataEntry.COLUMN_NAME_Z,
                FPDataEntry.COLUMN_NAME_BSSID,
                FPDataEntry.COLUMN_NAME_RSSI
        };

        String selection = FPDataEntry.COLUMN_NAME_FPTAG+"=?";
        String selectionArgs[] = {tag};

        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = FPDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                FPDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                 // filter by row groups
                sortOrder               // sort order
        );

        return results;
    }


    public Cursor GetAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                FPDataEntry.COLUMN_NAME_FPTAG,
                FPDataEntry.COLUMN_NAME_X,
                FPDataEntry.COLUMN_NAME_Y,
                FPDataEntry.COLUMN_NAME_Z,
                FPDataEntry.COLUMN_NAME_BSSID,
                FPDataEntry.COLUMN_NAME_RSSI
        };

        String selection = "";
        String selectionArgs[] = {};

        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = FPDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                FPDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                 // filter by row groups
                sortOrder               // sort order
        );


        ArrayList<String> fpTAGs = new ArrayList<String>();
        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            // get string of data in column 0
            fpTAGs.add(results.getString(0));
        }

        return results;
    }

    public ArrayList<String> GetRPFPs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                FPDataEntry.COLUMN_NAME_FPTAG,
                FPDataEntry.COLUMN_NAME_X,
                FPDataEntry.COLUMN_NAME_Y,
                FPDataEntry.COLUMN_NAME_Z
        };
        String selection = "";
        String selectionArgs[] = {};
        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = FPDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                true,
                FPDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                 // filter by row groups
                sortOrder,              // sort order
                null
        );

        ArrayList<String> fpTAGs = new ArrayList<String>();
        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            // get string of data in column 0
            fpTAGs.add(results.getString(0));
        }

        return fpTAGs;
    }

    /*
     * ACCESSING THE TEST DATA
     */

    public void AddTestFP(Fingerprint fingerprint){
        // TODO check location is not null
        String fptag = fingerprint.GetTag();
        Point3D location = fingerprint.GetPosition();
        Map<String, Integer> waps = fingerprint.GetWAPs();

        // add each WAP to database, with fptag, location
        for(Iterator i = waps.entrySet().iterator(); i.hasNext();) {
            Map.Entry item = (Map.Entry) i.next();
            String bssid = (String) item.getKey();
            Integer rssi = (Integer) item.getValue();

            addTestFPData(fptag, location, bssid, rssi); // add to DB
        }
    }

    private boolean addTestFPData (String fptag, Point3D p, String bssid, Integer rssi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TestDataEntry.COLUMN_NAME_FPTAG, fptag);
        contentValues.put(TestDataEntry.COLUMN_NAME_X, p.X);
        contentValues.put(TestDataEntry.COLUMN_NAME_Y, p.Y);
        contentValues.put(TestDataEntry.COLUMN_NAME_Z, p.Z);
        contentValues.put(TestDataEntry.COLUMN_NAME_BSSID, bssid);
        contentValues.put(TestDataEntry.COLUMN_NAME_RSSI, rssi);
        long newRowID = db.insert(TestDataEntry.TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor GetTestFingerprintByTag(String tag){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                TestDataEntry.COLUMN_NAME_FPTAG,
                TestDataEntry.COLUMN_NAME_X,
                TestDataEntry.COLUMN_NAME_Y,
                TestDataEntry.COLUMN_NAME_Z,
                TestDataEntry.COLUMN_NAME_BSSID,
                TestDataEntry.COLUMN_NAME_RSSI
        };

        String selection = TestDataEntry.COLUMN_NAME_FPTAG+"=?";
        String selectionArgs[] = {tag};

        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = TestDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                TestDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                 // filter by row groups
                sortOrder               // sort order
        );

        return results;
    }


    /**
     * get the list of test points
     * @return
     */
    public ArrayList<String> GetTestFPs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                TestDataEntry.COLUMN_NAME_FPTAG,
                TestDataEntry.COLUMN_NAME_X,
                TestDataEntry.COLUMN_NAME_Y,
                TestDataEntry.COLUMN_NAME_Z
        };
        String selection = "";
        String selectionArgs[] = {};
        String groupBy = null;
        String filter = null; // HAVING clause
        String sortOrder = TestDataEntry.COLUMN_NAME_FPTAG + " DESC";

        Cursor results = db.query(
                true,                   // distinct
                TestDataEntry.TABLE_NAME, // table to query
                columns,                // columns to return
                selection,              // columns for the WHERE clause
                selectionArgs,          // values for the WHERE clause
                groupBy,                // group the rows
                filter,                 // filter by row groups
                sortOrder,              // sort order
                null                    // limit the number of rows
        );

        ArrayList<String> fpTAGs = new ArrayList<String>();
        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            // get string of data in column 0
            fpTAGs.add(results.getString(0));
        }

        return fpTAGs;
    }

    public void DeleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ FPDataEntry.TABLE_NAME);
        db.execSQL("delete from "+ TestDataEntry.TABLE_NAME);
    }
}