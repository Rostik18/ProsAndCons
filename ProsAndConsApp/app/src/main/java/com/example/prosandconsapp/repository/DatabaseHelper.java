package com.example.prosandconsapp.repository;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "prosandconsDB";
    private static final int SCHEMA = 1; // db version
    static final String TABLE_GROUP = "groups";
    static final String TABLE_RECORD = "records";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RECORD_TIME = "time";
    public static final String COLUMN_POSITIVE = "positive";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String INDEX_GROUP_ID = "group_id_index";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String a = "CREATE TABLE " + TABLE_GROUP +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT);";

        String b = "CREATE TABLE " + TABLE_RECORD +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD_TIME + " INTEGER, " +
                COLUMN_POSITIVE + " BOOLEAN NOT NULL CHECK ( " + COLUMN_POSITIVE + " IN (0, 1)), " +
                COLUMN_GROUP_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_GROUP_ID + ") REFERENCES " + TABLE_GROUP + " ON DELETE CASCADE);";

        db.execSQL("CREATE TABLE " + TABLE_GROUP +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_RECORD +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD_TIME + " INTEGER, " +
                COLUMN_POSITIVE + " BOOLEAN NOT NULL CHECK ( " + COLUMN_POSITIVE + " IN (0, 1)), " +
                COLUMN_GROUP_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_GROUP_ID + ") REFERENCES " + TABLE_GROUP + " ON DELETE CASCADE);");

        db.execSQL("CREATE INDEX " + INDEX_GROUP_ID + " ON " + TABLE_RECORD + " (" + COLUMN_GROUP_ID +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECORD);
        onCreate(db);
    }
}
