package com.dnatividad.cutapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class EntidadPedido {


    private String imgFoto;
    private String codPed;
    private String fecPed;
    private String codPro;
    private String ProdNam;
    private String ProdIng;
    private String ProdPecio;
    private String codUsua;
    private String usuaName;
    private String usuaApell;
    private String usuaDirec;
    private String usuaTelf;
    private String usuaFecNAc;
    private String prodPorci;
    private String usuaSabor;
    private String pedMensa;
    private String pedInfo;
    private String pedFecEnt;
    private String pedEstado;


    public EntidadPedido(String imgFoto, String codPed, String fecPed, String codPro, String prodNam, String prodIng, String prodPecio, String codUsua, String usuaName, String usuaApell, String usuaDirec, String usuaTelf, String usuaFecNAc, String prodPorci, String usuaSabor, String pedMensa, String pedInfo, String pedFecEnt, String pedEstado) {
        this.imgFoto = imgFoto;
        this.codPed = codPed;
        this.fecPed = fecPed;
        this.codPro = codPro;
        this.ProdNam = prodNam;
        this.ProdIng = prodIng;
        this.ProdPecio = prodPecio;
        this.codUsua = codUsua;
        this.usuaName = usuaName;
        this.usuaApell = usuaApell;
        this.usuaDirec = usuaDirec;
        this.usuaTelf = usuaTelf;
        this.usuaFecNAc = usuaFecNAc;
        this.prodPorci = prodPorci;
        this.usuaSabor = usuaSabor;
        this.pedMensa = pedMensa;
        this.pedInfo = pedInfo;
        this.pedFecEnt = pedFecEnt;
        this.pedEstado = pedEstado;
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

    public String getCodPed() {
        return codPed;
    }

    public String getFecPed() {
        return fecPed;
    }

    public String getCodPro() {
        return codPro;
    }

    public String getProdNam() {
        return ProdNam;
    }

    public String getProdIng() {
        return ProdIng;
    }

    public String getProdPecio() {
        return ProdPecio;
    }

    public String getCodUsua() {
        return codUsua;
    }

    public String getUsuaName() {
        return usuaName;
    }

    public String getUsuaApell() {
        return usuaApell;
    }

    public String getUsuaDirec() {
        return usuaDirec;
    }

    public String getUsuaTelf() {
        return usuaTelf;
    }

    public String getUsuaFecNAc() {
        return usuaFecNAc;
    }

    public String getProdPorci() {
        return prodPorci;
    }

    public String getUsuaSabor() {
        return usuaSabor;
    }

    public String getPedMensa() {
        return pedMensa;
    }

    public String getPedInfo() {
        return pedInfo;
    }

    public String getPedFecEnt() {
        return pedFecEnt;
    }

    public String getPedEstado() {
        return pedEstado;
    }


}
