package com.example.madassignment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class AddPhoto extends AppCompatActivity {
    private ImageView picture;
    private static ArrayList<Bitmap> pictureList;
    private Bitmap bitmapImage;
    private ArrayList<String> imageUrl = new ArrayList<>(50);
    private Bundle extras;
    private String data;
    private static final int REQUEST_THUMBNAIL = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        Button searchButton = (Button) findViewById(R.id.searchPictureButton);
        Button selectPhoto = (Button) findViewById(R.id.selectPictureBtn);
        Button takePhoto = (Button) findViewById(R.id.takePictureButton);
        EditText search = (EditText) findViewById(R.id.searchEditText);
        picture = (ImageView) findViewById(R.id.picureId2);

        if ((extras = getIntent().getExtras()) != null) {
            String pictureString = extras.getString("imageString");
//            byte[] b = Base64.decode(data, Base64.DEFAULT);
//            bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);
//            picture.setImageBitmap(bitmapImage);
            try {
                FileInputStream is = this.openFileInput(pictureString);
                bitmapImage = BitmapFactory.decodeStream(is);
                is.close();
                picture.setImageBitmap(bitmapImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchValue = search.getText().toString();
                new GetPhotoTask().execute(searchValue);
            }
        });
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    byte[] b = baos.toByteArray();
//                    String pictureString = Base64.encodeToString(b, Base64.DEFAULT);
                    String filename = "studentImage.png";
                    FileOutputStream stream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();
                    bitmapImage.recycle();

                    //Pop intent
                    Intent i = new Intent(AddPhoto.this, RegisterStudent.class);
                    i.putExtra("image", filename);
                    finish();
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photo, REQUEST_THUMBNAIL);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL) {
            Bitmap image = (Bitmap) resultIntent.getExtras().get("data");
            bitmapImage = image;
            picture.setImageBitmap(image);
        }
    }

    private String downloadToString(HttpURLConnection conn) {
        String data = null;
        try {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = IOUtils.toByteArray(inputStream);
            data = new String(byteData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void getImageFromUrl() {
        Bitmap image = null;
        for (int i = 0; i < imageUrl.size(); i++) {
            Uri.Builder url = Uri.parse(imageUrl.get(i)).buildUpon();
            String urlString = url.build().toString();
            HttpURLConnection connection = openConnection(urlString);
            image = downloadToBitmap(connection);
            pictureList.add(image);
            connection.disconnect();
        }
    }

    private void getImageLargeUrl(String data) {
        try {
            JSONObject jBase = new JSONObject(data);
            JSONArray jHits = jBase.getJSONArray("hits");
            for (int i = 0; i < 10; i++) {
                imageUrl.add(jHits.getJSONObject(i).getString("largeImageURL"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection openConnection(String urlString) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private Bitmap downloadToBitmap(HttpURLConnection conn) {
        Bitmap data = null;
        try {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = getByteArrayFromInputStream(inputStream);
            Log.d("Hello", String.valueOf(byteData.length));
            data = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    private class GetPhotoTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... searchKey) {
            String data = null;
            pictureList = new ArrayList<Bitmap>(50);
            Uri.Builder url = Uri.parse("https://pixabay.com/api/").buildUpon();
            url.appendQueryParameter("key", "23319229-94b52a4727158e1dc3fd5f2db");
            url.appendQueryParameter("q", searchKey[0]);
            url.appendQueryParameter("per_page", "10");
            String urlString = url.build().toString();
            Log.d("Hello", "pictureRetrievalTask: " + urlString);
            HttpURLConnection connection = openConnection(urlString);
            data = downloadToString(connection);
            getImageLargeUrl(data);
            getImageFromUrl();
            Intent i = new Intent(AddPhoto.this, SearchStudents.class);
            startActivity(i);
            connection.disconnect();
            return null;
        }
    }

    public static ArrayList<Bitmap> getPictureList() {
        return pictureList;
    }
}
