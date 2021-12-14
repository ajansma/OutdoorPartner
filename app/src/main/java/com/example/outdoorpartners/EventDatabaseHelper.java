package com.example.outdoorpartners;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventDatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "eventDatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String EVENTS_TABLE = "eventsTable";
    static final String ID = "_id"; // by convention
    static final String NAME = "name";
    static final String DESCRIPTION = "description";
    static final String DAY = "day";
    static final String MONTH = "month";
    static final String YEAR = "year";
    static final String MIN = "min";
    static final String HOUR = "hour";
    static final String TYPE = "type";
    static final String LOCATION = "location";
    static final String IMAGE_RESOURCE_ID = "imageResourceId";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate = "CREATE TABLE " + EVENTS_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                "DAY" + " INTEGER, " +
                "MONTH" + " INTEGER, " +
                "YEAR" + " INTEGER, " +
                "MIN" + " INTEGER, " +
                HOUR + " INTEGER," +
                TYPE + " TEXT, " +
                LOCATION + " TEXT, " +
                IMAGE_RESOURCE_ID + " INTEGER)";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertContact(Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, event.getName());
        contentValues.put(DESCRIPTION, event.getDescription());
        contentValues.put("DAY", event.getDay());
        contentValues.put("MONTH", event.getMonth());
        contentValues.put("YEAR", event.getYear());
        contentValues.put("MIN", event.getMin());
        contentValues.put("HOUR", event.getHour());
        contentValues.put("TYPE", event.getType());
        contentValues.put("LOCATION", event.getLocation());
        contentValues.put(IMAGE_RESOURCE_ID, event.getImage());


        // get a writeable ref to the database
        SQLiteDatabase db = getWritableDatabase();
        db.insert(EVENTS_TABLE, null, contentValues);
        // close the writeable ref when done!!
        db.close();
    }

    public Cursor getSelectAllCursor() {
        // we need to construct a query to get a cursor
        // to step through records
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EVENTS_TABLE, new String[]{ID,
                        NAME,
                        DESCRIPTION,
                        DAY,
                        MONTH,
                        YEAR,
                        MIN,
                        HOUR,
                        TYPE,
                        LOCATION,
                        IMAGE_RESOURCE_ID},
                null, null, null,
                null, null);
        return cursor;
    }

    public List<Event> getSelectAllContacts() {
        List<Event> events = new ArrayList<>();
        Cursor cursor = getSelectAllCursor();
        // the cursor starts "before" the first record
        // in case there is no first record
        while (cursor.moveToNext()) { // returns false when no more records to process
            // parse the field values
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int day = cursor.getInt(3);
            int month = cursor.getInt(4);
            int year = cursor.getInt(5);
            int min = cursor.getInt(6);
            int hour = cursor.getInt(7);
            String type = cursor.getString(8);
            String location = cursor.getString(9);
            int image = cursor.getInt(10);
            Event event = new Event(description, id, image, location, name, year, month, day, hour, min, type);
            events.add(event);
        }
        return events;
    }

    public Event getSelectEventById(int idParam){
        // to step through records
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EVENTS_TABLE, new String[]{ID,
                        NAME,
                        DESCRIPTION,
                        DAY,
                        MONTH,
                        YEAR,
                        MIN,
                        HOUR,
                        TYPE,
                        LOCATION,
                        IMAGE_RESOURCE_ID},
                ID + "=?", new String[]{"" + idParam}, null,
                null, null);
        Event event = null;
        if(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int day = cursor.getInt(3);
            int month = cursor.getInt(4);
            int year = cursor.getInt(5);
            int min = cursor.getInt(6);
            int hour = cursor.getInt(7);
            String type = cursor.getString(8);
            String location = cursor.getString(9);
            int image = cursor.getInt(10);
            event = new Event(description, id, image, location, name, year, month, day, hour, min, type);
        }
        return event;
    }



    public List<Integer> getSelectAllIds(){
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = getSelectAllCursor();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int day = cursor.getInt(3);
            int month = cursor.getInt(4);
            int year = cursor.getInt(5);
            int min = cursor.getInt(6);
            int hour = cursor.getInt(7);
            String type = cursor.getString(8);
            String location = cursor.getString(9);
            int image = cursor.getInt(10);
            ids.add(id);
        }
        return ids;
    }

    public void insertEvent(){

    }


    public void deleteAllContacts() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(EVENTS_TABLE, null, null);
        db.close();
    }

}
