package com.taller1.proyecto.service.impl;

import com.taller1.proyecto.dao.IUsuarioDAO;
import com.taller1.proyecto.model.Usuario;
import com.taller1.proyecto.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    IUsuarioDAO usuarioDAO;

    @Override
    public Usuario registrar(Usuario usuario) {
        return usuarioDAO.save(usuario);
    }

    @Override
    public void modificar(Usuario usuario) {
        usuarioDAO.save(usuario);
    }

    @Override
    public void eliminar(int idUsuario) {
        usuarioDAO.deleteById(idUsuario);
    }

    @Override
    public Usuario listarId(int idUsuario) {
        return usuarioDAO.findById(idUsuario).get();
    }

    @Override
    public List<Usuario> listar() {
        return usuarioDAO.findAll();
    }

    @Override
    public Usuario buscarPorCredenciales(String correoUsuario, String password) {
        return usuarioDAO.buscarPorCredenciales(correoUsuario, password);
    }
}
