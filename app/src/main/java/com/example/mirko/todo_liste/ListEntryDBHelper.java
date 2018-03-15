package com.example.mirko.todo_liste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Schweiger on 15.03.2018.
 */

public class ListEntryDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todoliste";
    private static final String TABLE_TODO = "todo";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXT = "textdata";

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_TODO +
            " (" + COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, " +
            COLUMN_TEXT + " TEXT NOT NULL);";

    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_TEXT};

    private SQLiteDatabase database;

    public ListEntryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void open(){
        this.database = super.getWritableDatabase();
    }

    @Override
    public void close(){
        super.close();
    }

    public void saveEntry(ListEntry entry){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entry.getId());
        values.put(COLUMN_TEXT, entry.getText());

        this.database.insert(TABLE_TODO, null, values);
    }

    public List<ListEntry> getAllEntries(){
        List<ListEntry> entryArrayList = new ArrayList<>();

        Cursor cursor = this.database.query(TABLE_TODO, COLUMNS, null, null, null, null, null);

        if(cursor!= null){
            cursor.moveToFirst();

            while (cursor.moveToNext()){
                ListEntry listEntry = cursorToEntryList(cursor);
                entryArrayList.add(listEntry);
            }

            cursor.close();
        }

        return entryArrayList;
    }

    private ListEntry cursorToEntryList(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int textIndex = cursor.getColumnIndex(COLUMN_TEXT);

        String id = cursor.getString(idIndex);
        String text = cursor.getString(textIndex);

        ListEntry listEntry = new ListEntry(text);
        listEntry.setId(id);

        return listEntry;
    }

    public boolean removeListEntry(ListEntry listEntry){
        if(listEntry != null){
            return this.database.delete(TABLE_TODO, COLUMN_ID + " = '" + listEntry.getId() + "' ", null) > 0;
        }

        return false;
    }

}
