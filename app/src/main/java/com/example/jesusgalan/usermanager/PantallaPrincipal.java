package com.example.jesusgalan.usermanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaPrincipal extends AppCompatActivity {

    Button insertar, listar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        insertar = findViewById(R.id.boton_pantalla_insertar);
        listar = findViewById(R.id.boton_pantalla_listar);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevos_usuarios = new Intent("com.example.jesusgalan.usermanager.PantallaNuevosUsuarios");
                startActivity(nuevos_usuarios);
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cargar la actividad listar
            }
        });


    }
}
