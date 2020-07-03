package com.taller1.proyecto.service.impl;

import com.taller1.proyecto.dao.ICitaDAO;
import com.taller1.proyecto.model.Cita;
import com.taller1.proyecto.service.ICitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaServiceImpl implements ICitaService {

    @Autowired
    ICitaDAO citaDAO;

    @Override
    public Cita registrar(Cita cita) {
        return citaDAO.save(cita);
    }

    @Override
    public void modificar(Cita cita) {
        citaDAO.save(cita);
    }

    @Override
    public void eliminar(int idCita) {
        citaDAO.deleteById(idCita);
    }

    @Override
    public Cita listarId(int idCita) {
        return citaDAO.findById(idCita).get();
    }

    @Override
    public List<Cita> listar() {
        return citaDAO.findAll();
    }

    @Override
    public List<Cita> buscarPorIdUsuario(int idUsuario) {
        return citaDAO.buscarPorIdUsuario(idUsuario);
    }

    @Override
    public List<Cita> buscarPorFecha(String fechaCita) {
        return citaDAO.buscarPorFecha(fechaCita);
    }

    @Override
    public List<Cita> buscarPorHora(String horaCita) {
        return citaDAO.buscarPorHora(horaCita);
    }
}
