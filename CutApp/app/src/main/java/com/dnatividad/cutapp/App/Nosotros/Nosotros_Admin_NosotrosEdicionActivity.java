package com.dnatividad.cutapp.App.Nosotros;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Admin_MisCalificacionesActivity;
import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Cliente_CitasPorCalificarActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.Utilitarios.Entidades.Peluqueria;
import com.dnatividad.cutapp.Utilitarios.General.ManejoErrores;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;

public class Nosotros_Admin_NosotrosEdicionActivity extends AppCompatActivity {
    TextView lbl_idPeluqueria, lbl_latitud, lbl_longitud, txt_nombrePeluqueria, txt_descripcionPeluqueria, txt_direccionPeluqueria, txt_telefonoPeluqueria;
    TimePicker tp_horarioAtencionInicioPeluqueria, tp_horarioAtencionFinPeluqueria;
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nosotros_admin_nosotros_edicion);
        setUrlOrigin();
        buscar();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    public void buscar() {
        lbl_idPeluqueria = (TextView) findViewById(R.id.lbl_idPeluqueria);
        txt_nombrePeluqueria = (TextView) findViewById(R.id.txt_nombrePeluqueria);
        txt_descripcionPeluqueria = (TextView) findViewById(R.id.txt_descripcionPeluqueria);
        txt_direccionPeluqueria = (TextView) findViewById(R.id.txt_direccionPeluqueria);
        txt_telefonoPeluqueria = (TextView) findViewById(R.id.txt_telefonoPeluqueria);

        tp_horarioAtencionInicioPeluqueria = (TimePicker) findViewById(R.id.tp_horarioAtencionInicioPeluqueria);
        tp_horarioAtencionFinPeluqueria = (TimePicker) findViewById(R.id.tp_horarioAtencionFinPeluqueria);

        lbl_latitud = (TextView) findViewById(R.id.lbl_latitud);
        lbl_longitud = (TextView) findViewById(R.id.lbl_longitud);

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlOrigin + "/peluquerias/listar/1");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");

                    int responseCode = httpURLConnection.getResponseCode();
                    String responseMessage = httpURLConnection.getResponseMessage();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response = "";
                        String line = "";
                        //Log.i("response","Por ejecutar");

                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                        //Log.i("response",response);
                        llenarCampos(response);

                    } else {

                        Log.v("CatalogClient", "Response code:" + responseCode);
                        Log.v("CatalogClient", "Response message:" + responseMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error Message",Toast.LENGTH_LONG).show();
                        }
                    });

                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        });
        runOnUiThread(new Runnable() {
            public void run() {
                //mostrarErrorLogin(response[0]);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void llenarCampos(String response) {
        Integer hora, minuto;
        String AMPM;
        try{
            JSONObject jsonObject = new JSONObject(response);
            Log.i("jsonObject", String.valueOf(jsonObject));

            Peluqueria datosPeluqueria = new Peluqueria(
                    Integer.parseInt(jsonObject.getString("idPeluqueria")),
                    jsonObject.getString("nombrePeluqueria")+"\n",
                    jsonObject.getString("telfPeluqueria")+"\n",
                    jsonObject.getString("direccionPeluqueria")+"\n",
                    jsonObject.getString("horaInicioPeluqueria")+"\n",
                    jsonObject.getString("horaFinPeluqueria")+"\n",
                    jsonObject.getString("descripcion")+"\n",
                    Double.parseDouble(jsonObject.getString("latitud")),
                    Double.parseDouble(jsonObject.getString("longitud"))
            );

            lbl_idPeluqueria.setText(String.valueOf(datosPeluqueria.getIdPeluqueria()));
            txt_nombrePeluqueria.setText(datosPeluqueria.getNombrePeluqueria());
            txt_direccionPeluqueria.setText(datosPeluqueria.getDireccionPeluqueria());
            txt_telefonoPeluqueria.setText(datosPeluqueria.getTelfPeluqueria());

            //region hora inicio
            AMPM = datosPeluqueria.getHoraInicioPeluqueria().substring(6,8);
            //Log.i("AMPM", AMPM);
            if(AMPM.equals("AM"))
                hora = Integer.parseInt(datosPeluqueria.getHoraInicioPeluqueria().substring(0,2));
            else
                hora = Integer.parseInt(datosPeluqueria.getHoraInicioPeluqueria().substring(0,2)) + 12;
            Log.i("Hora I", String.valueOf(hora));
            minuto = Integer.parseInt(datosPeluqueria.getHoraInicioPeluqueria().substring(3,5));
            tp_horarioAtencionInicioPeluqueria.setHour(hora);
            tp_horarioAtencionInicioPeluqueria.setMinute(minuto);
            //endregion

            //region hora fin
            AMPM = datosPeluqueria.getHoraFinPeluqueria().substring(6,8);
            //Log.i("AMPM", AMPM);
            if(AMPM.equals("AM"))
                hora = Integer.parseInt(datosPeluqueria.getHoraFinPeluqueria().substring(0,2));
            else
                hora = Integer.parseInt(datosPeluqueria.getHoraFinPeluqueria().substring(0,2)) + 12;
            Log.i("Hora F", String.valueOf(hora));
            minuto = Integer.parseInt(datosPeluqueria.getHoraFinPeluqueria().substring(3,5));
            tp_horarioAtencionFinPeluqueria.setHour(hora);
            tp_horarioAtencionFinPeluqueria.setMinute(minuto);
            //endregion

            txt_descripcionPeluqueria.setText(datosPeluqueria.getDescripcion());
            lbl_latitud.setText(String.valueOf(datosPeluqueria.getLatitud()));
            lbl_longitud.setText(String.valueOf(datosPeluqueria.getLongitud()));
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Act_DatosPeluqueria(View v){
        updateData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateData() {
        String horaInicio, horaFin = "";
        Integer diferencialHoras = 0;
        //region hora inicio
        if(tp_horarioAtencionInicioPeluqueria.getHour() < 12){
            diferencialHoras = 0;
        }else
            diferencialHoras = 12;


        if(String.valueOf(tp_horarioAtencionInicioPeluqueria.getHour() - diferencialHoras).length() == 1)
            horaInicio = '0' + String.valueOf(tp_horarioAtencionInicioPeluqueria.getHour()) + ':';
        else
            horaInicio = String.valueOf(tp_horarioAtencionInicioPeluqueria.getHour() - diferencialHoras) + ':';

        Log.i("H Inicio 1", horaInicio);
        if(String.valueOf(tp_horarioAtencionInicioPeluqueria.getMinute()).length() == 1)
            horaInicio += String.valueOf(tp_horarioAtencionInicioPeluqueria.getMinute()) + '0'+ ' ';
        else
            horaInicio += String.valueOf(tp_horarioAtencionInicioPeluqueria.getMinute()) + ' ';

        Log.i("H Inicio 2", horaInicio);
        if(tp_horarioAtencionInicioPeluqueria.getHour() < 12)
            horaInicio += "AM";
        else
            horaInicio += "PM";
        Log.i("H Inicio 3", horaInicio);
        //endregion

        //region hora fin
        if(tp_horarioAtencionFinPeluqueria.getHour() < 12){
            diferencialHoras = 0;
        }else
            diferencialHoras = 12;
        if(String.valueOf(tp_horarioAtencionFinPeluqueria.getHour()- diferencialHoras).length() == 1)
            horaFin = '0' + String.valueOf(tp_horarioAtencionFinPeluqueria.getHour() - diferencialHoras) + ':';
        else
            horaFin = String.valueOf(tp_horarioAtencionFinPeluqueria.getHour() - diferencialHoras) + ':';

        Log.i("H Fin 1", horaFin);
        if(String.valueOf(tp_horarioAtencionFinPeluqueria.getMinute()).length() == 1)
            horaFin += String.valueOf(tp_horarioAtencionFinPeluqueria.getMinute()) + '0'+ ' ';
        else
            horaFin += String.valueOf(tp_horarioAtencionFinPeluqueria.getMinute()) + ' ';

        Log.i("H Fin 2", horaFin);
        if(tp_horarioAtencionFinPeluqueria.getHour() < 12)
            horaFin += "AM";
        else
            horaFin += "PM";
        Log.i("H Fin 3", horaFin);
        //endregion

        final String finalHoraInicio = horaInicio;
        final String finalHoraFin = horaFin;

        if(!hayErrores()){
            AsyncTask.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlOrigin + "/peluquerias/actualizar");

                        JSONObject jsonObject = new JSONObject();
                        //jsonObject.put("id", getIntent().getStringExtra("id"));

                        jsonObject.put("idPeluqueria", lbl_idPeluqueria.getText().toString());
                        //Log.i("idPeluqueria", lbl_idPeluqueria.getText().toString());
                        jsonObject.put("nombrePeluqueria", txt_nombrePeluqueria.getText().toString());
                        //Log.i("nombrePeluqueria", txt_nombrePeluqueria.getText().toString());
                        jsonObject.put("direccionPeluqueria", txt_direccionPeluqueria.getText().toString());
                        //Log.i("direccionPeluqueria", txt_direccionPeluqueria.getText().toString());
                        jsonObject.put("telfPeluqueria", txt_telefonoPeluqueria.getText().toString());
                        //Log.i("telfPeluqueria", txt_telefonoPeluqueria.getText().toString());
                        jsonObject.put("descripcion", txt_descripcionPeluqueria.getText().toString());
                        //Log.i("descripcion", txt_descripcionPeluqueria.getText().toString());
                        jsonObject.put("latitud", lbl_latitud.getText().toString());
                        //Log.i("latitud", lbl_latitud.getText().toString());
                        jsonObject.put("longitud", lbl_longitud.getText().toString());
                        //Log.i("longitud", lbl_longitud.getText().toString());
                        jsonObject.put("horaInicioPeluqueria", finalHoraInicio);
                        //Log.i("horaInicioPeluqueria", horaInicio);
                        jsonObject.put("horaFinPeluqueria", finalHoraFin);
                        //Log.i("horaFinPeluqueria", finalHoraFin);

                        //endregion

                        //region hora fin
                        //endregion

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
    }

     private boolean hayErrores() {
        ManejoErrores manejoErrores = new ManejoErrores();
        String errores = "";
        String saltolinea = "\n";
        if(txt_nombrePeluqueria.getText().toString().equals("")){
            errores += "Falta ingresar su el nombre de la peluquería" + saltolinea;
        }

        if(txt_direccionPeluqueria.getText().toString().equals("")){
            errores += "Falta ingresar la dirección" + saltolinea;
        }

        if(txt_telefonoPeluqueria.getText().toString().equals("")){
            errores += "Falta ingresar el teléfono" + saltolinea;
        }

        if(txt_descripcionPeluqueria.getText().toString().equals("")){
            errores += "Falta ingresar la descrición" + saltolinea;
        }

        //LocalTime horaInicio = LocalTime.parse( String.valueOf(tp_horarioAtencionInicioPeluqueria.getHour()) + ':' + String.valueOf(tp_horarioAtencionInicioPeluqueria.getMinute())) ;
        //LocalTime horaFin = LocalTime.parse( String.valueOf(tp_horarioAtencionFinPeluqueria.getHour()) + ':' + String.valueOf(tp_horarioAtencionFinPeluqueria.getMinute())) ;
        //if(Duration.between( horaInicio , horaFin ).isNegative())
            //errores += "La hora de Inicio no puede ser mayor a la hora de Fin" + saltolinea;

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
