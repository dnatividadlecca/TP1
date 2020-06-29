package com.taller1.proyecto.service;

import com.taller1.proyecto.model.Servicio;

import java.util.List;

public interface IServicioService {

    Servicio registrar(Servicio servicio);

    void modificar(Servicio servicio);

    void eliminar(int idServicio);

    Servicio listarId(int idServicio);

    List<Servicio> listar();

}
