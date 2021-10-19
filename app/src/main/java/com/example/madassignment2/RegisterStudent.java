package com.example.madassignment2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madassignment2.Database.Student;
import com.example.madassignment2.Database.StudentList;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStudent extends AppCompatActivity {
    private String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private Bitmap imageBitmap;
    private StudentList list;
    private Bundle extras;
    private String pictureString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_student);
        list = new StudentList();
        list.load(getApplicationContext());
        Button loadImage = (Button) findViewById(R.id.loadImage);
        ImageView picture = (ImageView) findViewById(R.id.picureId);
        Button addStudent = (Button) findViewById(R.id.addStudent);
        Button editStudent = (Button) findViewById(R.id.editStudent);
        Button deleteStudent = (Button) findViewById(R.id.deleteStudent);
        EditText lName = (EditText) findViewById(R.id.studentLName);
        EditText fName = (EditText) findViewById(R.id.studentFName);
        EditText phoneNumber = (EditText) findViewById(R.id.studentPhoneNumber);
        EditText email = (EditText) findViewById(R.id.studentEmail);
        //Email Verification
        Pattern pattern = Pattern.compile(regex);
        if ((extras = getIntent().getExtras()) != null) {
            if(extras.getString("image") != null) {
                pictureString = extras.getString("image");
                try {
                    FileInputStream is = this.openFileInput(pictureString);
                    imageBitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    picture.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(extras.getString("firstName") != null)
            {
                fName.setText(extras.getString("firstName"));
                lName.setText(extras.getString("lastName"));
                email.setText(extras.getString("email"));
                phoneNumber.setText(String.valueOf(extras.getInt("phoneNumber")));
                try {
                    pictureString = extras.getString("filename");
                    FileInputStream is = this.openFileInput(pictureString);
                    imageBitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    picture.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fName.getText().toString().matches("") && !lName.getText().toString().matches("") && !email.getText().toString().matches("") && !phoneNumber.getText().toString().matches("") && !(picture.getDrawable() == null)) {
                    Toast.makeText(getApplicationContext(), list.remove(new Student(fName.getText().toString(), lName.getText().toString(), email.getText().toString(), Integer.parseInt(phoneNumber.getText().toString()), bitmapToString())), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You're missing values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterStudent.this, AddPhoto.class);
                finish();
                startActivity(i);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fName.getText().toString().matches("") && !lName.getText().toString().matches("") && !email.getText().toString().matches("") && !phoneNumber.getText().toString().matches("") && !(picture.getDrawable() == null)) {
                    Matcher matcher = pattern.matcher(email.getText());
                    if (!matcher.matches()) {
                        Toast.makeText(getApplicationContext(), "Your email is not valid", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), list.add(new Student(fName.getText().toString(), lName.getText().toString(), email.getText().toString(), Integer.parseInt(phoneNumber.getText().toString()), bitmapToString())), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have an empty field", Toast.LENGTH_LONG).show();
                }
            }
        });
        editStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fName.getText().toString().matches("") && !lName.getText().toString().matches("") && !email.getText().toString().matches("") && !phoneNumber.getText().toString().matches("") && !(picture.getDrawable() == null)) {
                    Matcher matcher = pattern.matcher(email.getText());
                    if (!matcher.matches()) {
                        Toast.makeText(getApplicationContext(), "Your email is not valid", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), list.update(new Student(fName.getText().toString(), lName.getText().toString(), email.getText().toString(), Integer.parseInt(phoneNumber.getText().toString()), bitmapToString())), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have an empty field", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String bitmapToString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

//    private void stringToBitmap()
//    {
//        byte[] b = Base64.decode(pictureString, Base64.DEFAULT);
//        imageBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//        picture.setImageBitmap(imageBitmap);
//    }
}
