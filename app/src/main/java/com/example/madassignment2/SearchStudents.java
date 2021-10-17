package com.example.madassignment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madassignment2.AddPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SearchStudents extends AppCompatActivity implements SearchStudentsAdapter.ItemClickListener {

    private SearchStudentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_result);
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.viewPicturesRV);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new SearchStudentsAdapter(this, AddPhoto.getPictureList());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmapImage = adapter.getItem(position);
//        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String pictureString = Base64.encodeToString(b, Base64.DEFAULT);
//        //Pop intent
//        Intent i = new Intent(SearchStudents.this, AddPhoto.class);
//        i.putExtra("imageString", pictureString);
//        finish();
//        startActivity(i);
        try {
            String filename = "bitmap.png";
            FileOutputStream stream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            bitmapImage.recycle();

            Intent i = new Intent(SearchStudents.this, AddPhoto.class);
            i.putExtra("imageString", filename);
            finish();
            startActivity(i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}