package com.dnatividad.cutapp.App.Citas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Admin_MisCalificacionesActivity;
import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Cliente_CitasPorCalificarActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Admin_NosotrosEdicionActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Citas_Admin_ActualizarCitasActivity extends AppCompatActivity {
    private ImageView fotoServicio;
    private TextView idCita;
    private TextView fechaCita, horaCita;
    private TextView idServicio;
    private TextView codtitu;
    private TextView correoUsuario;
    private TextView nombreUsuario;
    private TextView nombreServicio;
    private TextView estadoCita;
    private Spinner spi_estado;
    private String datos_telefonoUsuario;
    private static final int REQUEST_CALL = 1;

    //Para mostrar la imagen de codigo a bitmap
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_admin_actualizar_citas);
        setUrlOrigin();
        setReferences();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void setReferences() {
        spi_estado = (Spinner) findViewById(R.id.spinner_estado);
        String[] estados = {"CONFIRMADO","ATENDIDO"};
        spi_estado.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados));

        //coge el texto de los campos del catalogo
        fotoServicio = (ImageView) findViewById(R.id.imgfotoServicio);
        idCita = (TextView)findViewById(R.id.act_cod_pedido);
        fechaCita = (TextView)findViewById(R.id.act_fechaCita);
        horaCita = (TextView)findViewById(R.id.act_horaCita);
        idServicio = (TextView)findViewById(R.id.act_idServicio);
        codtitu = (TextView)findViewById(R.id.act_Producto);
        correoUsuario = (TextView)findViewById(R.id.act_correoUsuario);
        nombreUsuario = (TextView)findViewById(R.id.act_nombreUsuario);
        nombreServicio = (TextView)findViewById(R.id.act_nombreServicio);
        estadoCita = (TextView)findViewById(R.id.act_estado);

        ImageButton btnLlamada = (ImageButton) findViewById(R.id.imgLlamada);
        btnLlamada.setBackgroundResource(0);

        String datos_fotoServicio = getIntent().getStringExtra("fotoServicio");
        String datos_idCita = getIntent().getStringExtra("idCita");
        String datos_idServicio = getIntent().getStringExtra("idServicio");
        //String datos_idUsuario = getIntent().getStringExtra("idUsuario");
        String datos_titulo = getIntent().getStringExtra("titulo");
        String datos_fechaCita = getIntent().getStringExtra("fechaCita");
        String datos_horaCita = getIntent().getStringExtra("horaCita");
        String datos_correoUsuario = getIntent().getStringExtra("correoUsuario");
        String datos_nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String datos_nombreServicio = getIntent().getStringExtra("nombreServicio");
        String datos_estadoServicio = getIntent().getStringExtra("estadoServicio");
        datos_telefonoUsuario = getIntent().getStringExtra("telefonoUsuario");

        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(datos_fotoServicio),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //*************************************************************************************************

        fotoServicio.setImageBitmap(bitmap);
        idCita.setText(datos_idCita);
        fechaCita.setText(datos_fechaCita);
        horaCita.setText(datos_horaCita);
        idServicio.setText(datos_idServicio);
        codtitu.setText(datos_titulo);
        correoUsuario.setText(datos_correoUsuario);
        nombreUsuario.setText(datos_nombreUsuario);
        nombreServicio.setText(datos_nombreServicio);
        estadoCita.setText(datos_estadoServicio);

        ImageView llamada = findViewById(R.id.imgLlamada);
        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamar(datos_telefonoUsuario);
            }
        });
    }

    private void llamar(String telefonoPeluqueria) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else{
            String numeroLlamar = "tel:" + telefonoPeluqueria;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(numeroLlamar)));
        }
    }

    public void ActualizarPedido(View v){
        updateData();
    }

    private void updateData() {
        final String datos_idUsuario = getIntent().getStringExtra("idUsuario");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin + "/citas/actualizar");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("idCita", Integer.parseInt(idCita.getText().toString()));
                    //Log.i("idCita", txt_idServicio.getText().toString());
                    jsonObject.put("fechaCita", fechaCita.getText().toString());
                    //Log.i("fechaCita", txt_nombreServicio.getText().toString());
                    jsonObject.put("horaCita", horaCita.getText().toString());
                    //Log.i("horaCita", txt_descripcionServicio.getText().toString());
                    jsonObject.put("comentarioCita", "");
                    //Log.i("comentarioCita", txt_costoServicio.getText().toString());
                    jsonObject.put("estadoCita", spi_estado.getSelectedItem().toString());
                    //Log.i("estadoCita", spi_estado.getSelectedItem().toString());

                    JSONObject jsonObjectUsuario = new JSONObject();
                    jsonObjectUsuario.put("idUsuario", Integer.parseInt(datos_idUsuario));

                    jsonObject.put("usuario", jsonObjectUsuario);
                    //Log.i("peluqueria", jsonObjectPeluqueria.toString());

                    JSONObject jsonObjectServicio = new JSONObject();
                    jsonObjectServicio.put("idServicio", Integer.parseInt(idServicio.getText().toString()));
                    JSONArray objJsonservicios = new JSONArray();
                    objJsonservicios.put(jsonObjectServicio);
                    jsonObject.put("servicios", objJsonservicios);

                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("PUT");

                    httpURLConnection.connect();
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(jsonObject.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

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

    public void AnularPedido(View v){
        deleteData();
    }

    private void deleteData() {
        DialogInterface.OnClickListener confirmacion = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL(urlOrigin + "/citas/eliminar/" + idCita.getText().toString());

                                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                                    httpURLConnection.setRequestMethod("DELETE");
                                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                    httpURLConnection.setRequestProperty("charset","utf-8");
                                    httpURLConnection.setRequestProperty("Accept", "application/json");
                                    httpURLConnection.setDoOutput(true);
                                    httpURLConnection.setDoInput(true);
                                    httpURLConnection.connect();

                                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                    String temp = null;
                                    StringBuilder sb = new StringBuilder();
                                    while((temp = in.readLine()) != null){
                                        sb.append(temp).append(" ");
                                    }
                                    String result = sb.toString();
                                    Log.i("resultado", result);
                                    in.close();

                                    analyseResponse(result);
                                    httpURLConnection.disconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lbl_confirmacion_anular_cita).setPositiveButton(R.string.lbl_confirmacion_si, confirmacion)
                .setNegativeButton(R.string.lbl_confirmacion_no, confirmacion).show();
    }

    void analyseResponse(String response){
        Log.i("Respuesta", response);

        switch (response){
            case "validData":
                Intent intent = new Intent(getApplicationContext(), Servicios_Admin_MisServiciosActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), Citas_Admin_MisCitasActivity.class);
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
