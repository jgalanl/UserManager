package com.example.jesusgalan.usermanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.Cursor;

public class PantallaAutenticacion extends AppCompatActivity {

    Button sesion;
    EditText userText, passwordText;
    public static final String preferencias = "MisPreferencias";
    public static final String pref_nombre = "Nombre";
    public static final String pref_password = "Password";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_autenticacion);
        SQLiteDatabase.loadLibs(this);

        //Preparar parametros de la consulta
        final String [] projection = {
                UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO,
                UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD
        };
        final String selection = UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO + " = ? AND " +
                UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD + " = ?";

        //Comprobar preferencias
        sharedPreferences = getSharedPreferences(preferencias, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(pref_nombre) && sharedPreferences.contains(pref_password)){
            String usuario = sharedPreferences.getString(pref_nombre, "");
            String password = sharedPreferences.getString(pref_password, "");
            String [] selectionArgs = {usuario, password};
            //Crear instancia de la bbdd y recuperar usuario
            UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase("a");
            Cursor cursor = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, selection,
                    selectionArgs, null, null, null);
            //Comprobar resultado de la consulta
            if (cursor.getCount() == 1){
                cursor.close();
                Intent pantalla_principal = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                startActivity(pantalla_principal);
                finish();
            }
        }

        sesion = findViewById(R.id.boton_sesion);
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario;
                String password;

                //Recoger datos de entrada
                userText = findViewById(R.id.usuario);
                passwordText = findViewById(R.id.password);
                usuario = userText.getText().toString();
                SHA SHA = new SHA();
                password = SHA.sha(passwordText.getText().toString());
                String [] selectionArgs = {usuario, password};
                //Crear instancia de la bbdd y recuperar usuario
                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase("a");
                Cursor cursor = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);
                //Comprobar resultado de la consulta
                if (cursor.getCount() == 1){
                    cursor.close();
                    //Guardar preferencias
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(pref_nombre, usuario);
                    editor.putString(pref_password, password);
                    editor.apply();
                    Intent pantalla_principal = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                    startActivity(pantalla_principal);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "El usuario o la contraseña son incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
