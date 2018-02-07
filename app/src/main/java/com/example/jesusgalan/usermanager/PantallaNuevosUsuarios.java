package com.example.jesusgalan.usermanager;

import android.app.Activity;
import android.os.Bundle;
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

public class PantallaNuevosUsuarios extends Activity {

    Button insertar;
    Spinner nat;
    CheckBox male, female;
    EditText num;
    DatePicker date;
    String cadena_json;
    //https://randomuser.me/api/?inc=nat,registered,gender,picture,location,&nat=gb,&gender=female,&results=50

   // https://randomuser.me/api/?inc=gender,registered&gender=male&registered=2013-08-26%2004:54:46&noinfo

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
                female = findViewById(R.id.box_female);
                if (male.isChecked()){
                    genero = "male";
                }
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
                        Toast.makeText(getApplicationContext(), R.string.DescargandoUsuarios, Toast.LENGTH_LONG).show();
                        cadena_json = hilo.execute(nacionalidad, genero, numero).get();
                        Toast.makeText(getApplicationContext(), getString(R.string.UsuariosDescargados, Integer.parseInt(numero)), Toast.LENGTH_LONG).show();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                    int insertados = 0;
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
                            String imagen = jsonObject.getString("picture");
                            String localizacion = jsonObject.getString("location");
                            JSONObject login = jsonObject.getJSONObject("login");
                            String username = login.getString("username");
                            String password = login.getString("password");

                            //Comprobar fecha  de registro
                            Calendar fechaRegistro = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            fechaRegistro.setTime(format.parse(fecha));
                            if(fechaRegistro.after(fechaUsuario)){
                                mDbHelper.insertar(nombreCompleto, fecha, gender, imagen, localizacion, username, password);
                                insertados++;
                            }
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.UsuariosInsertados, insertados), Toast.LENGTH_LONG).show();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
