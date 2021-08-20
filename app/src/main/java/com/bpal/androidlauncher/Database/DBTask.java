package com.bpal.androidlauncher.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bpal.androidlauncher.Model.AppInfo;

import java.util.ArrayList;

public class DBTask extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "app";
    private static final String TABLE_Task = "Taskbar";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ICON = "icon";
    private static final String PACKAGE = "package";

    public DBTask(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Task + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + ICON + " TEXT,"
                + PACKAGE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Task);
        // Create tables again
        onCreate(db);
    }

    public void addtotask(String name, String icon, String pack){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT package FROM "+ TABLE_Task + " WHERE "+PACKAGE+" = "+pack;
        //Cursor cursor = db.rawQuery(query, null);
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(ICON, String.valueOf(icon));
        values.put(PACKAGE, pack);
        /*if (cursor.moveToNext()) {
          do {
              db.insert(TABLE_Task, null, values);
          }while (cursor.moveToNext());
        }*/
        db.insert(TABLE_Task, null, values);
        db.close();
    }

    public void deletetask(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Task, NAME+" = ?",new String[]{String.valueOf(name)});
        db.close();
    }

    public ArrayList<AppInfo> get_tbApps(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppInfo> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_Task;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToNext()) {
            do {
                list.add(new AppInfo(
                        cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ICON)),
                        cursor.getString(cursor.getColumnIndex(PACKAGE))
                ));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean findTask(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + NAME + " FROM " + TABLE_Task + " WHERE " +
                NAME + "=" + name ;
        //Cursor cursor = db.rawQuery(query, null);
        Cursor cur = db.query(TABLE_Task, null, "name = ?",
                new String[] {name}, null, null, null, null);
        /*if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;*/
        if (cur != null && cur.getCount()>0) {
            // duplicate found
            return true;
        }
        return false;

    }

}
