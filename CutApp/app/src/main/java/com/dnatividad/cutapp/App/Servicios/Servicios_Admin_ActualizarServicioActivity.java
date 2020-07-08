package com.dnatividad.cutapp.App.Servicios;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Admin_MisCalificacionesActivity;
import com.dnatividad.cutapp.App.Calificaciones.Calificaciones_Cliente_CitasPorCalificarActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.MainActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Admin_NosotrosEdicionActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.Utilitarios.General.ManejoErrores;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class Servicios_Admin_ActualizarServicioActivity extends AppCompatActivity {
    private  ImageView img_fotoServicio;
    private TextView txt_idServicio;
    private EditText txt_nombreServicio, txt_descripcionServicio, txt_costoServicio;
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    String urlOrigin;
    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_admin_actualizar_servicio);
        setUrlOrigin();
        setReferences();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void setReferences() {
        img_fotoServicio = (ImageView) findViewById(R.id.imgfotoServicio);
        txt_idServicio = (TextView)findViewById(R.id.act_servicio_idServicio);
        txt_nombreServicio = (EditText)findViewById(R.id.act_servicio_nombreServicio);
        txt_descripcionServicio = (EditText)findViewById(R.id.act_servicio_descripcionServicio);
        txt_costoServicio = (EditText)findViewById(R.id.act_servicio_costoServicio);

        String dato_fotoServicio = getIntent().getStringExtra("fotoServicio");
        String datos_idServicio = getIntent().getStringExtra("idServicio");
        String datos_nombreServicio = getIntent().getStringExtra("nombreServicio");
        String datos_descripcionServicio = getIntent().getStringExtra("descripcionServicio");
        String datos_costoServicio = getIntent().getStringExtra("costoServicio");

        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(dato_fotoServicio),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //*************************************************************************************************

        img_fotoServicio.setImageBitmap(bitmap);
        txt_idServicio.setText(datos_idServicio);
        txt_nombreServicio.setText(datos_nombreServicio);
        txt_descripcionServicio.setText(datos_descripcionServicio);
        txt_costoServicio.setText(datos_costoServicio);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void ActualizarProducto(View v){
        updateData();
    }

    public void AnularProducto(View v){
        DialogInterface.OnClickListener confirmacion = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteData();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lbl_confirmacion_eliminar_servicio).setPositiveButton(R.string.lbl_confirmacion_si, confirmacion)
                .setNegativeButton(R.string.lbl_confirmacion_no, confirmacion).show();
    }

    private boolean hayErrores() {
        ManejoErrores manejoErrores = new ManejoErrores();
        String errores = "";
        String saltolinea = "\n";
        if(txt_nombreServicio.getText().toString().equals("")){
            errores += "Falta ingresar su el nombre del servicio" + saltolinea;
        }

        if(txt_descripcionServicio.getText().toString().equals("")){
            errores += "Falta ingresar la descrición del servicio" + saltolinea;
        }

        if(txt_costoServicio.getText().toString().equals("")){
            errores += "Falta ingresar el costo del servicio" + saltolinea;
        }else{
            if(Double.parseDouble(txt_costoServicio.getText().toString()) == 0){
                errores += "El costo no puede ser cero" + saltolinea;
            }
        }

        //Log.i("errores",errores);
        if(errores.equals("")) return false;
        else{
            manejoErrores.MostrarError(this,errores);
            return true;
        }
    }

    void updateData(){
        //region manejo foto
        Bitmap bitmap = ((BitmapDrawable) img_fotoServicio.getDrawable()).getBitmap();
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        //endregion

        img_fotoServicio = (ImageView) findViewById(R.id.imgfotoServicio);
        txt_idServicio = (TextView)findViewById(R.id.act_servicio_idServicio);
        txt_nombreServicio = (EditText)findViewById(R.id.act_servicio_nombreServicio);
        txt_descripcionServicio = (EditText)findViewById(R.id.act_servicio_descripcionServicio);
        txt_costoServicio = (EditText)findViewById(R.id.act_servicio_costoServicio);

        if(!hayErrores()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlOrigin + "/servicios/actualizar");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("idServicio", Integer.parseInt(txt_idServicio.getText().toString()));
                        //Log.i("idServicio", txt_idServicio.getText().toString());
                        jsonObject.put("nombreServicio", txt_nombreServicio.getText().toString());
                        //Log.i("nombreServicio", txt_nombreServicio.getText().toString());
                        jsonObject.put("descripcionServicio", txt_descripcionServicio.getText().toString());
                        //Log.i("descripcionServicio", txt_descripcionServicio.getText().toString());
                        jsonObject.put("costoServicio", txt_costoServicio.getText().toString());
                        //Log.i("costoServicio", txt_costoServicio.getText().toString());
                        jsonObject.put("fotoServicio", fotoEnBase64);
                        Log.i("fotoServicio", fotoEnBase64);

                        JSONObject jsonObjectPeluqueria = new JSONObject();
                        jsonObjectPeluqueria.put("idPeluqueria", "1");

                        jsonObject.put("peluqueria", jsonObjectPeluqueria);
                        //Log.i("peluqueria", jsonObjectPeluqueria.toString());

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

    void deleteData(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin + "/servicios/eliminar/" + txt_idServicio.getText().toString());

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
                Intent intentP = new Intent(getApplicationContext(), Servicios_Admin_MisServiciosActivity.class);
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

    //region Camara
    public void seleccionarImagenDesdeGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ImageView imgFotoPerfil = (ImageView) findViewById(R.id.imgfotoServicio);
            Bitmap img = (Bitmap)data.getExtras().get("data");
            imgFotoPerfil.setImageBitmap(img);

            if (resultCode == MainActivity.RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_IMAGE:
                        try {
                            Uri selectedImage = data.getData();

                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            final Bitmap bmp = BitmapFactory.decodeStream(imageStream);


                            imgFotoPerfil.setImageBitmap(bmp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;


                }
            }
        }
    //endregion
}
