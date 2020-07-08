package com.dnatividad.cutapp.App.Citas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Citas_Cliente_RegistrarCitaActivity extends AppCompatActivity {
    private ImageView img_fotoServicio;
    private TextView txt_idServicio, txt_nombreServicio, txt_descripcionServicio;
    String urlOrigin;

    //extraer la fecha del calendario
    CalendarView calendarView;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_cliente_registrar_cita);
        setUrlOrigin();
        setReferences();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void setReferences() {
        img_fotoServicio = (ImageView) findViewById(R.id.imgfotoServicio);
        txt_idServicio = (TextView)findViewById(R.id.txt_idServicio);
        txt_nombreServicio = (TextView)findViewById(R.id.txt_nombreServicio);

        String datos_fotoServicio = getIntent().getStringExtra("fotoServicio");
        String datos_idServicio = getIntent().getStringExtra("idServicio");
        String datos_nombreServicio = getIntent().getStringExtra("nombreServicio");
        //String datos_descripcionServicio = getIntent().getStringExtra("descripcionServicio");

        byte [] encodeByte = Base64.decode(String.valueOf(datos_fotoServicio),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        img_fotoServicio.setImageBitmap(bitmap);
        txt_idServicio.setText(datos_idServicio);
        txt_nombreServicio.setText(datos_nombreServicio);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Reg_Citas(View v){
        DialogInterface.OnClickListener confirmacion = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        insertData();;

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lbl_confirmacion_registrar_cita).setPositiveButton(R.string.lbl_confirmacion_si, confirmacion)
                .setNegativeButton(R.string.lbl_confirmacion_no, confirmacion).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void insertData(){
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        final Integer idUsuario = Integer.parseInt(prefs.getString("IDUSUSARIO", ""));
        calendarView = (CalendarView) findViewById(R.id.simpleCalendarView);
        timePicker = (TimePicker) findViewById(R.id.simpleTimeView);

        final SimpleDateFormat simpleDatePickerFechaPedido = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //final SimpleDateFormat simpleTimePickerFechaPedido = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());

        String hora = "";
        hora = String.valueOf(timePicker.getHour()) + ':' + String.valueOf(timePicker.getMinute()) + " ";

        if(timePicker.getHour() < 12)
            hora += "AM";
        else
            hora += "PM";

        final String finalHora = hora;
        Log.i("idUsuario", String.valueOf(idUsuario));
        Log.i("fecha", simpleDatePickerFechaPedido.format(calendarView.getDate()));
        Log.i("hora", hora);
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin + "/citas/registrar");

                    JSONObject jsonObject = new JSONObject();
                    //jsonObject.put("id", getIntent().getStringExtra("id"));

                    jsonObject.put("fechaCita", simpleDatePickerFechaPedido.format(calendarView.getDate()));
                    //Log.i("fechaCita", txt_nombreServicio.getText().toString());
                    jsonObject.put("horaCita", finalHora);
                    //Log.i("horaCita", horaCita);
                    jsonObject.put("comentarioCita", "");
                    //Log.i("comentarioCita", txt_costoServicio.getText().toString());
                    jsonObject.put("estadoCita", "GENERADA");

                    JSONObject jsonObjectUsuario = new JSONObject();
                    jsonObjectUsuario.put("idUsuario", Integer.parseInt(String.valueOf(idUsuario)));

                    jsonObject.put("usuario", jsonObjectUsuario);
                    //Log.i("usuario", jsonObjectUsuario.toString());

                    JSONObject jsonObjectServicio = new JSONObject();
                    jsonObjectServicio.put("idServicio", Integer.parseInt(txt_idServicio.getText().toString()));
                    JSONArray objJsonservicios = new JSONArray();
                    objJsonservicios.put(jsonObjectServicio);
                    jsonObject.put("servicios", objJsonservicios);

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
                Intent intent = new Intent(getApplicationContext(), Servicios_Admin_MisServiciosActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), Citas_Cliente_MisCitasActivity.class);
                intentP.putExtra("urlOrigin", urlOrigin);
                startActivity(intentP);
                break;
        }
    }

    //------------------------------------MENU-----------------------------------------------

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
