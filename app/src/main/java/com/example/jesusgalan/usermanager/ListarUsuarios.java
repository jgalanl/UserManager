package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


public class ListarUsuarios extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        Log.d("holi", "listar usuarios");

        UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
        try {
            TableLayout tabla = findViewById(R.id.tabla);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION
            };
            Cursor users = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME,projection,null,
                    null,null,null,null);

            Log.d("holi", "cursor creado");
            while(users.moveToNext()) {
                TableRow fila = new TableRow(this);
                TextView nombre = new TextView(this);
                nombre.setText(users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE)));
                fila.addView(nombre);
                TextView fecha = new TextView(this);
                fecha.setText((users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA))));
                fila.addView(fecha);
                TextView genero = new TextView(this);
                genero.setText(users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO)));
                fila.addView(genero);
                //Imagen
                ImageView imagen = new ImageView(this);
                byte[] b = users.getBlob(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN));
                Bitmap bmp= BitmapFactory.decodeByteArray(b, 0 , b.length);
                imagen.setImageBitmap(bmp);
                fila.addView(imagen);
                //Localizacion
                Button localizacion = new Button(this);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list;
                Log.d("holiiii", users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION)));
                list = geocoder.getFromLocationName(users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION)),1);

                Log.d("holi", " "+list.size());

                double latitude= list.get(0).getLatitude();
                double longitude= list.get(0).getLongitude();
                Log.d("holi", "latitud "+latitude);
                Log.d("holi", "longitude "+longitude);


                fila.addView(localizacion);
                tabla.addView(fila);
            }

            users.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
