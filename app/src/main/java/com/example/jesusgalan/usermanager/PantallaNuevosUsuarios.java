package com.example.jesusgalan.usermanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class PantallaNuevosUsuarios extends AppCompatActivity {

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
                //Nacionalidad
                nat = findViewById(R.id.spinner_nacionalidad);
                String nacionalidad = nat.getSelectedItem().toString();
                //Genero
                String genero="Por defecto";
                male = findViewById(R.id.box_male);
                female = findViewById(R.id.box_female);
                if (male.isChecked()){
                    genero = "male";
                }
                if(female.isChecked()){
                    genero = "female";
                }
                //Numero de usuarios a insertar
                num = findViewById(R.id.editText);
                String numero = num.getText().toString();
                //Fecha
                date = findViewById(R.id.datePicker);
                int day  = date.getDayOfMonth();
                int month= date.getMonth();
                int year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                //SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY");

                //Obtener datos de los usuarios almacenados en el servidos
                ObtenerJSON hilo = new ObtenerJSON();
                try {
                    cadena_json = hilo.execute(nacionalidad, genero, numero).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("holi", "nuevos usuarios"+cadena_json);
                //Parsear datos json
                //BBDD
                UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                try {
                    JSONObject parser = new JSONObject(cadena_json);
                    JSONArray jsonArray = parser.getJSONArray("results");
                    for(int i = 0; i < jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        JSONObject name = jsonObject.getJSONObject("name");
                        String nombreCompleto = name.getString("title").concat(" ")
                                .concat(name.getString("first")).concat(" ")
                                .concat(name.getString("last"));
                        Log.d("nombre", nombreCompleto);

                        String fecha = jsonObject.getString("registered");
                        Log.d("fecha", fecha);
                        String gender = jsonObject.getString("gender");
                        Log.d("gender", gender);
                        String imagen = jsonObject.getString("picture");
                        String localizacion = jsonObject.getString("location");

                        //Crear un mapa de valores
                        ContentValues values = new ContentValues();
                        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE, nombreCompleto);
                        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA, fecha);
                        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO, gender);
                        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN, imagen);
                        values.put(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION, localizacion);
                        //Insertar la informacion en la bbdd
                        long newRow = db.insert(UsuariosContract.UsuariosEntry.TABLE_NAME, null, values);
                        Log.d("holi", "se ha insertado"+newRow);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
