package com.dnatividad.cutapp.Utilitarios.Entidades;

public class Peluqueria {
    private int idPeluqueria;
    private String nombrePeluqueria;
    private String telfPeluqueria;
    private String direccionPeluqueria;
    private String horaInicioPeluqueria;
    private String horaFinPeluqueria;
    private String descripcion;
    private Double latitud;
    private Double longitud;

    public Peluqueria(  int idPeluqueria,
                        String nombrePeluqueria,
                        String telfPeluqueria,
                        String direccionPeluqueria,
                        String horaInicioPeluqueria,
                        String horaFinPeluqueria,
                        String descripcion,
                        Double latitud,
                        Double longitud){

        this.idPeluqueria = idPeluqueria;
        this.nombrePeluqueria = nombrePeluqueria;
        this.telfPeluqueria = telfPeluqueria;
        this.direccionPeluqueria = direccionPeluqueria;
        this.horaInicioPeluqueria = horaInicioPeluqueria;
        this.horaFinPeluqueria = horaFinPeluqueria;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdPeluqueria() {
        return idPeluqueria;
    }

    public String getNombrePeluqueria() {
        return nombrePeluqueria;
    }

    public String getTelfPeluqueria() {
        return telfPeluqueria;
    }

    public String getDireccionPeluqueria() {
        return direccionPeluqueria;
    }

    public String getHoraInicioPeluqueria() {
        return horaInicioPeluqueria;
    }

    public String getHoraFinPeluqueria() {
        return horaFinPeluqueria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
}
