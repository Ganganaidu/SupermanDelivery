package com.supermandelivery.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.supermandelivery.MyApplication;
import com.supermandelivery.models.SavedAddress;

import java.lang.String;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    /**
     * The name of the database.
     */
    public static final String DB_NAME = "SuperManDelivery.db";

    /**
     * The DB's version number. This needs to be increased on schema changes.
     */
    public static final int DB_VERSION = 2;

    /**
     * Singleton instance of {@link DbHelper}.
     */
    private static DbHelper instance = null;
    private SQLiteDatabase db;

    /**
     * @return the {@link DbHelper} singleton.
     */
    public static DbHelper getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new DbHelper();
        }
    }

    private DbHelper() {
        super(MyApplication.getInstance().getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String recentAddress = "CREATE TABLE recentAddress(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude TEXT," +
                "longitude TEXT," +
                "remarks TEXT," +
                "address TEXT," +
                "flatNo TEXT," +
                "name TEXT," +
                "contactNo TEXT," +
                "entity1 TEXT," +
                "entity2 TEXT," +
                "id TEXT)";

        db.execSQL(recentAddress);
    }

    public void insertRecentAddress(SavedAddress savedAddress) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            if (savedAddress.getGeopoint() != null) {
                values.put("latitude", savedAddress.getGeopoint().getLat());
                values.put("longitude", savedAddress.getGeopoint().getLng());
            }
            values.put("remarks", savedAddress.getRemarks());
            values.put("address", savedAddress.getAddress());
            values.put("flatNo", savedAddress.getFlatNo());
            values.put("name", savedAddress.getName());
            values.put("contactNo", savedAddress.getContactNo());
            values.put("id", "");
            values.put("entity1", "");
            values.put("entity2", "");

            db.insert("recentAddress", null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // This on upgrade is used for changes of version to version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS table");
                break;
        }
    }

}
