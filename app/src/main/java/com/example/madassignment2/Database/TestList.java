package com.example.madassignment2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.madassignment2.Database.TestSchema.TestTable;

import java.util.ArrayList;
import java.util.List;

public class TestList {
    private SQLiteDatabase db;
    private List<TestResults> Tests = new ArrayList<>();

    public void load(Context context) {
        //Creating a new database if there are no current ones
        if (this.db == null) {
            this.db = new TestDB(context.getApplicationContext()).getWritableDatabase();
        }
        TestCursor cursor = new TestCursor(db.query(TestTable.name, null, null, null, null, null, null));
        try {
            //Fill the new list from Tests in the database
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Tests.add(cursor.getTest());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    public int size() {
        return Tests.size();
    }

    public TestResults get(int i) {
        return Tests.get(i);
    }

    public String add(TestResults newTest) {
        //Adding a new Test to the list and database
        Tests.add(newTest);
        ContentValues cv = new ContentValues();
        cv.put(TestTable.columns.totalTime, newTest.getTotalTime());
        cv.put(TestTable.columns.startTime, newTest.getStartTime());
        cv.put(TestTable.columns.totalScore, newTest.getTotalScore());
        db.insert(TestTable.name, null, cv);
        return "Successfully added test";
    }

    public List<TestResults> getTests() {
        return Tests;
    }
}