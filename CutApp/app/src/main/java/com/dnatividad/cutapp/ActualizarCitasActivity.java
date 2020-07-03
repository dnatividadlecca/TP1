package com.dnatividad.cutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.ManejoMenu.controlMenuOpciones;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import androidx.appcompat.app.AppCompatActivity;

public class ActualizarCitasActivity extends AppCompatActivity {
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

    //Para mostrar la imagen de codigo a bitmap
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_citas);
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
    }

    public void ActualizarPedido(View v){
/*
        TextView act_cod_pedido = (TextView) findViewById(R.id.act_cod_pedido);

        Spinner spinner_estado = (Spinner) findViewById(R.id.spinner_estado);
        final String estado = spinner_estado.getSelectedItem().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_pedido", act_cod_pedido.getText().toString())
                .addFormDataPart("estado", estado)
                .build();
        Log.i("Actualizar ====>", act_cod_pedido.getText().toString());
        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/act_pedidos")
                .url("http://cutapp.atwebpages.com/index.php/act_pedidos")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String cadenaJson = response.body().string();
                    Log.i("Actualizar ====>", estado);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Actualizacion exitosa", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });

        MisPedidos();
        ActualizarCitasActivity.this.finish();
        */
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
/*
        TextView act_cod_pedido = (TextView) findViewById(R.id.act_cod_pedido);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_pedido", act_cod_pedido.getText().toString())
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/anular_pedidos")
                .url("http://cutapp.atwebpages.com/index.php/anular_pedidos")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String cadenaJson = response.body().string();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Anulacion exitosa", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });

        MisPedidos();
        ActualizarCitasActivity.this.finish();
 */
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
                Intent intent = new Intent(getApplicationContext(), MisServiciosActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), CitasTotalClientesActivity.class);
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
