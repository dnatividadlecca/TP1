package com.taller1.proyecto.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idServicio;

    @Column(name = "nomServicio", nullable = false, length = 200)
    private String nombreServicio;

    @Column(name = "costoServicio", nullable = false)
    private Double costoServicio;

    @Column(name = "descServicio", nullable = false, length = 200)
    private String descripcionServicio;

    @Lob
    @Column(name = "fotoServicio", nullable = false, length = 500)
    private String fotoServicio;

    @ManyToOne
    @JoinColumn(name = "id_peluqueria", nullable = false)
    private Peluqueria peluqueria;

    @ManyToMany(mappedBy = "servicios")
    private List<Cita> citas;

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public Double getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(Double costoServicio) {
        this.costoServicio = costoServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getFotoServicio() {
        return fotoServicio;
    }

    public void setFotoServicio(String fotoServicio) {
        this.fotoServicio = fotoServicio;
    }

    public Peluqueria getPeluqueria() {
        return peluqueria;
    }

    public void setPeluqueria(Peluqueria peluqueria) {
        this.peluqueria = peluqueria;
    }
}
