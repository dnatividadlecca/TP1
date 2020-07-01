package com.taller1.proyecto.controller;

import com.taller1.proyecto.model.Usuario;
import com.taller1.proyecto.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    IUsuarioService service;

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario ){
        Usuario u = new Usuario();
        try {
            u = service.registrar(usuario);
        } catch (Exception e) {
            return new ResponseEntity<Usuario>(u, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Usuario>(u, HttpStatus.OK);
    }

    @GetMapping(value = "/listar", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = new ArrayList<>();
        try {
            usuarios = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<List<Usuario>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable("idUsuario") Integer idUsuario){
        Usuario usuario = new Usuario();
        try {
            usuario = service.listarId(idUsuario);
        } catch (Exception e) {
            return new ResponseEntity<Usuario>(usuario, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    @PutMapping(value = "/actualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> actualizar(@RequestBody Usuario usuario){
        int resultado = 0;
        try {
            service.modificar(usuario);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> eliminar(@PathVariable Integer idUsuario){
        int resultado = 0;
        try {
            service.eliminar(idUsuario);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado,HttpStatus.OK);
    }
}
