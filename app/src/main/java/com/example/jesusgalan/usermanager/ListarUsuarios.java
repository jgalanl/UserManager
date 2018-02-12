package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ListarUsuarios extends Activity {

    Button listar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
        try {
            TableLayout Tabla= (TableLayout)findViewById(R.id.tabla);
            TableRow fila = new TableRow(this);
            TextView nombre =new TextView(this);
            nombre.setText("Nombre");
            fila.addView(nombre);
            TextView genero =new TextView(this);
            genero.setText("Genero");
            fila.addView(genero);
            TextView fecha =new TextView(this);
            fecha.setText("Fecha");
            fila.addView(fecha);
            TextView imagen =new TextView(this);
            imagen.setText("Imagen");
            fila.addView(imagen);
            TextView localizacion =new TextView(this);
            localizacion.setText("Localizacion");
            fila.addView(localizacion);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION
            };
            Cursor users = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME,projection,null,null,null,null,null);
            if (users.moveToFirst()) {
                String itemId = users.getString(
                        users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE)
                );
                Log.d("User", itemId);
            }
            Log.v("Users", "Entro en listar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
