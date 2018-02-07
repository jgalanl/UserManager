package com.example.jesusgalan.usermanager;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ObtenerJSON extends AsyncTask<String, Void, String> {
    private StringBuilder data = new StringBuilder();
    @Override
    protected String doInBackground(String... strings) {
        try {
            //Construir la url
            Uri.Builder builder = Uri.parse("https://randomuser.me/api/?&inc=name,registered,gender,picture,location&noinfo").buildUpon();
            if(! strings[0].equalsIgnoreCase("Por defecto")){
                builder = builder.appendQueryParameter("nat", strings[0]);
            }
            if (! strings[1].equalsIgnoreCase("Por defecto")){
                builder = builder.appendQueryParameter("gender", strings[1]);
            }
            if(! strings[2].equalsIgnoreCase("")){
                builder = builder.appendQueryParameter("results", strings[2]);
            }
            Log.d("holi", "url"+builder.build().toString());

            URL url = new URL(builder.build().toString());
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null){
                data.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("holi", "json"+data);
        return data.toString();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
    }
}
