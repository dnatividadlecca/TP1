package com.taller1.proyecto.service;

import com.taller1.proyecto.model.Peluqueria;

import java.util.List;

public interface IPeluqueriaService {

    Peluqueria registrar(Peluqueria peluqueria);

    void modificar(Peluqueria peluqueria);

    void eliminar(int idPeluqueria);

    Peluqueria listarId(int idPeluqueria);

    List<Peluqueria> listar();
}
