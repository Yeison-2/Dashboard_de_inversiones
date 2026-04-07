package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.SeleccionalEntity;
import com.jdc.web2026i.entities.TelefonoEntity;
import com.jdc.web2026i.services.SeleccionalService;
import com.jdc.web2026i.services.TelefonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class TelefonoController {

    @Autowired
    private TelefonoService telefonoService;

    @Autowired
    private SeleccionalService seleccionalService;

    @GetMapping("/ListarTelefonos")
    public String listarTelefonos(Model model) {
        model.addAttribute("titulo", "Lista de Telefonos");
        model.addAttribute("telefonos", telefonoService.findAll());
        return "ListarTelefonos";
    }

    @GetMapping("/CrearTelefono")
    public String crearTelefono(Model model) {
        model.addAttribute("titulo", "Formulario Telefono");
        model.addAttribute("objTelefono", new TelefonoEntity());
        model.addAttribute("seccionales", seleccionalService.findAll());
        return "CrearTelefono";
    }

    @GetMapping("/Consulta4")
    public String consulta4(Model model) {
        model.addAttribute("titulo", "Consulta 4");
        model.addAttribute("telefonos", telefonoService.findPrimeros8());
        return "Consulta4";
    }


    @PostMapping("/CrearTelefono")
    public String guardarTelefono(@ModelAttribute("objTelefono") TelefonoEntity telefono,
                                  @RequestParam("idSeccional") Long idSeccional,
                                  SessionStatus status,
                                  Model model) {
        SeleccionalEntity seccional = seleccionalService.findById(idSeccional);
        if (seccional == null) {
            model.addAttribute("titulo", "Formulario Telefono");
            model.addAttribute("seccionales", seleccionalService.findAll());
            model.addAttribute("error", "La seccional seleccionada no existe.");
            return "CrearTelefono";
        }

        telefono.setSeleccional(seccional);
        telefono.setUniversidad(seccional.getUniversidad());
        telefonoService.save(telefono);
        status.setComplete();
        return "redirect:/ListarTelefonos";
    }

    @GetMapping("/EliminarTelefonoBy/{id}")
    public String eliminarTelefonoBy(@PathVariable("id") Long id) {
        if (id > 0) {
            telefonoService.delete(id);
        }
        return "redirect:/ListarTelefonos";
    }

    @GetMapping("/EditarTelefonoBy/{id}")
    public String editarTelefono(@PathVariable("id") Long id, Model model) {
        model.addAttribute("titulo", "Editar Telefono");
        model.addAttribute("objTelefono", telefonoService.findById(id));
        model.addAttribute("seccionales", seleccionalService.findAll());
        return "EditarTelefono";
    }

    @PostMapping("/EditarTelefonoBy/{id}")
    public String guardarEditarTelefono(@PathVariable("id") Long id,
                                        @ModelAttribute("objTelefono") TelefonoEntity telefono,
                                        @RequestParam("idSeccional") Long idSeccional,
                                        Model model) {
        TelefonoEntity telefonoExistente = telefonoService.findById(id);
        SeleccionalEntity seccional = seleccionalService.findById(idSeccional);

        if (telefonoExistente == null || seccional == null) {
            model.addAttribute("titulo", "Editar Telefono");
            model.addAttribute("objTelefono", telefonoExistente != null ? telefonoExistente : telefono);
            model.addAttribute("seccionales", seleccionalService.findAll());
            model.addAttribute("error", "No fue posible actualizar el telefono por datos invalidos.");
            return "EditarTelefono";
        }

        telefonoExistente.setNumero(telefono.getNumero());
        telefonoExistente.setTipo(telefono.getTipo());
        telefonoExistente.setSeleccional(seccional);
        telefonoExistente.setUniversidad(seccional.getUniversidad());

        telefonoService.save(telefonoExistente);
        return "redirect:/ListarTelefonos";
    }
}
