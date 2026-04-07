package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.UniversidadEntity;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.jdc.web2026i.services.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UniversidadController {
    @Autowired
    private UniversidadService universidadService;

    @GetMapping("ListarUniversidades")
    public String listarUniversidades(Model model) {
        model.addAttribute("titulo", "Listar Universidades");
        model.addAttribute("universidad",universidadService.findAll());
        return "ListarUniversidades";

    }

    @GetMapping("Consulta3")
    public String consulta3(Model model) {
        model.addAttribute("titulo", "Consulta 3");
        model.addAttribute("universidad",universidadService.findByNombreComienzaU("u"));
        return "consulta3";
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
        return  "redirect:/ListarUniversidades";
    }

    @RequestMapping("/eliminarUniversidadBy/{id}")
    public String eliminarById(@PathVariable(value = "id") Long id,
                               RedirectAttributes redirectAttributes) {

        if (id == null || id <= 0) {
            redirectAttributes.addFlashAttribute("error", "Id de universidad inválido.");
            return "redirect:/ListarUniversidades";
        }

        UniversidadEntity universidad = universidadService.findById(id);
        if (universidad == null) {
            redirectAttributes.addFlashAttribute("error", "La universidad no existe.");
            return "redirect:/ListarUniversidades";
        }

        if (universidadService.tieneRectorAsociado(id)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se puede eliminar la universidad porque tiene un rector asociado."
            );
            return "redirect:/ListarUniversidades";
        }

        universidadService.delete(id);
        redirectAttributes.addFlashAttribute("ok", "Universidad eliminada correctamente.");
        return "redirect:/ListarUniversidades";
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
        return "redirect:/ListarUniversidades";
    }
}
