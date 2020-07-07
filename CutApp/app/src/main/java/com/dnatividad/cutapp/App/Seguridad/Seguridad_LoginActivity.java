package com.dnatividad.cutapp.App.Seguridad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import com.dnatividad.cutapp.CatalogoActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Admin_MisCitasActivity;
import com.dnatividad.cutapp.ContactenosActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_MisCitasActivity;
import com.dnatividad.cutapp.MisPedidosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_ListadoServiciosSeleccionarActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.App.Citas.Citas_Cliente_RegistrarCitaActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
import com.dnatividad.cutapp.UbicanosActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.Utilitarios.Entidades.Usuarios;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Seguridad_LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    String urlOrigin;
    CheckBox checkBox;
    TextView txt_correoUsuario, txt_contrasenaUsuario;
    GoogleApiClient googleApiClient;
    String SiteKey = "6Lf2Ku8UAAAAAM0em8iDij9BVHgpeSpavP7VhRox";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad_login);
        setUrlOrigin();

        //En caso se acceda a la pantalla de login, se borran las credenciales si es que aún existen
        SharedPreferences preferences = getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
        preferences.edit().clear().commit();
        checkBox = findViewById(R.id.check_box);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(Seguridad_LoginActivity.this)
                .build();
        googleApiClient.connect();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()){
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully verified",Toast.LENGTH_LONG).show();
                                        checkBox.setTextColor(Color.GREEN);
                                    }
                                }
                            });
                }else {
                    checkBox.setTextColor(Color.BLACK);
                }
            }
        });
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    public void buscar(View v) {
        txt_correoUsuario = (TextView) findViewById(R.id.txt_correoUsuario);
        txt_contrasenaUsuario = (TextView) findViewById(R.id.txt_contrasenaUsuario);
        if (!txt_correoUsuario.getText().toString().isEmpty() && !txt_contrasenaUsuario.getText().toString().isEmpty()) {
            if (checkBox.isChecked()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        URL url = new URL(urlOrigin + "/usuarios/listar-por-credenciales/" + txt_correoUsuario.getText() + '/' + txt_contrasenaUsuario.getText());
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        httpURLConnection.setRequestProperty("Accept", "application/json");

                        int responseCode = httpURLConnection.getResponseCode();
                        String responseMessage = httpURLConnection.getResponseMessage();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            String response = "";
                            String line = "";
                            //Log.i("response","Por ejecutar");

                            while ((line = br.readLine()) != null) {
                                response += line;
                            }
                            //Log.i("response",response);
                            verificarAccesos(response);

                        } else {

                            Log.v("CatalogClient", "Response code:" + responseCode);
                            Log.v("CatalogClient", "Response message:" + responseMessage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error Message",Toast.LENGTH_LONG).show();
                            }
                        });

                    } finally {
                        if (httpURLConnection != null)
                            httpURLConnection.disconnect();
                    }
                }
            });
            runOnUiThread(new Runnable() {
                public void run() {
                    //mostrarErrorLogin(response[0]);
                }
            });

        }
        else{Toast.makeText(Seguridad_LoginActivity.this,"Confirmar que usted no es un robot",Toast.LENGTH_LONG).show();}
        }else{
            Toast.makeText(Seguridad_LoginActivity.this,"Los campos no pueden ser nulos",Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarErrorLogin(String response) {
        Log.i("error", response);
        if(response.equals("true")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.
                    setTitle("Error").
                    setMessage("Usuario y/o contraseña no válidos").
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
    }

    private void verificarAccesos(String response) {

        try{
            JSONObject jsonObject = new JSONObject(response);
            Log.i("jsonObject", String.valueOf(jsonObject));

            Usuarios usuarioLogin = new Usuarios(
                    Integer.parseInt(jsonObject.getString("idUsuario")),
                    jsonObject.getString("nombreUsuario")+"\n",
                    jsonObject.getString("apellidoMaterno")+"\n",
                    jsonObject.getString("apellidoPaterno")+"\n",
                    jsonObject.getString("telefono")+"\n",
                    Integer.parseInt(jsonObject.getString("rolUsuario")),
                    jsonObject.getString("correoUsuario")+"\n",
                    ""+"\n"
            );

            if(usuarioLogin.getIdUser() > 0){
                Boolean rolAdmin;
                if(String.valueOf(usuarioLogin.getRol()).equals("0"))
                    rolAdmin = false;
                else
                    rolAdmin = true;

                guardarPreferencia(rolAdmin, true, usuarioLogin.getIdUser());

                Log.i("rolAdmin", String.valueOf(rolAdmin));
                switch (String.valueOf(rolAdmin)){
                    case "true":
                        CitasCliente();
                        break;
                    case "false":
                        listadoServiciosEscoger();
                        break;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public void guardarPreferencia(Boolean rolAdmin, Boolean sesionIniciada, Integer idUsuario) {

        /*
        if (valor.equals("true")){
            EditText editText1 = (EditText) findViewById(R.id.txt_usuario);

            //Log.i("se almaceno ====>", editText1.getText().toString());
            //Log.i("permiso ====>", valor);
            SharedPreferences prefs = getSharedPreferences("PREFERENCIAS",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sesionIniciada", true);
            editor.putString("CADENA", editText1.getText().toString());
            editor.putString("PERMISO", String.valueOf(Boolean.parseBoolean(rol.toString())));
            editor.commit();
        }
        else{
            EditText editText1 = (EditText) findViewById(R.id.txt_usuario);
            //Log.i("se almaceno ====>", editText1.getText().toString());
            SharedPreferences prefs = getSharedPreferences("PREFERENCIAS",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CADENA", editText1.getText().toString());
            editor.putString("PERMISO", valor);
            editor.commit();
        }
        */
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SESIONINICIADA", String.valueOf(sesionIniciada));
        editor.putString("PERMISOADMIN", String.valueOf(rolAdmin));
        editor.putString("IDUSUSARIO", String.valueOf(idUsuario));
        editor.commit();
    }

    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public void registrar(View v){
        Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
        RegistrarUsuario();
    }

    //metodo para asignar las funciones de las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_login){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_registroUsuarios){
            Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_nosotros){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_contactenos){
            Toast.makeText(this,"Contactenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_ubicanos){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_registroCitas){
            Toast.makeText(this,"Catalogo", Toast.LENGTH_SHORT).show();
            Catalogo();
        }
        else if (id ==R.id.item_misCitas){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            MisPedidos();
        }
        else if (id ==R.id.item_registroServicios){
            Toast.makeText(this,"Reg. Producto", Toast.LENGTH_SHORT).show();
            reg_producto();
        }
        else if (id ==R.id.item_misServicios){
            Toast.makeText(this,"Mis Productos", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        else if (id ==R.id.item_reporteCitas){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            Reportes();
        }
        return super.onOptionsItemSelected(item);
    }

    //region Navegacion
        //Navegacion de los botones del menu
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

        public void Catalogo(){
            Intent Catalogo = new Intent(this, CatalogoActivity.class);
            startActivity(Catalogo);
        }

        public void MisPedidos(){
            Intent mispedidos = new Intent(this, MisPedidosActivity.class);
            startActivity(mispedidos);
        }

        public void reg_producto(){
            Intent producto = new Intent(this, Servicios_Admin_RegistrarServicioActivity.class);
            startActivity(producto);
        }

        public void MisProductos(){
            Intent misproducto = new Intent(this, Servicios_Admin_MisServiciosActivity.class);
            startActivity(misproducto);
        }

        public void Reportes(){
            Intent reporte = new Intent(this, Citas_Cliente_RegistrarCitaActivity.class);
            startActivity(reporte);
        }

        public void MisCitas(){
            Intent miscitas = new Intent(this, Citas_Cliente_MisCitasActivity.class);
            startActivity(miscitas);
        }

        public void CitasCliente(){
            Intent miscitas = new Intent(this, Citas_Admin_MisCitasActivity.class);
            startActivity(miscitas);
        }

        public void reg_citas(){
            Intent registroCitas = new Intent(this, Citas_Cliente_RegistrarCitaActivity.class);
            startActivity(registroCitas);
        }

        public void listadoServiciosEscoger(){
            Intent registroCitas = new Intent(this, Citas_Cliente_ListadoServiciosSeleccionarActivity.class);
            startActivity(registroCitas);
        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //endregion
}
