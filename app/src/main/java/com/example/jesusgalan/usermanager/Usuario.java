package com.example.jesusgalan.usermanager;


import java.util.Date;

public class Usuario {
    //Atributos
    private String nombre;
    private Date registro;
    private char genero;
    //private imagen;
    //private localizacion;

    //Constructor
    public Usuario(String nombre, Date registro, char genero){
        this.nombre = nombre;
        this.registro = registro;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }
}
