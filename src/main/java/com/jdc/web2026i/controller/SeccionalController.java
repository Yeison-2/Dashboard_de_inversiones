package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.SeleccionalEntity;
import com.jdc.web2026i.entities.UniversidadEntity;
import com.jdc.web2026i.services.SeleccionalService;
import com.jdc.web2026i.services.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class SeccionalController {
    @Autowired
    private SeleccionalService seleccionalService;

    @Autowired
    private UniversidadService universidadService;

    @GetMapping("/listarSeccionales")
    public String listarSeccionales(Model model) {
        model.addAttribute("titulo", "Lista de Seccionales");
        model.addAttribute("seccionales", seleccionalService.findAll());
        return "ListarSeccionales";
    }

    @GetMapping("/Consulta2")
    public String consulta2(Model model) {
        model.addAttribute("titulo", "Consulta 2");
        model.addAttribute("seccionales", seleccionalService.findByIdMayorIgual(6));
        return "Consulta2";
    }


    @GetMapping("/crearSeleccionales")
    public String crearSeleccionales(Model model) {
        model.addAttribute("titulo", "Formulario Seccional");
        model.addAttribute("objSeccional", new SeleccionalEntity());
        model.addAttribute("universidades", universidadService.findAll());
        return "CrearSeccional";
    }

    @PostMapping("/crearSeleccionales")
    public String guardarSeccional(@ModelAttribute("objSeccional") SeleccionalEntity seccional,
                                   @RequestParam("idUniversidad") Long idUniversidad,
                                   SessionStatus status) {
        UniversidadEntity universidad = universidadService.findById(idUniversidad);
        seccional.setUniversidad(universidad);
        seleccionalService.save(seccional);
        status.setComplete();
        return "redirect:/listarSeccionales";
    }

    @GetMapping("/eliminarSeccionalBy/{id}")
    public String eliminarSeccional(@PathVariable("id") Long id) {
        if (id > 0) {
            seleccionalService.delete(id);
        }
        return "redirect:/listarSeccionales";
    }

    @GetMapping("/editarSeccionalBy/{id}")
    public String editarSeccional(@PathVariable("id") Long id, Model model) {
        model.addAttribute("titulo", "Editar Seccional");
        model.addAttribute("objSeccional", seleccionalService.findById(id));
        model.addAttribute("universidades", universidadService.findAll());
        return "EditarSeccional";
    }

    @PostMapping("/editarSeccionalBy/{id}")
    public String guardarEditarSeccional(@PathVariable("id") Long id,
                                         @ModelAttribute("objSeccional") SeleccionalEntity seccional,
                                         @RequestParam("idUniversidad") Long idUniversidad) {
        SeleccionalEntity seccionalExistente = seleccionalService.findById(id);
        seccionalExistente.setNombre(seccional.getNombre());
        seccionalExistente.setDireccion(seccional.getDireccion());

        UniversidadEntity universidad = universidadService.findById(idUniversidad);
        seccionalExistente.setUniversidad(universidad);

        seleccionalService.save(seccionalExistente);
        return "redirect:/listarSeccionales";
    }
}
