package com.example.madassignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.provider.ContactsContract.CommonDataKinds.Email;

import com.example.madassignment2.Database.Student;
import com.example.madassignment2.Database.StudentList;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CONTACT = 1;
    private int userId, userPhoneNumber;
    private String userFirstName, userSecondName, userEmail, filename;
    private Bitmap userPhoto;
    private StudentList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new StudentList();
        list.load(getApplicationContext());
        Button registerStudent = (Button) findViewById(R.id.regStudentButton);
        Button viewStudents = (Button) findViewById(R.id.viewAllStudents);
        Button importStudent = (Button) findViewById(R.id.importStudentBtn);
        Button mathTest = (Button) findViewById(R.id.mathTestBtn);
        registerStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterStudent.class);
                startActivity(i);
            }
        });
        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewStudent.class);
                startActivity(i);
            }
        });
        importStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        mathTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MathTest.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] queryField = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                    ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

            Cursor cursor = this.getContentResolver().query(contactUri, queryField, null, null, null);
            try {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    userId = cursor.getInt(0);
                    userFirstName = cursor.getString(1);
                    userSecondName = cursor.getString(2);
                    userPhoneNumber = cursor.getInt(3);
                }
            } finally {
                cursor.close();
            }
            Cursor emailCursor = this.getContentResolver().query(Email.CONTENT_URI, new String[]{Email.ADDRESS}, Email.CONTACT_ID + " = ?", new String[]{String.valueOf(userId)}, null, null);
            try {
                if(emailCursor.getCount() > 0)
                {
                    emailCursor.moveToFirst();
                    userEmail = emailCursor.getString(0);
                }
            }
            finally
            {
                emailCursor.close();
            }
            try {
                userPhoto = openPhoto(userId);
                filename = "bitmap.png";
                FileOutputStream stream = null;
                stream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                userPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //Cleanup
                stream.close();
                userPhoto.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        list.add(new Student(userFirstName, userSecondName, userEmail, userPhoneNumber, filename));
    }
    public Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;

    }
}