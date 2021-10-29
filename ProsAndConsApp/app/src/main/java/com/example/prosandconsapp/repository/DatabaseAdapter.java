package com.example.prosandconsapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.prosandconsapp.repository.entities.GroupEntity;
import com.example.prosandconsapp.repository.entities.RecordEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllGroups(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TITLE};
        return  database.query(DatabaseHelper.TABLE_GROUP, columns, null, null, null, null, null);
    }

    public ArrayList<GroupEntity> getGroups(){
        ArrayList<GroupEntity> groups = new ArrayList<>();
        Cursor cursor = getAllGroups();
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            groups.add(new GroupEntity(id, title));
        }
        cursor.close();
        return  groups;
    }

    public ArrayList<RecordEntity> getRecords(long groupIdFilter){
        ArrayList<RecordEntity> records = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.TABLE_RECORD, DatabaseHelper.COLUMN_GROUP_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(groupIdFilter)});

        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            Date time = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECORD_TIME)));
            boolean positive = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSITIVE)) == 1;
            long groupId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GROUP_ID));
            records.add(new RecordEntity(id, time, positive, groupId));
        }
        cursor.close();
        return  records;
    }

    // do i need it?
    public ArrayList<RecordEntity> getRecords(boolean positiveFilter){
        ArrayList<RecordEntity> records = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.TABLE_RECORD, DatabaseHelper.COLUMN_POSITIVE);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(positiveFilter)});

        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            Date time = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECORD_TIME)));
            boolean positive = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSITIVE)) == 1;
            long groupId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GROUP_ID));
            records.add(new RecordEntity(id, time, positive, groupId));
        }
        cursor.close();
        return  records;
    }

    public long getGroupsCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_GROUP);
    }

    public long getRecordsCount(long groupIdFilter){
        long count = 0;

        String query = String.format("SELECT COUNT(*) as total FROM %s WHERE %s=?", DatabaseHelper.TABLE_RECORD, DatabaseHelper.COLUMN_GROUP_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(groupIdFilter)});

        while (cursor.moveToNext())
            count = cursor.getLong(0);

        cursor.close();
        return count;
    }

    public long getRecordsCount(long groupIdFilter, boolean positiveFilter){
        long count = 0;

        String query = String.format("SELECT COUNT(*) as total FROM %s WHERE %s=? AND %s=?",
                DatabaseHelper.TABLE_RECORD, DatabaseHelper.COLUMN_GROUP_ID, DatabaseHelper.COLUMN_POSITIVE);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(groupIdFilter), positiveFilter ? "1" : "0"});

        while (cursor.moveToNext())
            count = cursor.getLong(0);

        cursor.close();
        return count;
    }

    public long insertGroup(GroupEntity group){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_TITLE, group.title);

        return  database.insert(DatabaseHelper.TABLE_GROUP, null, cv);
    }

    public long insertRecord(RecordEntity record){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_RECORD_TIME, new Date().getTime());
        cv.put(DatabaseHelper.COLUMN_POSITIVE, record.positive);
        cv.put(DatabaseHelper.COLUMN_GROUP_ID, record.group_id);

        return  database.insert(DatabaseHelper.TABLE_RECORD, null, cv);
    }

    public long deleteGroup(long id){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_GROUP, whereClause, whereArgs);
    }

    public long deleteRecord(long id){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_RECORD, whereClause, whereArgs);
    }
}
