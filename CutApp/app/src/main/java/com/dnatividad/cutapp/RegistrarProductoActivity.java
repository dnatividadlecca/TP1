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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistrarProductoActivity extends AppCompatActivity {

    //Request codes para la camara
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;

    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

     /*   if (ContextCompat.checkSelfPermission(RegistrarProductoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegistrarProductoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistrarProductoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    */
        ObtenerCodigoProducto();
    }

    //*************************Obtener Codigo Producto Incrementado en 1 *********************************************
    public void ObtenerCodigoProducto(){
        final TextView codproducto=findViewById(R.id.reg_prod_etiq_txt_cod);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/obtenerUltimoCodProducto")
                .url("http://cutapp.atwebpages.com/index.php/obtenerUltimoCodProducto")
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
                    //Log.i("COD Producto ====>", cadenaJson);

                    try {
                        JSONArray resp= new JSONArray(cadenaJson);

                        if ((resp == null) && (resp.equals(" "))){
                            // Log.i("no entro ====>", cadenaJson);
                        }
                        else{
                            String valor="";
                            for(int index = 0;index < resp.length(); index++) {
                                JSONObject jsonObject = resp.getJSONObject(index);
                                valor = jsonObject.getString("cod_producto");
                                valor=AunmentarUnoMasCodigo(valor);
                                //obtengo los valores del JSONArray y lo convierto a aun jsonObject para poder obtener el de la consulta
                            }
                            codproducto.setText(valor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    //obtengo el valor de la consulta con el ultimo codigo de pedido y le aumento en uno para el nuevo producto
    public String AunmentarUnoMasCodigo(String cadena){
        String numCuenta = cadena;
        int valor=Integer.parseInt(numCuenta)+1;
        cadena=valor+"".trim();
        return cadena;
    }
    //**********************************************************************************************



    public void Reg_producto(View v){
        TextView reg_prod_cod = (TextView) findViewById(R.id.reg_prod_etiq_txt_cod);
        EditText reg_prod_titulo = (EditText) findViewById(R.id.reg_prod_etiq_txt_titulo);
        EditText reg_prod_ingre = (EditText) findViewById(R.id.reg_prod_etiq_txt_descripcion);
        EditText reg_prod_precio = (EditText) findViewById(R.id.reg_prod_etiq_txt_precio);

        //-------------------------------capturo la imagen-----------------------------------------

        ImageView fotografia= (ImageView) findViewById(R.id.imgFotoPerfil);
        Bitmap bitmap = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        //*****************************************************************************************
        // capto el tag que tiene el imageview imageview es el nombre que llevaran todas las imagenes
      /*  String backgroundImageName = String.valueOf(fotografia.getTag());
        Log.i("Aqui==============>",backgroundImageName);
        foto = Environment.getExternalStorageDirectory() + "/"+backgroundImageName +".jpg";
        Log.i("Aqui==============>",foto);
       */
        //*****************************************************************************************

        //*****************************************************************************************
        //lo insertamos en un formulario y se envia al php
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_producto", reg_prod_cod.getText().toString())
                .addFormDataPart("titulo", reg_prod_titulo.getText().toString())
                .addFormDataPart("ingredientes", reg_prod_ingre.getText().toString())
                .addFormDataPart("precio", reg_prod_precio.getText().toString())
                .addFormDataPart("foto", fotoEnBase64)
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/reg_productos")
                .url("http://cutapp.atwebpages.com/index.php/reg_productos")
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
                    Log.i("====>", cadenaJson);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });



        // Borramos todos los campos
        reg_prod_cod.setText("");
        reg_prod_titulo.setText("");
        reg_prod_ingre.setText("");
        reg_prod_precio.setText("");
        fotografia.setImageResource(R.drawable.preview);
        ObtenerCodigoProducto();


        //***************************************recuperar funciona*********************************
        //Recuperar Imagen - insertamos la imagen recuperado al imageview 2 como prueba
        //Bitmap img=base64ToBitmap(fotoEnBase64);
        //Log.i("Aqui==============>",fotoEnBase64.toString());
        //ImageView imgs= (ImageView) findViewById(R.id.imgFotoPerfil2);
        //imgs.setImageBitmap(img);
        //******************************************************************************************
    }

    //***********************************recuperar funciona*****************************************
    //Recuperar Imagen - convierte el codigo base64 a imagen****************************************
    //private Bitmap base64ToBitmap(String b64) {
    //    byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
    //    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    //}

    //**********************************************************************************************




    //********************************************** CAMARA ****************************************
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



    //*****************************************************************************************************************************

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
        Intent producto = new Intent(this, RegistrarProductoActivity.class);
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
}
