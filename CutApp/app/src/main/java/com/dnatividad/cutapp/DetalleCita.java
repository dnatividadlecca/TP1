package com.dnatividad.cutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleCita extends AppCompatActivity {
TextView txt_idCita, txt_descripcionServicio, txt_nombreServicio, txt_costoServicio, txt_estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);
        setReferences();
    }

    private void setReferences() {
        txt_idCita = (TextView)findViewById(R.id.txt_idCita);
        txt_descripcionServicio = (TextView)findViewById(R.id.txt_descripcionServicio);
        txt_nombreServicio = (TextView)findViewById(R.id.txt_nombreServicio);
        txt_costoServicio = (TextView)findViewById(R.id.txt_costoServicio);
        txt_estado = (TextView)findViewById(R.id.txt_estado);

        String datos_ididCita = getIntent().getStringExtra("idCita");
        String datos_nombreServicio = getIntent().getStringExtra("nombreServicio");
        String datos_descripcionServicio = getIntent().getStringExtra("descripcionServicio");
        String datos_costoServicio = getIntent().getStringExtra("costoServicio");
        String datos_estadoServicio = getIntent().getStringExtra("estadoServicio");

        txt_estado.setText(datos_estadoServicio);
        txt_idCita.setText(datos_ididCita);
        txt_nombreServicio.setText(datos_nombreServicio);
        txt_descripcionServicio.setText(datos_descripcionServicio);
        txt_costoServicio.setText(datos_costoServicio);
    }
    //region Navegacion
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String permiso = prefs.getString("PERMISO", "");
        Log.i("permiso logueado ====>", permiso.trim());
        //------------------------------------------------------------------------------------------

        //otorgo permiso de acceso a las opciones del menu
        if(permiso.equals("true")){
            //esta linea permite hacer visible un item del menu
            MenuItem item = menu.findItem(R.id.item_6);
            item.setVisible(true);
            MenuItem item2 = menu.findItem(R.id.item_7);
            item2.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.item_7);
            item.setVisible(false);
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_11);
        itemMenuRegistrar.setVisible(true);
        //------------------------------------------------------------------------------------------
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_1){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_2){
            Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_3){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_4){
            Toast.makeText(this,"Contactenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_5){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_6){
            Toast.makeText(this,"Catalogo", Toast.LENGTH_SHORT).show();
            Catalogo();
        }
        else if (id ==R.id.item_7){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            MisPedidos();
        }
        else if (id ==R.id.item_8){
            Toast.makeText(this,"Reg. Producto", Toast.LENGTH_SHORT).show();
            reg_producto();
        }
        else if (id ==R.id.item_9){
            Toast.makeText(this,"Mis Productos", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        else if (id ==R.id.item_11){
            //Toast.makeText(this,"Cerrar Sesión", Toast.LENGTH_SHORT).show();
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Login(){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void RegistrarUsuario(){
        Intent registrarusuario = new Intent(this, RegistrarUsuarioActivity.class);
        startActivity(registrarusuario);
    }

    public void Nosotros(){
        Intent nosotros = new Intent(this, NosotrosActivity.class);
        startActivity(nosotros);
    }

    public void Contactenos(){
        Intent contactenos = new Intent(this, ContactenosActivity.class);
        startActivity(contactenos);
    }

    public void Ubicanos(){
        Intent ubicanos = new Intent(this, UbicanosActivity.class);
        startActivity(ubicanos);
    }

    public void Catalogo(){
        Intent Catalogo = new Intent(this, CatalogoActivity.class);
        startActivity(Catalogo);
    }

    public void MisPedidos(){
        Intent mispedidos = new Intent(this, MisPedidosActivity.class);
        startActivity(mispedidos);
    }

    public void reg_producto(){
        Intent producto = new Intent(this, RegistrarServicioActivity.class);
        startActivity(producto);
    }

    public void MisProductos(){
        Intent misproducto = new Intent(this, MisServiciosActivity.class);
        startActivity(misproducto);
    }

    private void cerrarSesion(){
        DialogInterface.OnClickListener confirmacion = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Limpia Preferencias
                        SharedPreferences preferences = getSharedPreferences("PREFERENCIAS",MODE_PRIVATE);
                        preferences.edit().clear().commit();

                        //Regresa Pantalla Login
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lbl_confirmacion_cerrar_sesion).setPositiveButton(R.string.lbl_confirmacion_si, confirmacion)
                .setNegativeButton(R.string.lbl_confirmacion_no, confirmacion).show();
    }
    //endregion
}
