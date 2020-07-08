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
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
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


public class Servicios_Admin_RegistrarServicioActivity extends AppCompatActivity {

    EditText reg_nombreServicio, reg_descripcionServicio, reg_costoServicio;
    ImageView fotografia;
    //Request codes para la camara
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    private Bitmap fotografiaOriginal;

    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUrlOrigin();
        setContentView(R.layout.activity_servicios_admin_registrar_servicio);

        fotografia = (ImageView) findViewById(R.id.imgfotoServicio);

        fotografiaOriginal =((BitmapDrawable) fotografia.getDrawable()).getBitmap();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    public void Reg_producto(View v){
        registrarServicio();
    }

    private void setReferences() {
        reg_nombreServicio = (EditText) findViewById(R.id.reg_nombreServicio);
        reg_descripcionServicio = (EditText) findViewById(R.id.reg_descripcionServicio);
        reg_costoServicio = (EditText) findViewById(R.id.reg_costoServicio);
        fotografia = (ImageView) findViewById(R.id.imgfotoServicio);
    }

    private void registrarServicio() {
        setReferences();

        Bitmap bitmap = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        insertData();
    }

    private boolean hayErrores() {
        ManejoErrores manejoErrores = new ManejoErrores();
        String errores = "";
        String saltolinea = "\n";
        if(reg_nombreServicio.getText().toString().equals("")){
            errores += "Falta ingresar su el nombre del servicio" + saltolinea;
        }

        if(reg_descripcionServicio.getText().toString().equals("")){
            errores += "Falta ingresar la descrición del servicio" + saltolinea;
        }

        if(reg_costoServicio.getText().toString().equals("")){
            errores += "Falta ingresar el costo del servicio" + saltolinea;
        }else{
            if(Double.parseDouble(reg_costoServicio.getText().toString()) == 0){
                errores += "El costo no puede ser cero" + saltolinea;
            }
        }

        Bitmap fotografiaNueva = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();
        if(fotografiaNueva == fotografiaOriginal){
            errores += "Falta ingresar la imagen del servicio" + saltolinea;
        }

        //Log.i("errores",errores);
        if(errores.equals("")) return false;
        else{
            manejoErrores.MostrarError(this,errores);
            return true;
        }
    }

    void insertData(){
        if(!hayErrores()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlOrigin + "/servicios/registrar");

                        JSONObject jsonObject = new JSONObject();
                        //jsonObject.put("id", getIntent().getStringExtra("id"));

                        jsonObject.put("nombreServicio", reg_nombreServicio.getText().toString());
                        //Log.i("nombreServicio", reg_nombreServicio.getText().toString());
                        jsonObject.put("descripcionServicio", reg_descripcionServicio.getText().toString());
                        //Log.i("descripcionServicio", reg_descripcionServicio.getText().toString());
                        jsonObject.put("costoServicio", reg_costoServicio.getText().toString());
                        //Log.i("costoServicio", reg_costoServicio.getText().toString());
                        jsonObject.put("fotoServicio", fotoEnBase64);
                        //Log.i("fotoServicio", fotoEnBase64);

                        JSONObject jsonObjectPeluqueria = new JSONObject();
                        jsonObjectPeluqueria.put("idPeluqueria", "1");

                        jsonObject.put("peluqueria", jsonObjectPeluqueria);
                        //Log.i("peluqueria", jsonObjectPeluqueria.toString());

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

    //region funciones de camara
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
