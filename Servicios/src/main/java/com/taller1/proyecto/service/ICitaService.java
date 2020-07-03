package com.taller1.proyecto.service;

import com.taller1.proyecto.model.Cita;

import java.util.List;

public interface ICitaService {

    Cita registrar(Cita cita);

    void modificar(Cita cita);

    void eliminar(int idCita);

    Cita listarId(int idCita);

    List<Cita> listar();

    List<Cita> buscarPorIdUsuario(int idUsuario);

    List<Cita> buscarPorFecha(String fechaCita);

    List<Cita> buscarPorHora(String horaCita);
}
