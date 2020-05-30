package com.dnatividad.cutapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class Entidad {

    private String imgFoto;
    private String codigo;
    private String titulo;
    private String ingrediente;
    private String precio;

    public Entidad(String imgFoto, String codigo, String titulo, String ingrediente, String precio){

        this.imgFoto=imgFoto;
        this.codigo=codigo;
        this.titulo=titulo;
        this.ingrediente=ingrediente;
        this.precio=precio;
    }

    //decodifica la imagem y la devuelve como BITMAP
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public Bitmap getImgFoto() {
        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(imgFoto),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public String getCodigo() {
        return codigo;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getContenido() {
        return ingrediente;
    }
    public String getPrecio() { return precio;}


}
