package com.dnatividad.cutapp.Utilitarios.Entidades;

public class Citas_Servicios {
    private Integer idCita;
    private Servicios idServicio;

    public Citas_Servicios(Integer idCita, Servicios idServicio) {
        this.idCita = idCita;
        this.idServicio = idServicio;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public Servicios getIdServicio() {
        return idServicio;
    }
}
