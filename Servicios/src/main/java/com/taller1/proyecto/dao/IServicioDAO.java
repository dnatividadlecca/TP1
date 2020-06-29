package com.taller1.proyecto.dao;

import com.taller1.proyecto.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IServicioDAO extends JpaRepository<Servicio, Integer> {

}
