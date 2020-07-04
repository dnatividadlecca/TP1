package com.taller1.proyecto.service;

import com.taller1.proyecto.model.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario registrar(Usuario usuario);

    void modificar(Usuario usuario);

    void eliminar(int idUsuario);

    Usuario listarId(int idUsuario);

    List<Usuario> listar();

    Usuario buscarPorCredenciales(String correoUsuario, String password);
}
