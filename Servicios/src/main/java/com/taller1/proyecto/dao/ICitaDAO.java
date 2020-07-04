package com.taller1.proyecto.dao;

import com.taller1.proyecto.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ICitaDAO extends JpaRepository<Cita, Integer> {

    @Query("from Cita c where c.usuario.idUsuario =:idUsuario")
    List<Cita> buscarPorIdUsuario(@Param("idUsuario") int idUsuario);

    @Query("from Cita c where c.fechaCita =:fechaCita")
    List<Cita> buscarPorFecha(@Param("fechaCita") String fechaCita);

    @Query("from Cita c where c.horaCita =:horaCita")
    List<Cita> buscarPorHora(@Param("horaCita") String horaCita);
}
