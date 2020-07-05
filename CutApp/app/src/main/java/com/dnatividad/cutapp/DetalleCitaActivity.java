package com.dnatividad.cutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.Entidades.Peluqueria;
import com.dnatividad.cutapp.Entidades.Servicios;
import com.dnatividad.cutapp.ManejoMenu.controlMenuOpciones;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetalleCitaActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView txt_idCita, txt_descripcionServicio, txt_nombreServicio, txt_costoServicio, txt_estado;
    String urlOrigin;
    //Double latitud, longitud;
    Peluqueria objetoPeluqueria;
    private GoogleMap mMap;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);
        setUrlOrigin();
        setReferences();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void setReferences() {
        txt_idCita = (TextView)findViewById(R.id.txt_idCita);
        txt_descripcionServicio = (TextView)findViewById(R.id.txt_descripcionServicio);
        txt_nombreServicio = (TextView)findViewById(R.id.txt_nombreServicio);
        txt_costoServicio = (TextView)findViewById(R.id.txt_costoServicio);
        txt_estado = (TextView)findViewById(R.id.txt_estado);

        String datos_idCita = getIntent().getStringExtra("idCita");
        String datos_idServicio = getIntent().getStringExtra("idServicio");
        String datos_nombreServicio = getIntent().getStringExtra("nombreServicio");
        String datos_descripcionServicio = getIntent().getStringExtra("descripcionServicio");
        String datos_costoServicio = getIntent().getStringExtra("costoServicio");
        String datos_estadoServicio = getIntent().getStringExtra("estadoServicio");

        txt_estado.setText(datos_estadoServicio);
        txt_idCita.setText(datos_idCita);
        txt_nombreServicio.setText(datos_nombreServicio);
        txt_descripcionServicio.setText(datos_descripcionServicio);
        txt_costoServicio.setText(datos_costoServicio);

        cargarDatosPeluqueria(datos_idServicio);
    }

    void cargarDatosPeluqueria(final String idServicio){
        final TextView txt_direccionPeluqueria = (TextView)findViewById(R.id.txt_direccionPeluqueria);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlOrigin + "/servicios/listar/" + idServicio);
                    //URL url = new URL("http://10.0.2.2:8080/servicios/listar/" + idServicio);

                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");

                    int responseCode = httpURLConnection.getResponseCode();
                    String responseMessage = httpURLConnection.getResponseMessage();

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response = "";
                        String line = "";
                        //Log.i("response","Por ejecutar");

                        while ((line=br.readLine()) != null) {
                            response += line;
                        }

                        //region capturo datos de json en objeto Servicio y peluqueria
                        JSONObject jsonObject = new JSONObject(response);
                        Log.i("jsonObject", String.valueOf(jsonObject));

                        //region obtengo datos y creo el objeto peluqueria
                        JSONObject objJsonPeluqueria = jsonObject.getJSONObject("peluqueria");
                        Log.i("objJsonPeluqueria", objJsonPeluqueria.toString());

                        Peluqueria objPeluqueria = new Peluqueria(
                                Integer.parseInt(objJsonPeluqueria.getString("idPeluqueria")),
                                objJsonPeluqueria.getString("nombrePeluqueria")+"\n",
                                objJsonPeluqueria.getString("telfPeluqueria")+"\n",
                                objJsonPeluqueria.getString("direccionPeluqueria")+"\n",
                                objJsonPeluqueria.getString("horaInicioPeluqueria")+"\n",
                                objJsonPeluqueria.getString("horaFinPeluqueria")+"\n",
                                objJsonPeluqueria.getString("descripcion")+"\n",
                                //Double.parseDouble(objJsonPeluqueria.getString("latitud")),
                                //Double.parseDouble(objJsonPeluqueria.getString("longitud"))
                                -12.04592,
                                -77.030565
                        );

                        objetoPeluqueria = objPeluqueria;
                        //endregion

                        Servicios servicioObtenido = new Servicios(
                                Integer.parseInt(jsonObject.getString("idServicio")),
                                jsonObject.getString("nombreServicio")+"\n",
                                Double.parseDouble(jsonObject.getString("costoServicio")),
                                jsonObject.getString("descripcionServicio")+"\n",
                                jsonObject.getString("fotoServicio")+"\n",
                                objPeluqueria
                        );

                        txt_direccionPeluqueria.setText(objetoPeluqueria.getDireccionPeluqueria());

                        ImageView llamada = findViewById(R.id.imgLlamada);
                        llamada.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llamar(objetoPeluqueria.getTelfPeluqueria());
                            }
                        });
                        //endregion

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

    private void llamar(String telefonoPeluqueria) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else{
            String numeroLlamar = "tel:" + telefonoPeluqueria;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(numeroLlamar)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Habilitar opciones de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Peluqueria and move the camera
        LatLng centro = new LatLng(objetoPeluqueria.getLatitud(), objetoPeluqueria.getLongitud());
        mMap.addMarker(
                new MarkerOptions().
                        position(centro).
                        title(objetoPeluqueria.getNombrePeluqueria()).
                        icon(getMarkerIcon(getString(R.string.hexColorPrimario))));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(centro));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centro,15));
    }

    //region maneja los colores
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    //endregion



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
