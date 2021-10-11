package com.example.madassignment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class AddPhoto extends AppCompatActivity {
    private ImageView picture;
    private Bitmap bitmapImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        Button searchButton = (Button) findViewById(R.id.searchPictureButton);
        Button selectPhoto = (Button) findViewById(R.id.selectPictureBtn);
        EditText search = (EditText) findViewById(R.id.searchEditText);
        picture = (ImageView) findViewById(R.id.picureId2);
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
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = getApplicationContext().openFileOutput(filename,Context.MODE_PRIVATE);
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
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

    private Bitmap getImageFromUrl(String imageUrl) {

        Bitmap image = null;

        Uri.Builder url = Uri.parse(imageUrl).buildUpon();
        String urlString = url.build().toString();
        Log.d("Hello", "ImageUrl: " + urlString);

        HttpURLConnection connection = openConnection(urlString);

        image = downloadToBitmap(connection);
        if (image != null) {
            // Log.d("Hello", image.toString());
        } else {
            Log.d("Hello", "Nothing returned");
        }
        connection.disconnect();


        return image;
    }

    private String getImageLargeUrl(String data) {
        String imageUrl = null;
        try {
            JSONObject jBase = new JSONObject(data);
            JSONArray jHits = jBase.getJSONArray("hits");
            if (jHits.length() > 0) {
                JSONObject jHitsItem = jHits.getJSONObject(0);
                imageUrl = jHitsItem.getString("largeImageURL");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;
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


    private class GetPhotoTask extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... searchKey)
        {
            String data = null;
            Uri.Builder url = Uri.parse("https://pixabay.com/api/").buildUpon();
            url.appendQueryParameter("key", "23319229-94b52a4727158e1dc3fd5f2db");
            url.appendQueryParameter("q", searchKey[0]);
            String urlString = url.build().toString();
            Log.d("Hello", "pictureRetrievalTask: " + urlString);

            HttpURLConnection connection = openConnection(urlString);
            data = downloadToString(connection);
            String imageURL = getImageLargeUrl(data);
            bitmapImage = getImageFromUrl(imageURL);
            if (data != null) {
                Log.d("Hello", data);
            } else {
                Log.d("Hello", "Nothing returned");
            }
            connection.disconnect();
            return bitmapImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            picture.setImageBitmap(bitmap);
        }
    }
}
