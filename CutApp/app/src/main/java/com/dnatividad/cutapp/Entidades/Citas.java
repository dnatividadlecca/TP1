package com.dnatividad.cutapp.Entidades;

public class Citas {

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

    public Citas(int idCita, String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, Usuarios usuarios_registro, Servicios servicios_registro, String Estado) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        CalificaCita = calificaCita;
        this.comentarioCita = comentarioCita;
        this.usuarios_registro = usuarios_registro;
        this.servicios_registro = servicios_registro;
        this.Estado = Estado;
    }

    public Citas(String fechaCita, String horaCita, Boolean calificaCita, String comentarioCita, Usuarios usuarios_registro, Servicios servicios_registro, String Estado) {
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        CalificaCita = calificaCita;
        this.comentarioCita = comentarioCita;
        this.usuarios_registro = usuarios_registro;
        this.servicios_registro = servicios_registro;
        this.Estado = Estado;
    }

    private int idCita;
    private String fechaCita;
    private String horaCita;
    private Boolean CalificaCita;
    private String comentarioCita;
    private String Estado;

    public String getEstado() {
        return Estado;
    }



    public Usuarios getUsuarios_registro() {
        return usuarios_registro;
    }

    public Servicios getServicios_registro() {
        return servicios_registro;
    }

    private Usuarios usuarios_registro;
    private Servicios servicios_registro;
}
