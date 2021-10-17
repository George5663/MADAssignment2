package com.example.madassignment2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.madassignment2.Database.StudentSchema.StudentTable;

import java.util.ArrayList;
import java.util.List;

public class StudentList {
    private SQLiteDatabase db;
    private List<Student> students = new ArrayList<>();

    public void load(Context context) {
        //Creating a new database if there are no current ones
        if (this.db == null) {
            this.db = new StudentDB(context.getApplicationContext()).getWritableDatabase();
        }
        StudentCursor cursor = new StudentCursor(db.query(StudentSchema.StudentTable.name, null, null, null, null, null, null));
        try {
            //Fill the new list from Students in the database
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                students.add(cursor.getStudent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    public int size() {
        return students.size();
    }

    public Student get(int i) {
        return students.get(i);
    }

    public String add(Student newStudent) {
        //Adding a new student to the list and database
        students.add(newStudent);
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.columns.firstName, newStudent.getFirstName());
        cv.put(StudentTable.columns.email, newStudent.getEmail());
        cv.put(StudentTable.columns.lastName, newStudent.getLastName());
        cv.put(StudentTable.columns.phoneNumber, newStudent.getPhoneNumber());
        cv.put(StudentTable.columns.studentPhoto, newStudent.getStudentPicture());
        db.insert(StudentTable.name, null, cv);
        return "Successfully added " + newStudent.getFirstName() + " " + newStudent.getLastName();
    }

    public String remove(Student newStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i) == newStudent) {
                //Removing a student from the list and database
                students.remove(students.get(i));
                String[] whereValue = {newStudent.getLastName()};
                db.delete(StudentTable.name, StudentTable.columns.lastName + " = ?", whereValue);
                return "Successfully removed " + newStudent.getFirstName() + " " + newStudent.getLastName();
            }
        }
        return newStudent.getFirstName() + " " + newStudent.getLastName() + " does not exist";
    }

    public String update(Student newStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getLastName().equals(newStudent.getLastName())) {
                students.remove(students.get(i));
                students.add(newStudent);
                //Updating the old student under the same name with new details
                ContentValues cv = new ContentValues();
                cv.put(StudentTable.columns.firstName, newStudent.getFirstName());
                cv.put(StudentTable.columns.email, newStudent.getEmail());
                cv.put(StudentTable.columns.lastName, newStudent.getLastName());
                cv.put(StudentTable.columns.phoneNumber, newStudent.getPhoneNumber());
                cv.put(StudentTable.columns.studentPhoto, newStudent.getStudentPicture());
                String[] whereValue = {newStudent.getLastName()};
                db.update(StudentTable.name, cv, StudentTable.columns.lastName + " = ?", whereValue);
                return "Successfully updated " + newStudent.getLastName();
            }
        }
        return newStudent.getFirstName() + " " + newStudent.getLastName() + " does not exist";
    }

    public List<Student> getStudents() {
        return students;
    }
}