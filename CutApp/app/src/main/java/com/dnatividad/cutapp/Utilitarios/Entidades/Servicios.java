package com.dnatividad.cutapp.Utilitarios.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.dnatividad.cutapp.Utilitarios.Entidades.Peluqueria;

public class Servicios {
    private int idServicio;
    private String nombreServicio;
    private Double costoServicio;
    private String descripcionServicio;
    private String fotoServicio;
    private Peluqueria peluqueria;

    public Servicios(int idServicio,
                     String nombreServicio,
                     Double costoServicio,
                     String descripcionServicio,
                     String fotoServicio,
                     Peluqueria peluqueria){

        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.costoServicio = costoServicio;
        this.descripcionServicio = descripcionServicio;
        this.fotoServicio = fotoServicio;
        this.peluqueria = peluqueria;
    }

    public Servicios(   int idServicio,
                        String nombreServicio,
                        Double costoServicio,
                        String descripcionServicio,
                        String fotoServicio){

        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.costoServicio = costoServicio;
        this.descripcionServicio = descripcionServicio;
        this.fotoServicio = fotoServicio;
    }

    public Bitmap getImgFoto() {
        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(fotoServicio),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public Double getCostoServicio() {
        return costoServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public String getFotoServicio() {
        return fotoServicio;
    }

    public Peluqueria getPeluqueria() {
        return peluqueria;
    }
}
