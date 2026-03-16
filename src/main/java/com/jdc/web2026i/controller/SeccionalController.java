package com.jdc.web2026i.controller;

import com.jdc.web2026i.services.SeleccionalService;
import com.jdc.web2026i.services.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeccionalController {
    @Autowired
    private SeleccionalService seleccionalService;

    @GetMapping("listarSeccionales")
    public String listarSeccionales(Model model) {
        model.addAttribute("titulo", "Listar Seccionales");
        model.addAttribute("seccional",seleccionalService.findAll());
        return "ListarSeccionales";

    }


    @GetMapping("crearSeleccionales")
    public String crearSeleccionales(Model model) {
        model.addAttribute("titulo", "Crear Seleccionales");
        return "CrearSeccional";
    }
}
