package com.example.madassignment2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.madassignment2.Database.TestSchema.TestTable;

public class TestDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Test.db";

    public TestDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create a new table
        db.execSQL("CREATE TABLE " + TestTable.name + "(" +
                TestTable.columns.totalScore + " INTEGER, " +
                TestTable.columns.startTime + " TEXT, " +
                TestTable.columns.totalTime + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
    }
}
