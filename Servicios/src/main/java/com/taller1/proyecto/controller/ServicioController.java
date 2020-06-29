package com.taller1.proyecto.controller;

import com.taller1.proyecto.model.Servicio;
import com.taller1.proyecto.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    IServicioService service;

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Servicio> registrar(@RequestBody Servicio servicio ){
        Servicio s = new Servicio();
        try {
            s = service.registrar(servicio);
        } catch (Exception e) {
            return new ResponseEntity<Servicio>(s, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Servicio>(s, HttpStatus.OK);
    }

    @GetMapping(value = "/listar", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Servicio>> listar(){
        List<Servicio> servicios = new ArrayList<>();
        try {
            servicios = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<List<Servicio>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Servicio>>(servicios,HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{idServicio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Servicio> getServicioById(@PathVariable("idServicio") Integer idServicio){
        Servicio servicio = new Servicio();
        try {
            servicio = service.listarId(idServicio);
        } catch (Exception e) {
            return new ResponseEntity<Servicio>(servicio, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @PutMapping(value = "/actualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> actualizar(@RequestBody Servicio servicio){
        int resultado = 0;
        try {
            service.modificar(servicio);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{idServicio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> eliminar(@PathVariable Integer idServicio){
        int resultado = 0;
        try {
            service.eliminar(idServicio);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado,HttpStatus.OK);
    }

}
