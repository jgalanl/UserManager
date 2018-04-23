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

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Random;

import javax.crypto.SecretKey;

import static com.example.jesusgalan.usermanager.UsuariosContract.UsuariosEntry.TABLE_NAME;

public class PantallaAutenticacion extends AppCompatActivity {

    Button sesion;
    EditText userText, passwordText;
    public static final String preferencias = "MisPreferencias";
    public static final String pref_nombre = "Nombre";
    public static final String pref_password = "Password";
    public static final String pref_clave = "Clave";
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
            String passwordUsuario = sharedPreferences.getString(pref_password, "");
            String clave = sharedPreferences.getString(pref_clave,"");
            String [] selectionArgs = {usuario, passwordUsuario};
            //Crear instancia de la bbdd y recuperar usuario
            //Generar la password
            SHA sha = new SHA();
            String password = sha.sha("admin");
            //Obtener la password de la bbdd con la clave y la password
            //String passwordbbdd = Crypto.decryptPbkdf2(clave, password);


              String  passwordbbdd = KeystoreProvider.decrypt("CN=Claves",password);



            UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
            SQLiteDatabase db = mDbHelper.getReadableDatabase(passwordbbdd);
            Cursor cursor = db.query(TABLE_NAME, projection, selection,
                    selectionArgs, null, null, null);
            //Comprobar resultado de la consulta
            if (cursor.getCount() == 1){
                cursor.close();
                db.close();
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
                String passwordUsuario;
                //Recoger datos de entrada
                userText = findViewById(R.id.usuario);
                passwordText = findViewById(R.id.password);
                usuario = userText.getText().toString();
                SHA sha = new SHA();
                passwordUsuario = sha.sha(passwordText.getText().toString());

                //Manejador de almacen claves KeyStore
                KeystoreProvider.loadKeyStore();
                //Generar par de claves

                try {
                    KeystoreProvider.generateNewKeyPair("Claves", getApplicationContext());
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                //Generar contraseña de la bbdd
                String password = sha.sha("admin");
                //Generar palabra aleatoria a partir de la password
                StringBuilder stringBuilder = new StringBuilder();
                Random random = new Random();
                random.setSeed(6);
                for(int i = 0; i < password.length(); i++){
                    stringBuilder.append(password.charAt(random.nextInt(password.length())));
                }
                String passwordbbdd = stringBuilder.toString();




                //Crear instancia de la bbdd
                SQLiteDatabase.loadLibs(getApplicationContext());
                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase(passwordbbdd);

                //Comprobar usuario
                String [] selectionArgs = {usuario, passwordUsuario};
                Cursor cursor = db.query(TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);
                //Comprobar resultado de la consulta
                if (cursor.getCount() == 1){
                    cursor.close();
                    db.close();
                    //Guardar preferencias, incluida la clave de la bbdd cifrada
                    //Cifrar palabra aleatoria con password
                    /*//Obtener Salt
                    byte salt [] = Crypto.generateSalt();
                    //Obtener clave cifrado PBE
                    SecretKey secretKey = Crypto.deriveKeyPbkdf2(salt, password);
                    //Obtener palabra aleatoria cifrada
                    String passwordcip = Crypto.encrypt(passwordbbdd, secretKey, salt);*/

                    String passwordcip = KeystoreProvider.encrypt("CN=Claves", passwordbbdd);

                    //Guardar passwordcip en shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(pref_nombre, usuario);
                    editor.putString(pref_password, password);
                    editor.putString(pref_clave, passwordcip);
                    editor.apply();
                    Intent pantalla_principal = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                    startActivity(pantalla_principal);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "El usuario o la contraseña son incorrectos", Toast.LENGTH_LONG).show();
                    cursor.close();
                    db.close();
                }
            }
        });
    }
}
