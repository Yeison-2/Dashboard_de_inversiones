package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.RectorEntity;
import com.jdc.web2026i.entities.UniversidadEntity;
import com.jdc.web2026i.services.RectorService;
import com.jdc.web2026i.services.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class rectorController {

    @Autowired private RectorService rectorService;

    @Autowired private UniversidadService universidadService;

    @GetMapping("/ListarRector")
    public String listarRector(Model model) {
        model.addAttribute("titulo", "Lista de Rectores");
        model.addAttribute("rectores", rectorService.findAll());
        return "ListarRector";
    }

    @GetMapping("/Consulta1")
    public String consulta1(Model model) {
        model.addAttribute("titulo", "Consulta 1 - Rectores TI");
        model.addAttribute("rectores", rectorService.findByDocumentoIdentidad("TI"));
        return "Consulta1";
    }



    @GetMapping("/CrearRector")
    public String crearRector(Model model) {
        model.addAttribute("titulo", "Formulario Rector");
        model.addAttribute("objRector", new RectorEntity());
        model.addAttribute("universidades", universidadService.findAll());
        return "CrearRector";
    }

    @PostMapping("/CrearRector")
    public String guardarRector(@ModelAttribute("objRector") RectorEntity rector,
                                @RequestParam("idUniversidad") Long idUniversidad,
                                SessionStatus status) {
        UniversidadEntity universidad = universidadService.findById(idUniversidad);
        rector.setUniversidad(universidad);
        rectorService.save(rector);
        status.setComplete();
        return "redirect:/ListarRector";
    }

    @GetMapping("/EliminarRectorBy/{id}")
    public String eliminarRectorBy(@PathVariable("id") Long id) {
        if (id >0) {
            rectorService.delete(id);
        }
        return "redirect:/ListarRector";
    }

    @GetMapping("/EditarRectorBy/{id}")
    public String editarRector(@PathVariable("id") Long id, Model model) {
        model.addAttribute("titulo", "Editar Rector");
        model.addAttribute("objRector", rectorService.findById(id));
        model.addAttribute("universidades", universidadService.findAll());
        return "EditarRector";
    }

    @PostMapping("/EditarRectorBy/{id}")
    public String guardarEditarRector(@PathVariable("id") Long id,
                                      @ModelAttribute("objRector") RectorEntity rector,
                                      @RequestParam("idUniversidad") Long idUniversidad) {
        RectorEntity rectorExistente = rectorService.findById(id);
        rectorExistente.setNombre(rector.getNombre());
        rectorExistente.setPrimerapellido(rector.getPrimerapellido());
        rectorExistente.setSegundoapellido(rector.getSegundoapellido());
        rectorExistente.setNum_documento(rector.getNum_documento());
        rectorExistente.setTipo(rector.getTipo());
        rectorExistente.setFechaNacimiento(rector.getFechaNacimiento());
        UniversidadEntity universidad = universidadService.findById(idUniversidad);
        rectorExistente.setUniversidad(universidad);
        rectorService.save(rectorExistente);
        return "redirect:/ListarRector";
    }
}
