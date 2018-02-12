package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


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
                //Localizacion
                tabla.addView(fila);
            }

            users.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
