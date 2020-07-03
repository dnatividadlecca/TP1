package com.dnatividad.cutapp;

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

import com.dnatividad.cutapp.ManejoMenu.controlMenuOpciones;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;


public class RegistrarUsuarioActivity extends AppCompatActivity {
    EditText reg_nombreUsuario, reg_apellidoMaterno, reg_apellidoPaterno, reg_telefono, reg_correoUsuario, reg_password;
    String urlOrigin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setReferences();
        setUrlOrigin();
        //setValues();
        setContentView(R.layout.activity_registrar_usuario);
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
        reg_password = findViewById(R.id.txt_passwordUsuario);
    }

    private void setValues() {
        reg_nombreUsuario.setText(Integer.toString(getIntent().getIntExtra("nombreUsuario",0)));
        reg_apellidoMaterno.setText(Integer.toString(getIntent().getIntExtra("apellidoMaterno",0)));
        reg_apellidoPaterno.setText(Integer.toString(getIntent().getIntExtra("apellidoPaterno",0)));
        reg_telefono.setText(Integer.toString(getIntent().getIntExtra("telefono",0)));
        reg_correoUsuario.setText(Integer.toString(getIntent().getIntExtra("correoUsuario",0)));
        reg_password.setText(Integer.toString(getIntent().getIntExtra("password",0)));
    }

    public void Reg_usuario(View v){
         updateData();
    }

    void updateData(){
        setReferences();
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
                    jsonObject.put("rolUsuario", "1");
                    jsonObject.put("correoUsuario", reg_correoUsuario.getText().toString());
                    //Log.i("correoUsuario", reg_correoUsuario.getText().toString());
                    jsonObject.put("password", reg_password.getText().toString());
                    //Log.i("password", reg_password.getText().toString());

                    /*
                    jsonObject.put("nombreUsuario", "prueba1");
                    jsonObject.put("apellidoMaterno", "prueba1");
                    jsonObject.put("apellidoPaterno", "prueba1");
                    jsonObject.put("telefono", "4875742");
                    jsonObject.put("rolUsuario", "1");
                    jsonObject.put("correoUsuario", "demo1@demo.com");
                    jsonObject.put("password", "123456");
                    */

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

    void analyseResponse(String response){
        Log.i("Respuesta", response);

        switch (response){
            case "validData":
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), LoginActivity.class);
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
        else if (id ==R.id.item_nosotros){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_contactenos){
            Toast.makeText(this,"Contáctenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_ubicanos){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_registroCitas){
            Toast.makeText(this,"Registro Citas", Toast.LENGTH_SHORT).show();
            RegistrarCita();
        }
        else if (id ==R.id.item_misCitas){
            Toast.makeText(this,"Mis Citas", Toast.LENGTH_SHORT).show();
            MisCitas();
        }
        else if (id ==R.id.item_reporteCitas){
            Toast.makeText(this,"Reporte Citas", Toast.LENGTH_SHORT).show();
            CitasCliente();
        }
        else if (id ==R.id.item_registroServicios){
            Toast.makeText(this,"Reg. Servicios", Toast.LENGTH_SHORT).show();
            RegistrarServicios();
        }
        else if (id ==R.id.item_misServicios){
            Toast.makeText(this,"Mis Servicios", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        else if (id ==R.id.item_cerrarSesion){
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);

    }
    //endregion

    //region Navegacion
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

    public void RegistrarCita(){
        Intent Catalogo = new Intent(this, MisServiciosClienteActivity.class);
        startActivity(Catalogo);
    }

    public void MisCitas(){
        Intent misCitas = new Intent(this, MisCitas.class);
        startActivity(misCitas);
    }

    public void CitasCliente(){
        Intent miscitas = new Intent(this, CitasTotalClientesActivity.class);
        startActivity(miscitas);
    }

    public void RegistrarServicios(){
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
