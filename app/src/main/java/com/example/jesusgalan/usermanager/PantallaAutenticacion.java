package com.example.jesusgalan.usermanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PantallaAutenticacion extends AppCompatActivity {

    Button sesion;
    EditText userText, passwordText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_autenticacion);

        sesion = findViewById(R.id.boton_sesion);
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recoger datos de entrada
                userText = findViewById(R.id.usuario);
                passwordText = findViewById(R.id.password);
                String usuario = userText.getText().toString();
                MD5 md5 = new MD5();
                String password = md5.md5(passwordText.getText().toString());

                Log.d("holi", usuario);
                Log.d("holi", password);

                //Preparar parametros de la consulta
                String [] projection = {
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO,
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD
                };
                String selection = UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO + " = ? AND " +
                        UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD + " = ?";
                String [] selectionArgs = {usuario, password};
                //Crear instancia de la bbdd y recuperar usuario
                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor cursor = db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);
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
