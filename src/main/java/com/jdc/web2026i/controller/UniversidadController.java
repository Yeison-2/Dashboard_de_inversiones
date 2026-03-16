package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.UniversidadEntity;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.jdc.web2026i.services.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class UniversidadController {
    @Autowired
    private UniversidadService universidadService;

    @GetMapping("listarUniversidades")
    public String listarUniversidades(Model model) {
        model.addAttribute("titulo", "Listar Universidades");
        model.addAttribute("universidad",universidadService.findAll());
        return "listarUniversidades";

    }

    @GetMapping("crearUniversidades")
    public String crearUniversidad(Model model) {
        model.addAttribute("titulo", "CrearUniversidad");
        model.addAttribute("objuniversidad", new UniversidadEntity());
        return "CrearUniversidad";
    }

    @PostMapping(value = "/crearUniversidad")
    public String guardarUniversidad(@Valid UniversidadEntity universidad, SessionStatus status) {
        universidad.setEstado(true);
        universidadService.save(universidad);
        status.setComplete();
        return  "redirect:/listarUniversidades";
    }

    @RequestMapping("/eliminarUniversidadBy/{id}")
    public String eliminarById(@PathVariable (value = "id")Long id) {
        if (id > 0) {
            universidadService.delete(id);
        }
        return "redirect:/listarUniversidades";
    }

    @GetMapping("/editarUniversidadBy/{id}")
    public String editarUniversidad(@PathVariable (value = "id")Long id, Model model) {
         model.addAttribute("titulo", "Actualizar Universidad");
         model.addAttribute("universidadActualizar", universidadService.findById(id));
         return "editarUniversidad";
    }

    @PostMapping("/editarUniversidadBy/{id}")
    public String guardarEditarUniversidad(@PathVariable(value = "id") Long id,
                                           @ModelAttribute("universidadActualizar") UniversidadEntity universidad) {
        UniversidadEntity universidadExistente = universidadService.findById(id);
        universidadExistente.setNombre(universidad.getNombre());
        universidadExistente.setNit(universidad.getNit());
        universidadExistente.setEstado(true);
        universidadExistente.setDescripcion(universidad.getDescripcion());
        universidadService.save(universidadExistente);
        return "redirect:/listarUniversidades";
    }
}
