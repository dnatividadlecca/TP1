package com.taller1.proyecto.service.impl;

import com.taller1.proyecto.dao.IPeluqueriaDAO;
import com.taller1.proyecto.model.Peluqueria;
import com.taller1.proyecto.service.IPeluqueriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeluqueriaServiceImpl implements IPeluqueriaService {

    @Autowired
    IPeluqueriaDAO peluqueriaDAO;

    @Override
    public Peluqueria registrar(Peluqueria peluqueria) {
        return peluqueriaDAO.save(peluqueria);
    }

    @Override
    public void modificar(Peluqueria peluqueria) {
        peluqueriaDAO.save(peluqueria);
    }

    @Override
    public void eliminar(int idPeluqueria) {
        peluqueriaDAO.deleteById(idPeluqueria);
    }

    @Override
    public Peluqueria listarId(int idPeluqueria) {
        return peluqueriaDAO.findById(idPeluqueria).get();
    }

    @Override
    public List<Peluqueria> listar() {
        return peluqueriaDAO.findAll();
    }
}
