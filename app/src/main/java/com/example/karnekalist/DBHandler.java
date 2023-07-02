package com.example.karnekalist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "new";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String COL_ID = "id";
    private static final String COL_TASKS = "tasks";
    public DBHandler(@Nullable Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COL_TASKS + " TEXT)";
        db.execSQL(query);
    }



    public void addNewTask(String task){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASKS,task);
        long a = database.insert(TABLE_NAME,null,values);
        Log.e("TAG", String.valueOf(a));
        database.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<RecyclerAdapterModel> readTasks(){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<RecyclerAdapterModel> tasksModalArrayList = new ArrayList<>();

        if (cursorTasks.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
//                tasksModalArrayList.add(new RecyclerAdapterModel(cursorTasks.getString(1),cursorTasks.getInt(0)));
                RecyclerAdapterModel model = new RecyclerAdapterModel();
                model.setId(cursorTasks.getInt(0));
                model.setTask(cursorTasks.getString(1));

                tasksModalArrayList.add(model);
            } while (cursorTasks.moveToNext());
            // moving our cursor to next.
        }

        cursorTasks.close();
        return tasksModalArrayList;
    }

    public void deleteTask(int id) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, COL_ID+"=?", new String[]{String.valueOf(id)});
        rationaliseCol1Values();
//
        db.close();
    }

    public void updateTask(String newTask,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "id=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(COL_TASKS,newTask);
//        values.put(COL_ID, id);
        db.update(TABLE_NAME,values,where,whereArgs);
//        rationaliseCol1Values();
        db.close();
    }
    @SuppressLint("Range")
    private void rationaliseCol1Values() {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        Cursor csr = db.query(TABLE_NAME,null,null,null,null,null,COL_ID + " ASC");

        int rowcount = csr.getCount();
        long expected_id = 1;
        long current_id;
        String where_clause = COL_ID + "=?";
        String[] args = new String[1];

        while (csr.moveToNext()) {
            current_id = csr.getLong(csr.getColumnIndex(COL_ID));
            if (current_id != expected_id) {
                cv.clear();
                cv.put(COL_ID,expected_id);
                args[0] = String.valueOf(current_id);
                db.update(TABLE_NAME,cv,where_clause,args);
                db.update(TABLE_NAME,cv,where_clause,args);
            }
            expected_id++;
        }
        csr.close();
        // Now adjust sqlite_sequence
        where_clause = "name=?";
        args[0] = TABLE_NAME;
        cv.clear();
        cv.put("seq",String.valueOf(rowcount));
        db.update("sqlite_sequence",cv,where_clause,args);
    }

}
