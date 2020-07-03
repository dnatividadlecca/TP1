package com.dnatividad.cutapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //En caso se acceda a la pantalla de login, se borran las credenciales si es que aún existen
        SharedPreferences preferences = getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    public void buscar(View v){

        /*
        EditText user_log = (EditText) findViewById(R.id.txt_usuario);
        EditText pass_log = (EditText) findViewById(R.id.txt_passwordUsuario);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/usuarios/"+user_log.getText().toString()+"/"+pass_log.getText().toString())
                .url("http://cutapp.atwebpages.com/index.php/usuarios/"+user_log.getText().toString()+"/"+pass_log.getText().toString())
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
                    Log.i("RESPUESTA ====>", cadenaJson);

                    try {
                        JSONArray resp= new JSONArray(cadenaJson);

                        if ((resp == null) && (resp.equals(" "))){
                            // Log.i("no entro ====>", cadenaJson);
                        }
                        else{
                            for(int index = 0;index < resp.length(); index++) {
                                JSONObject jsonObject = resp.getJSONObject(index);
                                Boolean ok = jsonObject.getBoolean("success");
                                String valor = jsonObject.getString("cod_usuario");
                                String valor2 = jsonObject.getString("password");
                                String valor3 = jsonObject.getString("permiso");
                                //obtengo los valores del JSONArray y lo convierto a aun jsonObject para poder obtener sus valores
                                Log.i("tiene permiso====>", valor3);


                                if (ok== true){

                                    guardarPreferencia(valor3);// como esto le doy permiso al usuario
                                    if(valor3 == "0") //admin
                                        Catalogo();
                                    else
                                        MisCitas();

                                    LoginActivity.this.finish();
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         */
        Boolean rolAdmin = true;
        Boolean sesionIniciada = true;
        guardarPreferencia(rolAdmin, sesionIniciada, 7);

        if(rolAdmin)
            CitasCliente();
        else
            listadoServiciosEscoger();
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

        public void Reportes(){
            Intent reporte = new Intent(this, RegistrarCitaActivity.class);
            startActivity(reporte);
        }

        public void MisCitas(){
            Intent miscitas = new Intent(this, MisCitas.class);
            startActivity(miscitas);
        }

        public void CitasCliente(){
            Intent miscitas = new Intent(this, CitasTotalClientesActivity.class);
            startActivity(miscitas);
        }

        public void reg_citas(){
            Intent registroCitas = new Intent(this, RegistrarCitaActivity.class);
            startActivity(registroCitas);
        }

        public void listadoServiciosEscoger(){
            Intent registroCitas = new Intent(this, MisServiciosClienteActivity.class);
            startActivity(registroCitas);
        }
    //endregion
}
