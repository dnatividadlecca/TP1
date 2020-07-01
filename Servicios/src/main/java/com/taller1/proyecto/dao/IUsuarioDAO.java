package com.taller1.proyecto.dao;

import com.taller1.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDAO extends JpaRepository<Usuario, Integer> {
}
