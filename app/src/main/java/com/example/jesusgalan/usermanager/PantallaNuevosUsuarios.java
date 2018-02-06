package com.example.jesusgalan.usermanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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
                Log.d("holi", "numero"+numero);
                //Fecha
                date = findViewById(R.id.datePicker);
                int day  = date.getDayOfMonth();
                int month= date.getMonth();
                int year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                //SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY");

                //Obtener respuesta del servidor
                ObtenerJSON hilo = new ObtenerJSON();
                try {
                    cadena_json = hilo.execute(nacionalidad, genero, numero).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("holi", "nuevos usuarios"+cadena_json);

                //Insertar respuesta en la bbdd


            }
        });
    }


}
