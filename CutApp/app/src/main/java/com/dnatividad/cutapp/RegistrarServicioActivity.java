package com.dnatividad.cutapp;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;


public class RegistrarServicioActivity extends AppCompatActivity {

    EditText reg_nombreServicio, reg_descripcionServicio, reg_costoServicio;
    ImageView fotografia;
    //Request codes para la camara
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;

    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUrlOrigin();
        setContentView(R.layout.activity_registrar_servicio);
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

    void insertData(){
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
                    Log.i("fotoServicio", fotoEnBase64);

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
                Intent intentP = new Intent(getApplicationContext(), MisServiciosActivity.class);
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
            MenuItem itemMenuMisProd = menu.findItem(R.id.item_9);
            itemMenuMisProd.setVisible(true);
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
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_11);
        itemMenuRegistrar.setVisible(true);
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
        else if (id ==R.id.item_11){
            //Toast.makeText(this,"Cerrar Sesión", Toast.LENGTH_SHORT).show();
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);
    }


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
