package com.example.jesusgalan.usermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class PantallaPresentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_presentacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Thread reloj=new Thread(){
          public void run(){
              try{
                  sleep(3000);
              }
              catch(InterruptedException e){
                  e.printStackTrace();
              }
              finally{
                  Intent principal = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                  startActivity(principal);
                  finish();
              }
          }
        };
        reloj.start();
    }
}
