package com.example.jesusgalan.usermanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PantallaNuevosUsuarios extends AppCompatActivity {

    Button insertar;
    Spinner nacionalidad;
    CheckBox male, female;
    EditText num;
    DatePicker date;
    //https://randomuser.me/api/?inc=nat,registered,gender,picture,location,&nat=gb,&gender=female,&results=50

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nuevos_usuarios);

        insertar = findViewById(R.id.boton_insertar);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reocoger datos introducidos por el usuario
                nacionalidad = findViewById(R.id.spinner_nacionalidad);
                String nat = nacionalidad.getSelectedItem().toString();
                String genero;
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
                date = findViewById(R.id.datePicker);
                int   day  = date.getDayOfMonth();
                int   month= date.getMonth();
                int   year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY");


            }
        });
    }


}
