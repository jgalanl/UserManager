package com.example.jesusgalan.usermanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.jesusgalan.usermanager.PantallaAutenticacion.pref_clave;
import static com.example.jesusgalan.usermanager.PantallaAutenticacion.preferencias;
import static com.example.jesusgalan.usermanager.UsuariosContract.UsuariosEntry.TABLE_NAME;

public class PantallaNuevosUsuarios extends AppCompatActivity {

    Button insertar;
    Spinner nat;
    CheckBox male, female;
    EditText num;
    DatePicker date;
    String cadena_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nuevos_usuarios);
        SQLiteDatabase.loadLibs(this);

        insertar = findViewById(R.id.boton_insertar);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reocoger datos introducidos por el usuario
                nat = findViewById(R.id.spinner_nacionalidad);
                String nacionalidad = nat.getSelectedItem().toString();
                String genero="Por defecto";
                male = findViewById(R.id.box_male);
                if (male.isChecked()){
                    genero = "male";
                }
                female = findViewById(R.id.box_female);
                if(female.isChecked()){
                    genero = "female";
                }
                num = findViewById(R.id.editText);
                String numero = num.getText().toString();
                //Ocultar teclado
                num.setInputType(InputType.TYPE_NULL);
                //Comprobar el numero de usuarios maximo a insertar
                if(Integer.parseInt(numero) > 5000){
                    Toast.makeText(getApplicationContext(), R.string.ErrorInsertar, Toast.LENGTH_LONG).show();
                }
                else {
                    //Fecha
                    date = findViewById(R.id.datePicker);
                    int day = date.getDayOfMonth();
                    int month = date.getMonth();
                    int year = date.getYear();
                    Calendar fechaUsuario = Calendar.getInstance();
                    fechaUsuario.set(year, month, day);
                    //Obtener json con datos de los usuarios almacenados en el servidor
                    ObtenerJSON hilo = new ObtenerJSON();
                    try {
                        cadena_json = hilo.execute(nacionalidad, genero, numero).get();
                        Toast.makeText(getApplicationContext(), getString(R.string.UsuariosDescargados, Integer.parseInt(numero)), Toast.LENGTH_LONG).show();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    int cont = 0;
                    //Obtener la palabra aleatoria cifrada
                    SharedPreferences sharedPreferences = getSharedPreferences(preferencias, Context.MODE_PRIVATE);
                    String passwordcip = sharedPreferences.getString(pref_clave, "");
                    //Generar la password
                    SHA sha = new SHA();
                    String password = sha.sha("admin");
                    //Obtener la password de la bbdd con la clave y la password

                    String clave =KeystoreProvider.decrypt("Claves", passwordcip);

                    //Acceder la bbdd
                    SQLiteDatabase.loadLibs(getApplicationContext());
                    UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                    SQLiteDatabase db = mDbHelper.getReadableDatabase(clave);
                    //Parsear datos json e insertar en la bbdd
                    try {
                        JSONObject parser = new JSONObject(cadena_json);
                        JSONArray jsonArray = parser.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            JSONObject name = jsonObject.getJSONObject("name");
                            String nombreCompleto = name.getString("title").concat(" ")
                                    .concat(name.getString("first")).concat(" ")
                                    .concat(name.getString("last"));
                            String fecha = jsonObject.getString("registered");
                            String gender = jsonObject.getString("gender");
                            if(gender.equalsIgnoreCase("male")){
                                gender = "M";
                            }
                            else {
                                gender = "F";
                            }
                            JSONObject picture = jsonObject.getJSONObject("picture");
                            String large = picture.getString("large");
                            ObtenerImagen obtenerImagen = new ObtenerImagen();
                            byte [] imagen = obtenerImagen.execute(large).get();
                            JSONObject location = jsonObject.getJSONObject("location");
                            String street = location.getString("street");
                            String city = location.getString("city");
                            String state = location.getString("state");
                            String localizacion = street.concat(",")
                                    .concat(city).concat(",")
                                    .concat(state);
                            JSONObject login = jsonObject.getJSONObject("login");
                            String username = login.getString("username");
                            SHA SHA = new SHA();
                            String passwordUser = SHA.sha(login.getString("password"));
                            //Comprobar fecha  de registro
                            Calendar fechaRegistro = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            fechaRegistro.setTime(format.parse(fecha));
                            if(fechaRegistro.after(fechaUsuario)){
                                ContentValues values = new ContentValues();
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE, nombreCompleto);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA, fecha);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO, gender);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN, imagen);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION, localizacion);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO, username);
                                values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD, passwordUser);
                                //Insertar la informacion en la bbdd
                                db.insert(TABLE_NAME, null, values);
                                cont++;
                            }
                        }
                        db.close();
                        Toast.makeText(getApplicationContext(), getString(R.string.UsuariosInsertados, cont), Toast.LENGTH_LONG).show();
                        finish();
                    } catch (JSONException | ParseException | ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navegacion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent nuevos_usuarios = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                startActivity(nuevos_usuarios);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
