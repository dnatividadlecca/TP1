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
import android.provider.MediaStore;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.Utilitarios.ManejoMenu.controlMenuOpciones;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class ContactenosActivity extends AppCompatActivity {

    //Request codes para la camara
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA  = 101;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactenos);
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
        Intent login = new Intent(this, Seguridad_LoginActivity.class);
        startActivity(login);
    }

    public void RegistrarUsuario(){
        Intent registrarusuario = new Intent(this, Seguridad_RegistrarUsuarioActivity.class);
        startActivity(registrarusuario);
    }

    public void Nosotros(){
        Intent nosotros = new Intent(this, Nosotros_Cliente_NosotrosActivity.class);
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
        Intent Catalogo = new Intent(this, Citas_Cliente_ListadoServiciosSeleccionarActivity.class);
        startActivity(Catalogo);
    }

    public void MisCitas(){
        Intent misCitas = new Intent(this, Citas_Cliente_MisCitasActivity.class);
        startActivity(misCitas);
    }

    public void CitasCliente(){
        Intent miscitas = new Intent(this, Citas_Admin_MisCitasActivity.class);
        startActivity(miscitas);
    }

    public void RegistrarServicios(){
        Intent producto = new Intent(this, Servicios_Admin_RegistrarServicioActivity.class);
        startActivity(producto);
    }

    public void MisProductos(){
        Intent misproducto = new Intent(this, Servicios_Admin_MisServiciosActivity.class);
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
        ImageView imgFotoContacto = (ImageView) findViewById(R.id.imgFotoContacto);
        Bitmap img = (Bitmap)data.getExtras().get("data");
        imgFotoContacto.setImageBitmap(img);

        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    try {
                        Uri selectedImage = data.getData();

                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        final Bitmap bmp = BitmapFactory.decodeStream(imageStream);


                        imgFotoContacto.setImageBitmap(bmp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void enviarMail(View view){
        EditText txt_mensaje = findViewById(R.id.txt_mensaje);
        ImageView fotografia= (ImageView) findViewById(R.id.imgFotoContacto);
        Bitmap bitmap = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();

        Log.i("Send email", "");
        String[] TO = {"PCISJNAR@upc.edu.pe"};
        String[] CC = {""};
        Intent envioMail = new Intent(Intent.ACTION_SEND);

        envioMail.setData(Uri.parse("mailto:"));
        envioMail.setType("text/plain");
        envioMail.putExtra(Intent.EXTRA_EMAIL, TO);
        envioMail.putExtra(Intent.EXTRA_CC, CC);
        envioMail.putExtra(Intent.EXTRA_SUBJECT, "Contacto");
        envioMail.putExtra(Intent.EXTRA_TEXT, txt_mensaje.getText());

        Log.i("foto", bitmap.toString());
        if (bitmap != null) {
            Log.i("foto", "1");
            uri = getImageUri(this,bitmap);
            Log.i("foto", "2");
            Log.i("URI", uri.toString());
            Log.i("foto", "3");
            envioMail.putExtra(Intent.EXTRA_STREAM, uri);
        }

        try {
            startActivity(Intent.createChooser(envioMail, "Enviar Correo..."));
            finish();
            Log.i("Correo", "Mensaje Enviado");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
