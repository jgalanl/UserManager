package com.example.jesusgalan.usermanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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
                            String medium = picture.getString("medium");
                            ObtenerImagen obtenerImagen = new ObtenerImagen();
                            byte [] imagen = obtenerImagen.execute(medium).get();

                            JSONObject location = jsonObject.getJSONObject("location");
                            String street = location.getString("street");
                            String city = location.getString("city");
                            String state = location.getString("state");
                            String postcode = location.getString("postcode");
                            String localizacion = street.concat(",")
                                    .concat(city);

                            JSONObject login = jsonObject.getJSONObject("login");
                            String username = login.getString("username");
                            String password = login.getString("password");
                            //Comprobar fecha  de registro
                            Calendar fechaRegistro = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            fechaRegistro.setTime(format.parse(fecha));

                            if(fechaRegistro.after(fechaUsuario)){
                                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                                mDbHelper.insertar(nombreCompleto, format.format(fechaRegistro.getTime()), gender, imagen, localizacion, username, password);
                                cont++;
                            }
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.UsuariosInsertados, cont), Toast.LENGTH_LONG).show();
                        finish();
                    } catch (JSONException | ParseException | ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
