package com.example.jesusgalan.usermanager;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;

public class ListarUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        UsuariosDbHelper mDbHelper = new UsuariosDbHelper(getApplicationContext());
        try {
            TableLayout tabla = findViewById(R.id.tabla2);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO,
                    UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD
            };
            AtomicReference<Cursor> users = new AtomicReference<>(db.query(UsuariosContract.UsuariosEntry.TABLE_NAME, projection, null,
                    null, null, null, null));

            while(users.get().moveToNext()) {
                TableRow fila = new TableRow(this);
                ImageView imagen = new ImageView(this);
                imagen.setPadding(5,5,5,5);

                byte[] b = users.get().getBlob(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_IMAGEN));
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0 , b.length);
                imagen.setImageBitmap(bmp);
                fila.addView(imagen,130,170);

                RelativeLayout columna = new RelativeLayout(this);
                TextView nombre = new TextView(this);
                nombre.setTextSize(14);
                nombre.setPadding(0,0,0,40);
                nombre.setText(users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_NOMBRE)));
                columna.addView(nombre);
                TextView fecha = new TextView(this);
                fecha.setTextSize(14);
                fecha.setPadding(0,90,0,20);
                fecha.setText((users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_FECHA))));
                columna.addView(fecha);
                fila.addView(columna);

                TextView genero = new TextView(this);
                genero.setTextSize(14);
                genero.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                genero.setGravity(Gravity.CENTER);
                genero.setText(users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_GENERO)));
                fila.addView(genero);

                Button localizacion = new Button(this);
                localizacion.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal);
                localizacion.setGravity(Gravity.CENTER);
                final String loc = users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_LOCALIZACION));
                localizacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("geo:0,0?q="+Uri.encode(loc));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });

                fila.addView(localizacion,50,80);

                TextView user = new TextView(this);
                user.setTextSize(14);
                user.setPadding(0,0,30,0);
                user.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                user.setText(users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_USUARIO)));
                user.setVisibility(View.GONE);
                fila.addView(user);
                TextView password = new TextView(this);
                password.setTextSize(14);
                password.setPadding(0,0,30,0);
                password.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                password.setText(users.get().getString(users.get().getColumnIndexOrThrow(UsuariosContract.UsuariosEntry.COLUMN_NAME_PASSWORD)));
                password.setVisibility(View.GONE);
                fila.addView(password);

                tabla.addView(fila);
            }
            users.get().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TableLayout tabla =findViewById(R.id.tabla2);
        TableLayout tabla_head =findViewById(R.id.tabla3);
        TextView localizacion = findViewById(R.id.localizacion);
        TextView user = findViewById(R.id.user);
        TextView password = findViewById(R.id.password);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            localizacion.setVisibility(View.GONE);
            tabla.setColumnCollapsed(3, true);
            tabla_head.setColumnCollapsed(3, true);
            user.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            tabla.setColumnCollapsed(4, false);
            tabla.setColumnCollapsed(5, false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            user.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            tabla.setColumnCollapsed(4, true);
            tabla.setColumnCollapsed(5, true);
            localizacion.setVisibility(View.VISIBLE);
            tabla.setColumnCollapsed(3, false);
            tabla_head.setColumnCollapsed(3, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navegacion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent nuevos_usuarios = new Intent("com.example.jesusgalan.usermanager.PantallaPrincipal");
                startActivity(nuevos_usuarios);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
