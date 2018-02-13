package com.example.jesusgalan.usermanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ObtenerImagen extends AsyncTask<String, Void, byte[]> {
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    @Override
    protected byte[] doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            inputStream.close();
        } catch (IOException e ){
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
}
