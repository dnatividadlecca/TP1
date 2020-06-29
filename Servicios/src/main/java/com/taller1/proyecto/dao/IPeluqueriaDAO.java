package com.taller1.proyecto.dao;

import com.taller1.proyecto.model.Peluqueria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPeluqueriaDAO extends JpaRepository<Peluqueria, Integer> {
}
