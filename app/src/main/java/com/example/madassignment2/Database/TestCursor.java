package com.example.madassignment2.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.madassignment2.Database.TestSchema.TestTable;

public class TestCursor extends CursorWrapper {
    public TestCursor(Cursor cursor) {
        super(cursor);
    }

    //Creating the Cursor to go over the database
    public TestResults getTest() {
        int totalScore = getInt(getColumnIndex(TestTable.columns.totalScore));
        String startTime = getString(getColumnIndex(TestTable.columns.startTime));
        String totalTime = getString(getColumnIndex(TestTable.columns.totalTime));

        return new TestResults(totalScore, startTime, totalTime);
    }
}
