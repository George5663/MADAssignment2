package com.example.madassignment2.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.madassignment2.Database.StudentSchema.StudentTable;

public class StudentCursor extends CursorWrapper {
    public StudentCursor(Cursor cursor) {
        super(cursor);
    }

    //Creating the Cursor to go over the database
    public Student getStudent() {
        String firstName = getString(getColumnIndex(StudentTable.columns.firstName));
        String lastName = getString(getColumnIndex(StudentTable.columns.lastName));
        String email = getString(getColumnIndex(StudentTable.columns.email));
        String studentPhoto = getString(getColumnIndex(StudentTable.columns.studentPhoto));
        int phoneNumber = getInt(getColumnIndex(StudentTable.columns.phoneNumber));
        return new Student(firstName, lastName, email, phoneNumber, studentPhoto);
    }
}
