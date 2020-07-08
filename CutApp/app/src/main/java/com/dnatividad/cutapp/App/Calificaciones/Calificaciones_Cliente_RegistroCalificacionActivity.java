package com.dnatividad.cutapp.App.Calificaciones;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.dnatividad.cutapp.Utilitarios.Entidades.Citas;
import com.dnatividad.cutapp.Utilitarios.Entidades.Citas_Servicios;
import com.dnatividad.cutapp.Utilitarios.Entidades.Servicios;
import com.dnatividad.cutapp.Utilitarios.Entidades.Usuarios;
import com.dnatividad.cutapp.Utilitarios.General.ManejoErrores;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Calificaciones_Cliente_RegistroCalificacionActivity extends AppCompatActivity {
    TextView txt_idCita, txt_cliente, txt_nombreservicio, txt_fechaHoraCita;
    EditText txt_Comentario;
    String urlOrigin;
    Citas datosCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificaciones_cliente_registro_calificacion);
        setUrlOrigin();
        cargarDetalle();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void cargarDetalle() {
        final String datos_idCita = getIntent().getStringExtra("idCita");

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlOrigin + "/citas/listar-por-id-cita/" + datos_idCita);
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
    }

    private void llenarCampos(String response) {
        try{
            txt_idCita = (TextView) findViewById(R.id.txt_idCita);
            txt_cliente = (TextView) findViewById(R.id.txt_cliente);
            txt_nombreservicio = (TextView) findViewById(R.id.txt_nombreservicio);
            txt_fechaHoraCita = (TextView) findViewById(R.id.txt_fechaHoraCita);
            txt_Comentario = (EditText) findViewById(R.id.txt_Comentario);

            JSONObject jsonObject = new JSONObject(response);
            Log.i("jsonObject", String.valueOf(jsonObject));

            //region UsuarioRegistro
            JSONObject objJsonusuario = jsonObject.getJSONObject("usuario");
            //Log.i("objJsonusuario", jsonObject.toString());
            Usuarios usuarioRegistro = new Usuarios(
                    Integer.parseInt(objJsonusuario.getString("idUsuario")),
                    objJsonusuario.getString("nombreUsuario") + "\n",
                    objJsonusuario.getString("apellidoPaterno") + "\n",
                    objJsonusuario.getString("apellidoMaterno") + "\n",
                    "" + "\n",
                    1,
                    "" + "\n",
                    "" + "\n"
            );

            //region ListadoServicios
            JSONArray objJsonservicios = jsonObject.getJSONArray("servicios");
            ArrayList<Citas_Servicios> listaServicios = new ArrayList<>();
            //Log.i("objJsonservicios", objJsonservicios.toString());
            for (int j = 0; j < objJsonservicios.length(); j++) {
                JSONObject objJsonServicio = objJsonservicios.getJSONObject(j);
                listaServicios.add(
                        new Citas_Servicios(
                                Integer.parseInt(jsonObject.getString("idCita")),
                                new Servicios(
                                        Integer.parseInt(objJsonServicio.getString("idServicio")),
                                        objJsonServicio.getString("nombreServicio") + "\n",
                                        Double.parseDouble(objJsonServicio.getString("costoServicio")),
                                        objJsonServicio.getString("descripcionServicio") + "\n",
                                        objJsonServicio.getString("fotoServicio") + "\n"
                                )
                        )
                );
            }

            datosCita = new Citas(
                    Integer.parseInt(jsonObject.getString("idCita")),
                    jsonObject.getString("fechaCita")+"\n",
                    jsonObject.getString("horaCita")+"\n",
                    Boolean.parseBoolean(jsonObject.getString("calificadaCita")),
                    jsonObject.getString("comentarioCita")+"\n",
                    jsonObject.getString("estadoCita")+"\n",
                    usuarioRegistro,
                    listaServicios
            );

            txt_idCita.setText(String.valueOf(datosCita.getIdCita()));
            txt_cliente.setText(
                    datosCita.getUsuarios_registro().getNomUser() + ' ' +
                            datosCita.getUsuarios_registro().getApePatUser() + ' ' +
                            datosCita.getUsuarios_registro().getApeMatUser());
            txt_nombreservicio.setText(datosCita.getLista_servicios().get(0).getIdServicio().getNombreServicio());
            txt_fechaHoraCita.setText(datosCita.getFechaCita() + ' ' + datosCita.getHoraCita());
            //txt_Comentario.setText(datosCita.getComentarioCita());


        }catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public void calificarCita(View view){
        updateData();
    }

    private boolean hayErrores() {
        ManejoErrores manejoErrores = new ManejoErrores();
        String errores = "";
        //String saltolinea = "\n";
        if(txt_Comentario.getText().toString().equals("")){
            errores += "Falta ingresar la calificación";
        }

        //Log.i("errores",errores);
        if(errores.equals("")) return false;
        else{
            manejoErrores.MostrarError(this,errores);
            return true;
        }
    }

    private void updateData() {
        if(!hayErrores()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlOrigin + "/citas/actualizar");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("idCita", Integer.parseInt(txt_idCita.getText().toString()));
                        jsonObject.put("fechaCita", datosCita.getFechaCita());
                        jsonObject.put("horaCita", datosCita.getHoraCita());
                        jsonObject.put("comentarioCita", txt_Comentario.getText().toString());
                        jsonObject.put("estadoCita", datosCita.getEstado());
                        jsonObject.put("calificadaCita", 1);

                        JSONObject jsonObjectUsuario = new JSONObject();
                        jsonObjectUsuario.put("idUsuario", datosCita.getUsuarios_registro().getIdUser());

                        jsonObject.put("usuario", jsonObjectUsuario);
                        //Log.i("peluqueria", jsonObjectPeluqueria.toString());

                        JSONObject jsonObjectServicio = new JSONObject();
                        jsonObjectServicio.put("idServicio", datosCita.getLista_servicios().get(0).getIdServicio().getIdServicio());
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
                Intent intentP = new Intent(getApplicationContext(), Calificaciones_Cliente_CitasPorCalificarActivity.class);
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
