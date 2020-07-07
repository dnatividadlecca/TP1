package com.dnatividad.cutapp.App.Calificaciones;

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
import android.widget.Toast;

import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Admin_NosotrosEdicionActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

public class Calificaciones_Cliente_RegistroCalificacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones_cliente_registro_calificacion);
    }

    //region opciones Navegacion
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);

        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String permisoAdmin = prefs.getString("PERMISOADMIN", "");
        String sesionIniciada = prefs.getString("SESIONINICIADA", "");
        Log.i("permiso Admin ====>", permisoAdmin.trim());
        Log.i("sesion Iniciada ====>", sesionIniciada.trim());
        //------------------------------------------------------------------------------------------

        controlMenuOpciones opcionesMenu = new controlMenuOpciones();
        opcionesMenu.asignarOpcionesAcceso(menu, permisoAdmin, sesionIniciada);

        return true;
    }
    //endregion

    //region Navegacion
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_login){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_registroUsuarios){
            Toast.makeText(this,"Registrar Usuario", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_misServicios){
            Toast.makeText(this,"Mis Servicios", Toast.LENGTH_SHORT).show();
            MisServicios();
        }
        else if (id ==R.id.item_registroServicios){
            Toast.makeText(this,"Reg. Servicios", Toast.LENGTH_SHORT).show();
            RegistrarServicios();
        }
        else if (id ==R.id.item_misCitas){
            Toast.makeText(this,"Mis Citas", Toast.LENGTH_SHORT).show();
            MisCitas();
        }
        else if (id ==R.id.item_registroCitas){
            Toast.makeText(this,"Registro Citas", Toast.LENGTH_SHORT).show();
            RegistrarCita();
        }
        else if (id ==R.id.item_reporteCitas){
            Toast.makeText(this,"Reporte Citas", Toast.LENGTH_SHORT).show();
            CitasCliente();
        }
        else if (id ==R.id.item_nosotros){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_citasPorCalificar){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            CitasPorCalificar();
        }
        else if (id ==R.id.item_misCalificaciones){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            MisCalificaciones();
        }
        else if (id ==R.id.item_nosotrosEdicion){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            NosotrosEdicion();
        }
        else if (id ==R.id.item_cerrarSesion){
            cerrarSesion();
        }

        return super.onOptionsItemSelected(item);

    }

    public void Login(){
        Intent login = new Intent(this, Seguridad_LoginActivity.class);
        startActivity(login);
    }

    public void RegistrarUsuario(){
        Intent registrarusuario = new Intent(this, Seguridad_RegistrarUsuarioActivity.class);
        startActivity(registrarusuario);
    }

    public void MisServicios(){
        Intent misServicios = new Intent(this, Servicios_Admin_MisServiciosActivity.class);
        startActivity(misServicios);
    }

    public void RegistrarServicios(){
        Intent producto = new Intent(this, Servicios_Admin_RegistrarServicioActivity.class);
        startActivity(producto);
    }

    public void MisCitas(){
        Intent misCitas = new Intent(this, Citas_Cliente_MisCitasActivity.class);
        startActivity(misCitas);
    }

    public void RegistrarCita(){
        Intent Catalogo = new Intent(this, Citas_Cliente_ListadoServiciosSeleccionarActivity.class);
        startActivity(Catalogo);
    }

    public void CitasCliente(){
        Intent miscitas = new Intent(this, Citas_Admin_MisCitasActivity.class);
        startActivity(miscitas);
    }

    public void CitasPorCalificar(){
        Intent miscitas = new Intent(this, Calificaciones_Cliente_CitasPorCalificarActivity.class);
        startActivity(miscitas);
    }

    public void MisCalificaciones(){
        Intent miscitas = new Intent(this, Calificaciones_Admin_MisCalificacionesActivity.class);
        startActivity(miscitas);
    }

    public void Nosotros(){
        Intent nosotros = new Intent(this, Nosotros_Cliente_NosotrosActivity.class);
        startActivity(nosotros);
    }

    public void NosotrosEdicion(){
        Intent nosotros = new Intent(this, Nosotros_Admin_NosotrosEdicionActivity.class);
        startActivity(nosotros);
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
                        Intent intent = new Intent(getApplicationContext(), Seguridad_LoginActivity.class);
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
