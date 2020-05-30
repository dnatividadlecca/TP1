package com.dnatividad.cutapp;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistrarUsuarioActivity extends AppCompatActivity {
    DatePicker simpleDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
    }

    //************************************************************************************
    public void Reg_usuario(View v){
        EditText reg_user = (EditText) findViewById(R.id.txt_us_usuario);
        EditText reg_passw = (EditText) findViewById(R.id.txt_clave);
        EditText reg_nombre = (EditText) findViewById(R.id.txt_nombre);
        EditText reg_apellido = (EditText) findViewById(R.id.txt_apellido);
        EditText reg_direccion = (EditText) findViewById(R.id.txt_direccion);
        EditText reg_celular = (EditText) findViewById(R.id.txt_celular);


        //extraer el valor del datepicker para seleccionar la fecha de nacimiento
        DatePicker dtp_fecha;
        TextView lbl_nacimiento;
        int dia,mes,ano;
        dtp_fecha=(DatePicker)findViewById(R.id.datePicker1);
        //insertamos el valor del calendario en un label
        lbl_nacimiento=(TextView)findViewById(R.id.lbl_nacimiento);
        dia=dtp_fecha.getDayOfMonth();
        mes=dtp_fecha.getMonth()+1;
        ano=dtp_fecha.getYear();
        lbl_nacimiento.setText(ano+"/"+mes+"/"+dia);


        //lo insertamos en un formulario
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_usuario", reg_user.getText().toString())
                .addFormDataPart("password", reg_passw.getText().toString())
                .addFormDataPart("nombre", reg_nombre.getText().toString())
                .addFormDataPart("apellido", reg_apellido.getText().toString())
                .addFormDataPart("direccion", reg_direccion.getText().toString())
                .addFormDataPart("telefono", reg_celular.getText().toString())
                .addFormDataPart("fecha_nacimiento",lbl_nacimiento.getText().toString() )
                .addFormDataPart("permiso","false")
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/reg_usuarios")
                .url("http://cutapp.atwebpages.com/index.php/reg_usuarios")
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

        //nombreProducto.setText("");
        //precioProducto.setText("");




    }


    //metodo para mostrar y ocultar en menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
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
            Toast.makeText(this,"Realizar Pedido", Toast.LENGTH_SHORT).show();
            Pedido();
        }    return super.onOptionsItemSelected(item);
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
    public void Pedido(){
        Intent Pedido = new Intent(this, PedidosActivity.class);
        startActivity(Pedido);
    }

}
