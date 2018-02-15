package com.example.jesusgalan.usermanager;


import android.provider.BaseColumns;


final class UsuariosContract {
    private UsuariosContract(){}
    static class UsuariosEntry implements BaseColumns{
        static final String TABLE_NAME = "USUARIO";
        static final String COLUMN_NAME_NOMBRE = "nombre";
        static final String COLUMN_NAME_FECHA = "fecha";
        static final String COLUMN_NAME_GENERO = "genero";
        static final String COLUMN_NAME_IMAGEN ="imagen";
        static final String COLUMN_NAME_LOCALIZACION ="localizacion";
        static final String COLUMN_NAME_USUARIO="usuario";
        static final String COLUMN_NAME_PASSWORD="password";
    }
}
