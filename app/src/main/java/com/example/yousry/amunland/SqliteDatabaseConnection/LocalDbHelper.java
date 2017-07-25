package com.example.yousry.amunland.SqliteDatabaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yousry.amunland.MainClasses.Landmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yousry on 4/6/2017.
 */

public class LocalDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "amunland";

    // Contacts table name
    private static final String TABLE_LANDMARKS = "landmarks";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_PHOTO = "photo";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public LocalDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }





    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LANDMARKS_TABLE = "CREATE TABLE " + TABLE_LANDMARKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_PHOTO + " TEXT"
                + ")";
        db.execSQL(CREATE_LANDMARKS_TABLE);
    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDMARKS);

        // Create tables again
        onCreate(db);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void onDelete(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDMARKS);
        onCreate(db);
    }













    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Landmark
    public void addLandmark(Landmark landmark) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, landmark.getName());
        values.put(KEY_DESCRIPTION, landmark.getDescription());
        values.put(KEY_ADDRESS, landmark.getAddress());
        values.put(KEY_LATITUDE, landmark.getLat());
        values.put(KEY_LONGITUDE, landmark.getLng());
        values.put(KEY_PHOTO, landmark.getImageName());
        // Inserting Row
        db.insert(TABLE_LANDMARKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Landmark
    public Landmark getLandmark(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LANDMARKS, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESCRIPTION, KEY_ADDRESS, KEY_LATITUDE, KEY_LONGITUDE, KEY_PHOTO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        Landmark landmark = null;
        if (cursor != null) {
            landmark = new Landmark(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)),
                    cursor.getString(6));
        }


        // return contact
        return landmark;
    }

    public void Open(){
        db =this.getWritableDatabase();
    }
    public Boolean isOpen(){
        if(db.isOpen()){
            return true;
        }
        return false;
    }
    public void Close()
    {
        db.close();
    }
    // Getting All Landmarks
    public List<Landmark> getAllLandmarks() {
        List<Landmark> landmarkList = new ArrayList<Landmark>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LANDMARKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Landmark landmark = new Landmark();
                landmark.setID(Integer.parseInt(cursor.getString(0)));
                landmark.setName(cursor.getString(1));
                landmark.setDescription(cursor.getString(2));
                landmark.setAddress(cursor.getString(3));
                landmark.setLat(cursor.getDouble(4));
                landmark.setLng(cursor.getDouble(5));
                landmark.setImageName(cursor.getString(6));

                // Adding contact to list
                landmarkList.add(landmark);
            } while (cursor.moveToNext());
        }

        // return contact list
        return landmarkList;
    }

    // Updating single Landmark
    public int updateLandmark(Landmark landmark) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, landmark.getName());
        values.put(KEY_DESCRIPTION, landmark.getName());
        values.put(KEY_ADDRESS, landmark.getName());
        values.put(KEY_LATITUDE, landmark.getName());
        values.put(KEY_LONGITUDE, landmark.getName());
        values.put(KEY_PHOTO, landmark.getName());

        // updating row
        return db.update(TABLE_LANDMARKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(landmark.getID()) });
    }

    // Deleting single Landmark
    public void deleteLandmark(Landmark landmark) {
        db = this.getWritableDatabase();
        db.delete(TABLE_LANDMARKS, KEY_ID + " = ?",
                new String[] { String.valueOf(landmark.getID()) });
    }


    // Getting Landmarks Count
    public int getLandmarksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LANDMARKS;
        this.db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
