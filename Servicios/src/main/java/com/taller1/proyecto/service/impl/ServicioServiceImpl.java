package com.taller1.proyecto.service.impl;

import com.taller1.proyecto.dao.IServicioDAO;
import com.taller1.proyecto.model.Servicio;
import com.taller1.proyecto.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioServiceImpl implements IServicioService {

    @Autowired
    IServicioDAO servicioDAO;

    @Override
    public Servicio registrar(Servicio servicio) {
        return servicioDAO.save(servicio);
    }

    @Override
    public void modificar(Servicio servicio) {
        servicioDAO.save(servicio);
    }

    @Override
    public void eliminar(int idServicio) {
        servicioDAO.deleteById(idServicio);
    }

    @Override
    public Servicio listarId(int idServicio) {
        return servicioDAO.findById(idServicio).get();
    }

    @Override
    public List<Servicio> listar() {
        return servicioDAO.findAll();
    }
}
