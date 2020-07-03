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
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActualizarServicioActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_actualizar_servicio);
        setUrlOrigin();
        setReferences();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    private void setReferences() {
        img_fotoServicio = (ImageView) findViewById(R.id.imgFotoPerfil);
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

    //decodifica la imagem y la devuelve como BITMAP
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    /*
    public void ActualizarProducto(View v){

        //-------------------------------capturo la imagen-----------------------------------------

        ImageView fotografia= (ImageView) findViewById(R.id.imgFotoPerfil);
        Bitmap bitmap = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        //******************************************************************************************

        TextView act_cod_producto = (TextView) findViewById(R.id.act_servicio_idServicio);
        EditText act_titulo = (EditText) findViewById(R.id.act_servicio_nombreServicio);
        EditText act_descripcion = (EditText) findViewById(R.id.act_servicio_descripcionServicio);
        EditText act_precio = (EditText) findViewById(R.id.act_servicio_costoServicio);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_producto", act_cod_producto.getText().toString())
                .addFormDataPart("titulo", act_titulo.getText().toString())
                .addFormDataPart("ingredientes", act_descripcion.getText().toString())
                .addFormDataPart("precio", act_precio.getText().toString())
                .addFormDataPart("foto", fotoEnBase64)
                .build();


        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/act_productos")
                .url("http://cutapp.atwebpages.com/index.php/act_productos")
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
                            Toast toast= Toast.makeText(getApplicationContext(), "Actualizacion exitosa", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });

        MisProductos();
        ActualizarServicioActivity.this.finish();
    }
    */
    public void ActualizarProducto(View v){
        updateData();
    }

    public void AnularProducto(View v){

        deleteData();
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

        img_fotoServicio = (ImageView) findViewById(R.id.imgFotoPerfil);
        txt_idServicio = (TextView)findViewById(R.id.act_servicio_idServicio);
        txt_nombreServicio = (EditText)findViewById(R.id.act_servicio_nombreServicio);
        txt_descripcionServicio = (EditText)findViewById(R.id.act_servicio_descripcionServicio);
        txt_costoServicio = (EditText)findViewById(R.id.act_servicio_costoServicio);

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

    /*
    void deleteData(){
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
                    //jsonObject.put("fotoServicio", fotoEnBase64);
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
    */
    //region Navegacion
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
            MenuItem item = menu.findItem(R.id.item_6);
            item.setVisible(true);
            MenuItem item2 = menu.findItem(R.id.item_7);
            item2.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.item_7);
            item.setVisible(false);
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_11);
        itemMenuRegistrar.setVisible(true);
        //------------------------------------------------------------------------------------------
        return true;
    }

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
            ImageView imgFotoPerfil = (ImageView) findViewById(R.id.imgFotoPerfil);
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
