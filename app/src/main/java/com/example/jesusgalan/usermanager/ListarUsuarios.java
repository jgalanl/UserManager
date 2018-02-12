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
            TableLayout Tabla= (TableLayout)findViewById(R.id.tabla);
//            TableRow fila = new TableRow(this);
//            TextView nombre =new TextView(this);
//            nombre.setText("Nombre");
//            fila.addView(nombre);
//            TextView genero =new TextView(this);
//            nombre.setText("Genero");
//            fila.addView(genero);
//            TextView fecha =new TextView(this);
//            nombre.setText("Fecha");
//            fila.addView(fecha);
//            TextView imagen =new TextView(this);
//            nombre.setText("Imagen");
//            fila.addView(imagen);
//            TextView Localizacion =new TextView(this);
//            nombre.setText("Localizacion");
//            fila.addView(Localizacion);
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
            if (users.moveToFirst()) {
                String itemId = users.getString(
                        users.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE)
                );
                Log.d("holi", "item: "+itemId);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
