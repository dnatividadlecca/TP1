package com.dnatividad.cutapp.Utilitarios.General;

import android.content.Context;
import android.widget.Toast;

public class ManejoErrores {

    public void MostrarError(Context contexto, String mensaje){
        Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show();
    }
}
