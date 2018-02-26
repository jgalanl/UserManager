package com.example.jesusgalan.usermanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class PantallaAutenticacion extends AppCompatActivity {

    Button sesion;
    EditText userText, passwordText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_autenticacion);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte [] imagen = byteArrayOutputStream.toByteArray();
        UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
        mDbHelper.insertar("Administrador", "2018/01/01", "A", imagen,"Leganés", "admin", "admin");

        sesion = findViewById(R.id.boton_sesion);
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recoger datos de entrada
                userText = findViewById(R.id.usuario);
                passwordText = findViewById(R.id.password);
                String usuario = userText.getText().toString();
                String password = passwordText.getText().toString();

                Log.d("holi", usuario);
                Log.d("holi", password);

                //Crear instancia de la bbdd y recuperar usuario
                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();


                String [] projection = {
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO,
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD
                };

                //Cursor prueba = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, null, null, null, null, null);



                //Log.d("holi", prueba.getString(prueba.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO)));
                //Log.d("holi", prueba.getString(prueba.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD)));



                String selection = UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO + " = ? AND " +
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD + " = ?";
                String [] selectionArgs = {usuario, password};

                Cursor cursor = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);

                //Log.d("holi", "Cursor: "+ cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO)));
                //Log.d("holi", "Cursor: "+ cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD)));

                //Comprobar resultado de la consulta
                if (cursor.getCount() == 1){
                    Intent pantalla_principal = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                    startActivity(pantalla_principal);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "El usuario o la contraseña son incorrectos", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        });


    }
}
