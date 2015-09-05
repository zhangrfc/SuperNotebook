package com.pennapps.camnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by zhangrf on 2015/9/5.
 */
public class InstaNotebookDBHelper extends SQLiteOpenHelper {

    /*Global variable for the database table*/
    public static final String DATABASE_NAME = "SuperNotebook.db";
    public static final String NOTE_TABLE_NAME = "notes";
    public static final String NOTE_COLUMN_ID = "note_id";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_CONTEXT = "context";
    public static final String NOTE_COLUMN_TIME = "time";
    public static final String NOTE_COLUMN_DATE = "date";
    public static final String NOTE_COLUMN_HOST = "host";
    public static final String NOTE_COLUMN_ADDRESS = "address";
    public static final String NOTE_COLUMN_PICTURE = "picture";
    public static final String NOTE_COLUMN_CATEGORY = "category";

    /*Constructor*/
    public InstaNotebookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + NOTE_TABLE_NAME +
                        " (" + NOTE_COLUMN_ID + " integer primary key, " +
                        NOTE_COLUMN_TITLE + " text, " +
                        NOTE_COLUMN_CONTEXT + " text, " +
                        NOTE_COLUMN_TIME + " text, " +
                        NOTE_COLUMN_DATE + " text, " +
                        NOTE_COLUMN_HOST + " text, " +
                        NOTE_COLUMN_ADDRESS + " text, " +
                        NOTE_COLUMN_PICTURE + " text, " +
                        NOTE_COLUMN_CATEGORY + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
        onCreate(db);
    }

    public boolean clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME, null, null);
        return true;
    }

    public boolean insertNote (String title, String context, String time, String date,
                                  String host, String address, String picture, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTEXT, context);
        contentValues.put(NOTE_COLUMN_TIME, time);
        contentValues.put(NOTE_COLUMN_DATE, date);
        contentValues.put(NOTE_COLUMN_HOST, host);
        contentValues.put(NOTE_COLUMN_ADDRESS, address);
        contentValues.put(NOTE_COLUMN_PICTURE, picture);
        contentValues.put(NOTE_COLUMN_CATEGORY, category);
        db.insert(NOTE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getOneNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + NOTE_TABLE_NAME + " where " + NOTE_COLUMN_ID+ "="+ id +"", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTE_TABLE_NAME);
        return numRows;
    }

    public boolean updateNote (Integer id, String title, String context, String time, String date,
                               String host, String address, String picture, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTEXT, context);
        contentValues.put(NOTE_COLUMN_TIME, time);
        contentValues.put(NOTE_COLUMN_DATE, date);
        contentValues.put(NOTE_COLUMN_HOST, host);
        contentValues.put(NOTE_COLUMN_ADDRESS, address);
        contentValues.put(NOTE_COLUMN_PICTURE, picture);
        contentValues.put(NOTE_COLUMN_CATEGORY, category);
        db.update(NOTE_TABLE_NAME, contentValues, NOTE_COLUMN_ID + " = ?", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNote (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTE_TABLE_NAME, NOTE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id)} );
    }

    public ArrayList<Note> getAllNotes()
    {
        ArrayList<Note> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NOTE_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Note note = new Note();
            note.NOTE_COLUMN_ID = res.getString(res.getColumnIndex(NOTE_COLUMN_ID));
            note.NOTE_COLUMN_TITLE = res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE));
            note.NOTE_COLUMN_CONTEXT = res.getString(res.getColumnIndex(NOTE_COLUMN_CONTEXT));
            note.NOTE_COLUMN_TIME = res.getString(res.getColumnIndex(NOTE_COLUMN_TIME));
            note.NOTE_COLUMN_DATE = res.getString(res.getColumnIndex(NOTE_COLUMN_DATE));
            note.NOTE_COLUMN_HOST = res.getString(res.getColumnIndex(NOTE_COLUMN_HOST));
            note.NOTE_COLUMN_ADDRESS = res.getString(res.getColumnIndex(NOTE_COLUMN_ADDRESS));
            note.NOTE_COLUMN_PICTURE = res.getString(res.getColumnIndex(NOTE_COLUMN_PICTURE));
            note.NOTE_COLUMN_CATEGORY = res.getString(res.getColumnIndex(NOTE_COLUMN_CATEGORY));
            array_list.add(note);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllTitles() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NOTE_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }
}
