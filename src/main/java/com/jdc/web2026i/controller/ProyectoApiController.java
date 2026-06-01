package com.jdc.web2026i.controller;

import com.jdc.web2026i.DTO.ProyectoDetalleRespuestaDTO;
import com.jdc.web2026i.services.ProyectoGestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProyectoApiController {

    private final ProyectoGestionService proyectoGestionService;

    public ProyectoApiController(ProyectoGestionService proyectoGestionService) {
        this.proyectoGestionService = proyectoGestionService;
    }

    @GetMapping("/proyectos/{id}/detalle")
    public ResponseEntity<ProyectoDetalleRespuestaDTO> detalle(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(proyectoGestionService.construirDetalle(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
