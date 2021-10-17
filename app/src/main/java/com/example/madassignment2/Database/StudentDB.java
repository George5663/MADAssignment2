package com.example.madassignment2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.madassignment2.Database.StudentSchema.StudentTable;

public class StudentDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "student.db";

    public StudentDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create a new table
        db.execSQL("CREATE TABLE " + StudentTable.name + "(" +
                StudentTable.columns.firstName + " TEXT, " +
                StudentTable.columns.lastName + " TEXT, " +
                StudentTable.columns.email + " TEXT, " +
                StudentTable.columns.studentPhoto + " TEXT, " +
                StudentTable.columns.phoneNumber + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
    }
}
