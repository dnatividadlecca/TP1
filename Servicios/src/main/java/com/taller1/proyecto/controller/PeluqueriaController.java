package com.taller1.proyecto.controller;

import com.taller1.proyecto.model.Peluqueria;
import com.taller1.proyecto.service.IPeluqueriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/peluquerias")
public class PeluqueriaController {

    @Autowired
    IPeluqueriaService service;

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Peluqueria> registrar(@RequestBody Peluqueria peluqueria ){
        Peluqueria p = new Peluqueria();
        try {
            p = service.registrar(peluqueria);
        } catch (Exception e) {
            return new ResponseEntity<Peluqueria>(p, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Peluqueria>(p, HttpStatus.OK);
    }

    @GetMapping(value = "/listar", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Peluqueria>> listar(){
        List<Peluqueria> peluquerias = new ArrayList<>();
        try {
            peluquerias = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<List<Peluqueria>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Peluqueria>>(peluquerias,HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{idPeluqueria}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Peluqueria> getPeluqueriaById(@PathVariable("idPeluqueria") Integer idPeluqueria){
        Peluqueria peluqueria = new Peluqueria();
        try {
            peluqueria = service.listarId(idPeluqueria);
        } catch (Exception e) {
            return new ResponseEntity<Peluqueria>(peluqueria, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Peluqueria>(peluqueria, HttpStatus.OK);
    }

    @PutMapping(value = "/actualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> actualizar(@RequestBody Peluqueria peluqueria){
        int resultado = 0;
        try {
            service.modificar(peluqueria);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{idPeluqueria}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> eliminar(@PathVariable Integer idPeluqueria){
        int resultado = 0;
        try {
            service.eliminar(idPeluqueria);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado,HttpStatus.OK);
    }
}
