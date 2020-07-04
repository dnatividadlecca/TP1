package com.taller1.proyecto.dao;

import com.taller1.proyecto.model.Cita;
import com.taller1.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Integer> {

    Usuario findByNombreUsuario(String nombreUsuario);

    @Query("from Usuario us where us.correoUsuario = :correoUsuario and us.password=:password")
    Usuario buscarPorCredenciales(@Param("correoUsuario") String correoUsuario, @Param("password") String password);
}