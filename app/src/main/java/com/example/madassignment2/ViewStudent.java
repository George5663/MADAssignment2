package com.example.madassignment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madassignment2.Database.Student;
import com.example.madassignment2.Database.StudentList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewStudent extends AppCompatActivity implements ViewStudentsAdapter.ItemClickListener {
    private StudentList studentList;
    private ViewStudentsAdapter adapter;
    private LinearLayoutManager llm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_students);
        studentList = new StudentList();
        studentList.load(getApplicationContext());

        RecyclerView rv = findViewById(R.id.viewStudentsRV);
        rv.setLayoutManager(llm = new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), llm.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        adapter = new ViewStudentsAdapter(this, studentList.getStudents());
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
    }

    public ViewStudentsAdapter.ItemClickListener getClickListener() {
        return this;
    }

    @Override
    public void onItemClickStudent(View view, int position) {
        //Open up add/edit/delete student with selected details
        Student tempStudent = adapter.getItem(position);
        Intent i = new Intent(ViewStudent.this, RegisterStudent.class);
        Bundle bundle = new Bundle();
        bundle.putString("firstName", tempStudent.getFirstName());
        bundle.putString("lastName", tempStudent.getLastName());
        bundle.putString("email", tempStudent.getEmail());
        bundle.putInt("phoneNumber", tempStudent.getPhoneNumber());

        String pictureString = tempStudent.getStudentPicture();
        Bitmap tempImage;
        byte[] b = Base64.decode(pictureString, Base64.DEFAULT);
        tempImage = BitmapFactory.decodeByteArray(b, 0, b.length);
        try {

            String filename = "studentImage.png";
            FileOutputStream stream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            tempImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            tempImage.recycle();
            bundle.putString("filename", filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        i.putExtras(bundle);
        startActivity(i);
        //Toast.makeText(this, "You Clicked " + adapter.getItem(position).getTitle(), Toast.LENGTH_LONG).show();
    }
}
