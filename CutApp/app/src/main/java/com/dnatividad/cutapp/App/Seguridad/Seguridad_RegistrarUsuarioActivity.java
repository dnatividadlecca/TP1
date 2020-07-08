package com.dnatividad.cutapp.App.Seguridad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Admin_MisCalificacionesActivity;
import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Cliente_CitasPorCalificarActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Admin_NosotrosEdicionActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.Utilitarios.General.ManejoErrores;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;


public class Seguridad_RegistrarUsuarioActivity extends AppCompatActivity {
    EditText reg_nombreUsuario, reg_apellidoMaterno, reg_apellidoPaterno, reg_telefono, reg_correoUsuario, reg_password;
    String urlOrigin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setReferences();
        setUrlOrigin();
        //setValues();
        setContentView(R.layout.activity_seguridad_registrar_usuario);
    }

    private void setUrlOrigin() {
        //urlOrigin = getString(R.string.urlOrigin);
        urlOrigin = getString(R.string.urlOrigin);
    }

    void setReferences(){
        reg_nombreUsuario = findViewById(R.id.txt_nombreUsuario);
        reg_apellidoMaterno = findViewById(R.id.txt_apellidoMaterno);
        reg_apellidoPaterno = findViewById(R.id.txt_apellidoPaterno);
        reg_telefono = findViewById(R.id.txt_telefonoUsuario);
        reg_correoUsuario = findViewById(R.id.txt_correoUsuario);
        reg_password = findViewById(R.id.txt_contrasenaUsuario);
    }

    public void Reg_usuario(View v){
        insertData();
    }

    void insertData(){
        setReferences();

        Log.i("hayErrores", "se llama hayErrores");
        if(!hayErrores()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlOrigin + "/usuarios/registrar");

                        JSONObject jsonObject = new JSONObject();
                        //jsonObject.put("id", getIntent().getStringExtra("id"));

                        jsonObject.put("nombreUsuario", reg_nombreUsuario.getText().toString());
                        //Log.i("nombreUsuario", reg_nombreUsuario.getText().toString());
                        jsonObject.put("apellidoMaterno", reg_apellidoMaterno.getText().toString());
                        //Log.i("apellidoMaterno", reg_apellidoMaterno.getText().toString());
                        jsonObject.put("apellidoPaterno", reg_apellidoPaterno.getText().toString());
                        //Log.i("apellidoPaterno", reg_apellidoPaterno.getText().toString());
                        jsonObject.put("telefono", reg_telefono.getText().toString());
                        //Log.i("telefono", reg_telefono.getText().toString());
                        jsonObject.put("rolUsuario", "0");
                        jsonObject.put("correoUsuario", reg_correoUsuario.getText().toString());
                        //Log.i("correoUsuario", reg_correoUsuario.getText().toString());
                        jsonObject.put("password", reg_password.getText().toString());
                        //Log.i("password", reg_password.getText().toString());

                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        httpURLConnection.setRequestProperty("Accept", "application/json");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.connect();
                        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                        dataOutputStream.writeBytes(jsonObject.toString());
                        dataOutputStream.flush();
                        dataOutputStream.close();
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                        String response = "";
                        String line = "";

                        while ((line = br.readLine()) != null) {
                            response += line;
                        }

                        analyseResponse(response);
                        httpURLConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private boolean hayErrores() {
        ManejoErrores manejoErrores = new ManejoErrores();
        String errores = "";
        String saltolinea = "\n";
        if(reg_nombreUsuario.getText().toString().equals("")){
            errores += "Falta ingresar su nombre" + saltolinea;
        }

        if(reg_apellidoPaterno.getText().toString().equals("")){
            errores += "Falta ingresar su apellido paterno" + saltolinea;
        }

        if(reg_apellidoMaterno.getText().toString().equals("")){
            errores += "Falta ingresar su apellido materno" + saltolinea;
        }

        if(reg_telefono.getText().toString().equals("")){
            errores += "Falta ingresar su teléfono" + saltolinea;
        }

        if(reg_correoUsuario.getText().toString().equals("")){
            errores += "Falta ingresar su correo" + saltolinea;
        }

        if(reg_password.getText().toString().equals("")){
            errores += "Falta ingresar su contraseña" + saltolinea;
        }

        //Log.i("errores",errores);
        if(errores.equals("")) return false;
        else{
            manejoErrores.MostrarError(this,errores);
            return true;
        }
    }

    void analyseResponse(String response){
        Log.i("Respuesta", response);

        switch (response){
            case "validData":
                Intent intent = new Intent(getApplicationContext(), Seguridad_LoginActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), Seguridad_LoginActivity.class);
                intentP.putExtra("urlOrigin", urlOrigin);
                startActivity(intentP);
                break;
        }
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
            Toast.makeText(this,"Citas por Calificar", Toast.LENGTH_SHORT).show();
            CitasPorCalificar();
        }
        else if (id ==R.id.item_misCalificaciones){
            Toast.makeText(this,"Mis Calificaciones", Toast.LENGTH_SHORT).show();
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
