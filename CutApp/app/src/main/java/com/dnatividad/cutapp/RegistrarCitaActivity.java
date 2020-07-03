package com.dnatividad.cutapp;

import android.content.Context;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrarCitaActivity extends AppCompatActivity {
    private ImageView img_fotoServicio;
    private TextView txt_idServicio, txt_nombreServicio, txt_descripcionServicio;
    String urlOrigin;

    //extraer la fecha del calendario
    CalendarView calendarView;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cita);
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
        insertData();
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
                Intent intent = new Intent(getApplicationContext(), MisServiciosActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                //textViewInvalidData.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent intentP = new Intent(getApplicationContext(), MisCitas.class);
                intentP.putExtra("urlOrigin", urlOrigin);
                startActivity(intentP);
                break;
        }
    }

    //------------------------------------MENU-----------------------------------------------

    //metodo para mostrar y ocultar en menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String permiso = prefs.getString("PERMISO", "");
        Log.i("permiso logueado ====>", permiso.trim());
        //------------------------------------------------------------------------------------------

        //otorgo permiso de acceso a las opciones del menu
        if(permiso.equals("true")){
            //esta linea permite hacer visible un item del menu
            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_6);
            itemMenuCatalogo.setVisible(true);

            MenuItem itemMenuPedidos = menu.findItem(R.id.item_7);
            itemMenuPedidos.setVisible(true);

            MenuItem itemMenuLogin = menu.findItem(R.id.item_1);
            itemMenuLogin.setVisible(false);

            MenuItem itemMenuRegistrar = menu.findItem(R.id.item_2);
            itemMenuRegistrar.setVisible(false);

        }else {
            MenuItem itemMenuPedidos = menu.findItem(R.id.item_7);
            itemMenuPedidos.setVisible(false);

            MenuItem itemMenuLogin = menu.findItem(R.id.item_1);
            itemMenuLogin.setVisible(false);

            MenuItem itemMenuRegistrar = menu.findItem(R.id.item_2);
            itemMenuRegistrar.setVisible(false);

            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_6);
            itemMenuCatalogo.setVisible(true);
        }
        //------------------------------------------------------------------------------------------

        return true;
    }

    //metodo para asignar las funciones de las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_1){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_2){
            Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_3){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_4){
            Toast.makeText(this,"Contactenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_5){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_6){
            Toast.makeText(this,"Catalogo", Toast.LENGTH_SHORT).show();
            Catalogo();
        }
        else if (id ==R.id.item_7){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            MisPedidos();
        }
        else if (id ==R.id.item_8){
            Toast.makeText(this,"Reg. Producto", Toast.LENGTH_SHORT).show();
            reg_producto();
        }
        else if (id ==R.id.item_9){
            Toast.makeText(this,"Mis Productos", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        return super.onOptionsItemSelected(item);

    }


    //Navegacion de los botones del menu
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
    public void Catalogo(){
        Intent Catalogo = new Intent(this, CatalogoActivity.class);
        startActivity(Catalogo);
    }
    public void MisPedidos(){
        Intent mispedidos = new Intent(this, MisPedidosActivity.class);
        startActivity(mispedidos);
    }
    public void reg_producto(){
        Intent producto = new Intent(this, RegistrarServicioActivity.class);
        startActivity(producto);
    }
    public void MisProductos(){
        Intent misproducto = new Intent(this, MisServiciosActivity.class);
        startActivity(misproducto);
    }
}
