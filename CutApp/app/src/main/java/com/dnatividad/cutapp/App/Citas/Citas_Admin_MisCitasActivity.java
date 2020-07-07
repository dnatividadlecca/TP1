package com.dnatividad.cutapp.App.Citas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.dnatividad.cutapp.Utilitarios.Adaptadores.AdaptadorListadoCitasAdmin;
import com.dnatividad.cutapp.Utilitarios.Entidades.Citas;
import com.dnatividad.cutapp.Utilitarios.Entidades.Citas_Servicios;
import com.dnatividad.cutapp.Utilitarios.Entidades.Servicios;
import com.dnatividad.cutapp.Utilitarios.Entidades.Usuarios;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Citas_Admin_MisCitasActivity extends AppCompatActivity {
    private ListView listItems;
    private AdaptadorListadoCitasAdmin adaptadorCitas;

    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_admin_mis_citas);

        setUrlOrigin();
        cargarCitas();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    public void cargarCitas(){
        Log.i("demo", "se cargara datos de todos");
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        final Integer idUsusarioSesion = Integer.parseInt(prefs.getString("IDUSUSARIO", ""));
        Log.i("Id Usuario Sesion ====>", idUsusarioSesion.toString());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlOrigin+"/citas/listar");
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");

                    int responseCode = httpURLConnection.getResponseCode();
                    String responseMessage = httpURLConnection.getResponseMessage();
                    Log.i("response", responseMessage);
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response="";
                        String line = "";
                        while ((line=br.readLine()) != null) {
                            response += line;
                        }
                        updateLista(response);

                    }else{
                        Log.v("CatalogClient", "Response code:"+ responseCode);
                        Log.v("CatalogClient", "Response message:"+ responseMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        });
    }

    void updateLista(String reportList) {

        final ArrayList<Citas> ListItems = new ArrayList<>();
        listItems = (ListView) findViewById(R.id.listaMisCitas);
        try {
            Log.i("reporte", reportList);
            JSONArray jsonArray = new JSONArray(reportList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);
                //Log.i("citas",objJson.toString());

                //region UsuarioRegistro
                JSONObject objJsonusuario = objJson.getJSONObject("usuario");
                //Log.i("objJsonusuario", objJsonusuario.toString());
                Usuarios usuarioRegistro = new Usuarios(
                        Integer.parseInt(objJsonusuario.getString("idUsuario")),
                        objJsonusuario.getString("nombreUsuario") + "\n",
                        objJsonusuario.getString("apellidoPaterno") + "\n",
                        objJsonusuario.getString("apellidoMaterno") + "\n",
                        objJsonusuario.getString("telefono") + "\n",
                        1,
                        objJsonusuario.getString("correoUsuario") + "\n",
                        "" + "\n"
                );
                //endregion

                //region ListadoServicios
                JSONArray objJsonservicios = objJson.getJSONArray("servicios");
                ArrayList<Citas_Servicios> listaServicios = new ArrayList<>();
                //Log.i("objJsonservicios", objJsonservicios.toString());
                for (int j = 0; j < objJsonservicios.length(); j++) {
                    JSONObject objJsonServicio = objJsonservicios.getJSONObject(j);
                    listaServicios.add(
                            new Citas_Servicios(
                                    Integer.parseInt(objJson.getString("idCita")),
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

                //endregion

                ListItems.add(new Citas(
                        Integer.parseInt(objJson.getString("idCita")),
                        objJson.getString("fechaCita") + "\n",
                        objJson.getString("horaCita") + "\n",
                        Boolean.parseBoolean(objJson.getString("calificadaCita")),
                        objJson.getString("comentarioCita") + "\n",
                        objJson.getString("estadoCita")+"\n",
                        usuarioRegistro,
                        listaServicios
                ));

            }

            runOnUiThread(new Runnable() {
                //Muestro el contenido del arraylist
                public void run() {
                    adaptadorCitas = new AdaptadorListadoCitasAdmin(Citas_Admin_MisCitasActivity.this, ListItems);
                    listItems.setAdapter(adaptadorCitas);
                }
            });

            listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

                    Citas cita = ListItems.get(position);
                    Intent i = new Intent(getApplicationContext(), Citas_Admin_ActualizarCitasActivity.class);
                    i.putExtra("idCita", String.valueOf(cita.getIdCita()));
                    i.putExtra("idServicio", String.valueOf(cita.getLista_servicios().get(0).getIdServicio().getIdServicio()));
                    i.putExtra("correoUsuario", String.valueOf(cita.getUsuarios_registro().getCorreoUser()));
                    i.putExtra("nombreServicio", cita.getLista_servicios().get(0).getIdServicio().getNombreServicio());
                    i.putExtra("idUsuario", String.valueOf(cita.getUsuarios_registro().getIdUser()));
                    i.putExtra("nombreUsuario",
                             cita.getUsuarios_registro().getNomUser() + ' ' +
                                   cita.getUsuarios_registro().getApePatUser() + ' ' +
                                   cita.getUsuarios_registro().getApeMatUser());
                    i.putExtra("telefonoUsuario", cita.getUsuarios_registro().getTelfUser());
                    i.putExtra("fechaCita", cita.getFechaCita());
                    i.putExtra("horaCita", cita.getHoraCita());
                    i.putExtra("costoServicio", String.valueOf(cita.getLista_servicios().get(0).getIdServicio().getCostoServicio()));
                    i.putExtra("estadoServicio", cita.getEstado());
                    i.putExtra("fotoServicio", String.valueOf(cita.getLista_servicios().get(0).getIdServicio().getFotoServicio()));
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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
