package com.taller1.proyecto.controller;

import com.taller1.proyecto.model.Cita;
import com.taller1.proyecto.service.ICitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    ICitaService service;

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cita> registrar(@RequestBody Cita cita ){
        Cita c = new Cita();
        try {
            c = service.registrar(cita);
        } catch (Exception e) {
            return new ResponseEntity<Cita>(c, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Cita>(c, HttpStatus.OK);
    }

    @GetMapping(value = "/listar", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cita>> findAll(){
        List<Cita> citas = new ArrayList<>();
        try {
            citas = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<List<Cita>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Cita>>(citas,HttpStatus.OK);
    }

    @GetMapping(value = "/listar-por-id-cita/{idCita}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cita> getCitaById(@PathVariable("idCita") Integer idCita){
        Cita cita = new Cita();
        try {
            cita = service.listarId(idCita);
        } catch (Exception e) {
            return new ResponseEntity<Cita>(cita, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Cita>(cita, HttpStatus.OK);
    }

        @PutMapping(value = "/actualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> actualizar(@RequestBody Cita cita){
        int resultado = 0;
        try {
            service.modificar(cita);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{idCita}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> eliminar(@PathVariable Integer idCita){
        int resultado = 0;
        try {
            service.eliminar(idCita);
            resultado = 1;
        } catch (Exception e) {
            resultado = 0;
        }
        return new ResponseEntity<Integer>(resultado,HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cita>> listar(@PathVariable("idUsuario") Integer idUsuario) {
        List<Cita> citas = new ArrayList<>();
        citas = service.buscarPorIdUsuario(idUsuario);
        return new ResponseEntity<List<Cita>>(citas, HttpStatus.OK);
    }

    @GetMapping(value = "/listar-por-fecha/{fechaCita}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cita>> listarPorFecha(@PathVariable("fechaCita") String fechaCita) {
        List<Cita> citas = new ArrayList<>();
        citas = service.buscarPorFecha(fechaCita);
        return new ResponseEntity<List<Cita>>(citas, HttpStatus.OK);
    }

    @GetMapping(value = "/listar-por-hora/{horaCita}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cita>> listarPorHora(@PathVariable("horaCita") String horaCita) {
        List<Cita> citas = new ArrayList<>();
        citas = service.buscarPorHora(horaCita);
        return new ResponseEntity<List<Cita>>(citas, HttpStatus.OK);
    }
}
