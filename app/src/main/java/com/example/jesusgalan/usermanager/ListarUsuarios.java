package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD

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
                final String loc = users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION));
                localizacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("geo:0,0?q="+Uri.encode(loc));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                fila.addView(localizacion);
                TextView user = new TextView(this);
                user.setText(users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO)));
                fila.addView(user);
                TextView password = new TextView(this);
                password.setText(users.getString(users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD)));
                fila.addView(password);
                tabla.addView(fila);
            }
            users.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("holi", ""+newConfig.orientation);
        super.onConfigurationChanged(newConfig);
        Log.d("holi", ""+newConfig.orientation);
        TableLayout tabla = findViewById(R.id.tabla);
        TextView imagen = findViewById(R.id.imagen);
        TextView localizacion = findViewById(R.id.localizacion);
        TextView user = findViewById(R.id.user);
        TextView password = findViewById(R.id.password);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imagen.setVisibility(View.GONE);
            localizacion.setVisibility(View.GONE);
            tabla.setColumnCollapsed(3, true);
            tabla.setColumnCollapsed(4, true);
            user.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            tabla.setColumnCollapsed(5, false);
            tabla.setColumnCollapsed(6, false);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            user.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            tabla.setColumnCollapsed(5, true);
            tabla.setColumnCollapsed(6, true);
            imagen.setVisibility(View.VISIBLE);
            localizacion.setVisibility(View.VISIBLE);
            tabla.setColumnCollapsed(3, false);
            tabla.setColumnCollapsed(4, false);
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
