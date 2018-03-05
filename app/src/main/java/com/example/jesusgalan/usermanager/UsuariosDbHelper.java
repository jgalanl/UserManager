package com.example.jesusgalan.usermanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.ExecutionException;

import static com.example.jesusgalan.usermanager.UsuariosContract.UsuariosEntry.TABLE_NAME;

public class UsuariosDbHelper extends SQLiteOpenHelper{
    //Informacion de la bbdd
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usuarios.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    UsuariosContract.UsuariosEntry._ID + "INTEGER PRIMARY KEY," +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA + INTEGER_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO + TEXT_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN + BLOB_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION + TEXT_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO + TEXT_TYPE + COMMA_SEP +
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    UsuariosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        ObtenerImagen obtenerImagen = new ObtenerImagen();
        byte [] imagen = new byte[0];
        try {
            imagen = obtenerImagen.execute("https://randomuser.me/api/portraits/men/0.jpg").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE, "Administrador");
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA, "2018-01-01");
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO, "M");
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN, imagen);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION, "Leganés");
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO, "admin");
        SHA sha = new SHA();
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD, sha.sha("admin"));
        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    void insertar(String nombreCompleto, String fecha, String genero, byte [] imagen, String localizacion, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        //Crear un mapa de valores
        ContentValues values = new ContentValues();
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE, nombreCompleto);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA, fecha);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO, genero);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN, imagen);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION, localizacion);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO, username);
        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD, password);
        //Insertar la informacion en la bbdd
        db.insert(TABLE_NAME, null, values);
    }
}
