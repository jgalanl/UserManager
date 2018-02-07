package com.example.jesusgalan.usermanager;


import android.provider.BaseColumns;

import java.util.Date;

public final class UsuariosContract {
    private UsuariosContract(){}

    public static class UsuariosEntry implements BaseColumns{
        public static final String TABLE_NAME = "USUARIO";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_FECHA = "fecha";
        public static final String COLUMN_NAME_GENERO = "genero";
        public static final String COLUMN_NAME_IMAGEN ="imagen";
        public static final String COLUMN_NAME_LOCALIZACION ="localizacion";
    }
}
