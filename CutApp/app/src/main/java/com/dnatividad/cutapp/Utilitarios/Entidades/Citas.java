package com.dnatividad.cutapp.Utilitarios.Entidades;

import java.util.ArrayList;

public class Citas {

    private int idCita;
    private String fechaCita;
    private String horaCita;
    private Boolean CalificaCita;
    private String comentarioCita;
    private String Estado;
    private Usuarios usuarios_registro;
    //private Servicios servicios_registro;

    private ArrayList<Citas_Servicios> lista_servicios;

    //public Citas(int idCita, String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, String Estado, Usuarios usuarios_registro, Servicios servicios_registro, ArrayList<Citas_Servicios> lista_servicios) {
    public Citas(int idCita, String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, String Estado, Usuarios usuarios_registro, ArrayList<Citas_Servicios> lista_servicios) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        CalificaCita = calificaCita;
        this.comentarioCita = comentarioCita;
        this.usuarios_registro = usuarios_registro;
        this.Estado = Estado;
        this.lista_servicios = lista_servicios;
    }

    //public Citas(String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, String Estado, Usuarios usuarios_registro, Servicios servicios_registro, ArrayList<Citas_Servicios> lista_servicios) {
    public Citas(String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, String Estado, Usuarios usuarios_registro, ArrayList<Citas_Servicios> lista_servicios) {
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        CalificaCita = calificaCita;
        this.comentarioCita = comentarioCita;
        this.usuarios_registro = usuarios_registro;
        //this.servicios_registro = servicios_registro;
        this.Estado = Estado;
        this.lista_servicios = lista_servicios;
    }

    public int getIdCita() {
        return idCita;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public Boolean getCalificaCita() {
        return CalificaCita;
    }

    public String getComentarioCita() {
        return comentarioCita;
    }

    public String getEstado() {
        return Estado;
    }

    public Usuarios getUsuarios_registro() {
        return usuarios_registro;
    }

    //public Servicios getServicios_registro() {
        //return servicios_registro;
    //}

    public ArrayList<Citas_Servicios> getLista_servicios() {
        return lista_servicios;
    }
}
