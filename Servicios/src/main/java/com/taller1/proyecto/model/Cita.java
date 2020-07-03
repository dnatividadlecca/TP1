package com.taller1.proyecto.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCita;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    private List<Servicio> servicios;

    @Column(name = "fechaCita", nullable = false, length = 200)
    private String fechaCita;

    @Column(name = "horaCita", nullable = false, length = 200)
    private String horaCita;

    @Column(name = "calificadaCita", nullable = true)
    private int calificadaCita;

    @Column(name = "comentarioCita", nullable = true, length = 500)
    private String comentarioCita;

    @Column(name = "estadoCita", nullable = false, length = 100)
    private String estadoCita;

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getComentarioCita() {
        return comentarioCita;
    }

    public void setComentarioCita(String comentarioCita) {
        this.comentarioCita = comentarioCita;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    public int getCalificadaCita() {
        return calificadaCita;
    }

    public void setCalificadaCita(int calificadaCita) {
        this.calificadaCita = calificadaCita;
    }
}
