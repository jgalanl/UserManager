package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class ListarUsuarios extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION
            };
            Cursor users = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME,projection,null,null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
