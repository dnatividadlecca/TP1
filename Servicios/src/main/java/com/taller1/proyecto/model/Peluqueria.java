package com.taller1.proyecto.model;

import javax.persistence.*;

@Entity
@Table(name = "peluquerias")
public class Peluqueria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPeluqueria;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombrePeluqueria;

    @Column(name = "telefono", nullable = false, length = 18)
    private String telfPeluqueria;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccionPeluqueria;

    @Column(name = "horaInicio", nullable = false)
    private String horaInicioPeluqueria;

    @Column(name = "horaFin", nullable = false)
    private String horaFinPeluqueria;

    @Column(name = "descripcion", nullable = true, length = 500)
    private String descripcion;

    private Double latitud;

    private Double longitud;

    public int getIdPeluqueria() {
        return idPeluqueria;
    }

    public void setIdPeluqueria(int idPeluqueria) {
        this.idPeluqueria = idPeluqueria;
    }

    public String getNombrePeluqueria() {
        return nombrePeluqueria;
    }

    public void setNombrePeluqueria(String nombrePeluqueria) {
        this.nombrePeluqueria = nombrePeluqueria;
    }

    public String getTelfPeluqueria() {
        return telfPeluqueria;
    }

    public void setTelfPeluqueria(String telfPeluqueria) {
        this.telfPeluqueria = telfPeluqueria;
    }

    public String getDireccionPeluqueria() {
        return direccionPeluqueria;
    }

    public void setDireccionPeluqueria(String direccionPeluqueria) {
        this.direccionPeluqueria = direccionPeluqueria;
    }

    public String getHoraInicioPeluqueria() {
        return horaInicioPeluqueria;
    }

    public void setHoraInicioPeluqueria(String horaInicioPeluqueria) {
        this.horaInicioPeluqueria = horaInicioPeluqueria;
    }

    public String getHoraFinPeluqueria() {
        return horaFinPeluqueria;
    }

    public void setHoraFinPeluqueria(String horaFinPeluqueria) {
        this.horaFinPeluqueria = horaFinPeluqueria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
