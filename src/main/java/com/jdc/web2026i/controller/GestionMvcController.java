package com.jdc.web2026i.controller;

import com.jdc.web2026i.entities.EstadoProyecto;
import com.jdc.web2026i.entities.FuenteInversion;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.MunicipioEntity;
import com.jdc.web2026i.entities.ProyectoEntity;
import com.jdc.web2026i.entities.SectorialEntity;
import com.jdc.web2026i.entities.UsuarioEntity;
import com.jdc.web2026i.DTO.InversionesMultiplesDTO;
import com.jdc.web2026i.repository.InversionRepository;
import com.jdc.web2026i.repository.MunicipioRepository;
import com.jdc.web2026i.repository.ProyectoRepository;
import com.jdc.web2026i.repository.SectorialRepository;
import com.jdc.web2026i.repository.UsuarioRepository;
import com.jdc.web2026i.services.ProyectoGestionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gestion")
public class GestionMvcController {

    private final ProyectoGestionService proyectoGestionService;
    private final SectorialRepository sectorialRepository;
    private final MunicipioRepository municipioRepository;
    private final ProyectoRepository proyectoRepository;
    private final InversionRepository inversionRepository;
    private final UsuarioRepository usuarioRepository;

    public GestionMvcController(
            ProyectoGestionService proyectoGestionService,
            SectorialRepository sectorialRepository,
            MunicipioRepository municipioRepository,
            ProyectoRepository proyectoRepository,
            InversionRepository inversionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.proyectoGestionService = proyectoGestionService;
        this.sectorialRepository = sectorialRepository;
        this.municipioRepository = municipioRepository;
        this.proyectoRepository = proyectoRepository;
        this.inversionRepository = inversionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/proyecto")
    public String formularioProyecto(
            @RequestParam(required = false) Integer id,
            Authentication authentication,
            Model model
    ) {
        try {
            Integer sectorialEfectiva = sectorialFiltroEfectivo(authentication);
            boolean esAdmin = isAdmin(authentication);
            List<SectorialEntity> sectoriales = esAdmin
                    ? sectorialRepository.findAll()
                    : List.of(sectorialRepository.findById(sectorialEfectiva)
                    .orElseThrow(() -> new IllegalArgumentException("Sectorial no encontrada")));
            model.addAttribute("sectoriales", sectoriales);
            model.addAttribute("esAdmin", esAdmin);
            model.addAttribute("sectorialIdFija", sectorialEfectiva);
            model.addAttribute("municipios", municipioRepository.findAll());
            model.addAttribute("estados", EstadoProyecto.values());

            ProyectoEntity proyecto;
            if (id == null) {
                proyecto = new ProyectoEntity();
            } else {
                proyecto = proyectoRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
                validarAccesoProyecto(proyecto, authentication);
            }
            model.addAttribute("proyecto", proyecto);
            return "proyecto-form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/proyecto")
    public String guardarProyecto(
            @RequestParam(required = false) Integer id,
            @RequestParam String nombreProyecto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaRegistro,
            @RequestParam String estado,
            @RequestParam String tipo,
            @RequestParam String responsable,
            @RequestParam(required = false) Integer sectorialId,
            @RequestParam Integer municipioId,
            Authentication authentication,
            RedirectAttributes ra
    ) {
        try {
            Integer sectorialIdEfectiva = sectorialId != null ? sectorialId : sectorialFiltroEfectivo(authentication);
            if (sectorialIdEfectiva == null) {
                throw new IllegalArgumentException("Debe seleccionar una sectorial.");
            }
            ProyectoEntity proyectoGuardado = proyectoGestionService.guardarProyecto(
                    id, nombreProyecto, fechaRegistro, estado, tipo, responsable,
                    sectorialIdEfectiva, municipioId);
            if (id == null) {
                String correoDestino = proyectoGestionService.enviarNotificacionProyectoNuevo(proyectoGuardado);
                ra.addFlashAttribute("mensajeOk", "Proyecto creado correctamente. Confirmacion enviada a: " + correoDestino);
            } else {
                ra.addFlashAttribute("mensajeOk", "Proyecto actualizado.");
            }
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/inversion")
    public String formularioInversion(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer proyectoId,
            Authentication authentication,
            RedirectAttributes ra,
            Model model
    ) {
        try {
            Integer sectorialEfectiva = sectorialFiltroEfectivo(authentication);
            List<ProyectoEntity> proyectosVisibles = isAdmin(authentication)
                    ? proyectoRepository.findAll()
                    : proyectoRepository.findBySectorial_Id(sectorialEfectiva);
            model.addAttribute("proyectos", proyectosVisibles);
            model.addAttribute("fuentes", FuenteInversion.values());

            InversionEntity inversion;
            Integer pidResolved = proyectoId;
            if (id != null) {
                inversion = inversionRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Inversion no encontrada"));
                validarAccesoProyecto(inversion.getProyecto(), authentication);
                pidResolved = inversion.getProyecto().getId();
            } else {
                inversion = new InversionEntity();
                if (pidResolved != null) {
                    ProyectoEntity proyecto = proyectoRepository.findById(pidResolved)
                            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
                    validarAccesoProyecto(proyecto, authentication);
                }
            }
            model.addAttribute("inversion", inversion);
            model.addAttribute("proyectoIdSeleccionado", pidResolved);

            if (pidResolved != null) {
                proyectoRepository.findById(pidResolved).ifPresent(p ->
                        model.addAttribute("fechaRegistroProyecto", p.getFechaRegistro()));
            }

            return "inversion-form";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/inversion")
    public String guardarInversion(
            @RequestParam(required = false) Integer id,
            @RequestParam Integer proyectoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInversion,
            @RequestParam(required = false) BigDecimal montoMunicipio,
            @RequestParam(required = false) BigDecimal montoDepartamento,
            @RequestParam(required = false) BigDecimal montoNacion,
            @RequestParam(required = false) BigDecimal montoOtroAportante,
            @RequestParam(required = false) String nombreOtroAportante,
            RedirectAttributes ra
    ) {
        try {
            // Si es edición (id != null), guardar como inversión individual
            if (id != null) {
                // Para edición, usar el método original que solo maneja un tipo a la vez
                BigDecimal monto = BigDecimal.ZERO;
                String fuenteNombre = null;

                if (montoMunicipio != null && montoMunicipio.compareTo(BigDecimal.ZERO) > 0) {
                    monto = montoMunicipio;
                    fuenteNombre = "MUNICIPIO";
                } else if (montoDepartamento != null && montoDepartamento.compareTo(BigDecimal.ZERO) > 0) {
                    monto = montoDepartamento;
                    fuenteNombre = "DEPARTAMENTO";
                } else if (montoNacion != null && montoNacion.compareTo(BigDecimal.ZERO) > 0) {
                    monto = montoNacion;
                    fuenteNombre = "NACION";
                } else if (montoOtroAportante != null && montoOtroAportante.compareTo(BigDecimal.ZERO) > 0) {
                    monto = montoOtroAportante;
                    fuenteNombre = "OTRO_APORTANTE";
                }

                if (fuenteNombre == null) {
                    throw new IllegalArgumentException("Debe ingresar al menos un monto.");
                }

                proyectoGestionService.guardarInversion(id, proyectoId, fechaInversion, fuenteNombre, monto, nombreOtroAportante);
                ra.addFlashAttribute("mensajeOk", "Inversion actualizada.");
            } else {
                // Para crear nuevas inversiones, usar el método de múltiples
                InversionesMultiplesDTO dto = new InversionesMultiplesDTO(
                        proyectoId, fechaInversion, montoMunicipio, montoDepartamento,
                        montoNacion, montoOtroAportante, nombreOtroAportante);
                proyectoGestionService.guardarInversionesMultiples(dto);
                ra.addFlashAttribute("mensajeOk", "Inversiones registradas exitosamente.");
            }
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", e.getMessage());
            return id == null ? "redirect:/gestion/inversion?proyectoId=" + proyectoId
                    : "redirect:/gestion/inversion?id=" + id;
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/inversion-multiple")
    public String formularioInversionMultiple(
            @RequestParam(required = false) Integer proyectoId,
            Authentication authentication,
            RedirectAttributes ra,
            Model model
    ) {
        try {
            Integer sectorialEfectiva = sectorialFiltroEfectivo(authentication);
            List<ProyectoEntity> proyectosVisibles = isAdmin(authentication)
                    ? proyectoRepository.findAll()
                    : proyectoRepository.findBySectorial_Id(sectorialEfectiva);
            model.addAttribute("proyectos", proyectosVisibles);

            ProyectoEntity proyectoSeleccionado = null;
            if (proyectoId != null) {
                proyectoSeleccionado = proyectoRepository.findById(proyectoId)
                        .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
                validarAccesoProyecto(proyectoSeleccionado, authentication);
                model.addAttribute("fechaRegistroProyecto", proyectoSeleccionado.getFechaRegistro());
            }

            model.addAttribute("proyectoIdSeleccionado", proyectoId);
            return "inversion-multiple-form";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/inversion-multiple")
    public String guardarInversionesMultiples(
            @RequestParam Integer proyectoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInversion,
            @RequestParam(required = false) BigDecimal montoMunicipio,
            @RequestParam(required = false) BigDecimal montoDepartamento,
            @RequestParam(required = false) BigDecimal montoNacion,
            @RequestParam(required = false) BigDecimal montoOtroAportante,
            @RequestParam(required = false) String nombreOtroAportante,
            RedirectAttributes ra
    ) {
        try {
            InversionesMultiplesDTO dto = new InversionesMultiplesDTO(
                    proyectoId, fechaInversion, montoMunicipio, montoDepartamento,
                    montoNacion, montoOtroAportante, nombreOtroAportante);
            proyectoGestionService.guardarInversionesMultiples(dto);
            ra.addFlashAttribute("mensajeOk", "Inversiones registradas exitosamente.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/gestion/inversion-multiple?proyectoId=" + proyectoId;
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/inversion/eliminar")
    public String eliminarInversion(@RequestParam Integer id, RedirectAttributes ra) {
        try {
            proyectoGestionService.eliminarInversion(id);
            ra.addFlashAttribute("mensajeOk", "Inversion eliminada.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("mensajeError", "No fue posible eliminar la inversion.");
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/consultas")
    public String consultas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,

            @RequestParam(required = false) Integer municipioConsulta,
            @RequestParam(required = false) FuenteInversion fuenteConsulta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invHasta,

            Authentication authentication,
            Model model
    ) {
        Integer sectorialEfectiva = sectorialFiltroEfectivo(authentication);
        List<ProyectoEntity> proyectosVisibles = isAdmin(authentication)
                ? proyectoRepository.findAll()
                : proyectoRepository.findBySectorial_Id(sectorialEfectiva);
        List<MunicipioEntity> municipiosVisibles = isAdmin(authentication)
                ? municipioRepository.findAll()
                : proyectosVisibles.stream()
                .map(ProyectoEntity::getMunicipio)
                .filter(m -> m != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(m -> m.getId(), m -> m, (a, b) -> a, LinkedHashMap::new),
                        m -> List.copyOf(m.values())));

        model.addAttribute("municipios", municipiosVisibles);
        model.addAttribute("estadosConsulta", EstadoProyecto.values());
        model.addAttribute("fuentes", FuenteInversion.values());

        if (estado != null && !estado.isBlank() && desde != null && hasta != null) {
            try {
                EstadoProyecto est = EstadoProyecto.valueOf(estado.strip().toUpperCase());
                List<ProyectoEntity> resultadosEstado = isAdmin(authentication)
                        ? proyectoRepository.findByEstadoAndFechaRegistroBetween(est, desde, hasta)
                        : proyectoRepository.findByEstadoAndFechaRegistroBetweenAndSectorial_Id(est, desde, hasta, sectorialEfectiva);
                model.addAttribute("consultaEstadoResultados", resultadosEstado);
                model.addAttribute("estadoFiltroSeleccionado", estado.strip().toUpperCase());
                model.addAttribute("consultaEstadoDesde", desde);
                model.addAttribute("consultaEstadoHasta", hasta);
            } catch (IllegalArgumentException e) {
                model.addAttribute("consultaEstadoError", "El estado seleccionado no es valido.");
            }
        }

        if (municipioConsulta != null && fuenteConsulta != null && invDesde != null && invHasta != null) {
            model.addAttribute("consultaInversionPorMunicipio",
                    inversionRepository.findByMunicipioFuenteYFechas(municipioConsulta, fuenteConsulta, sectorialEfectiva, invDesde,
                            invHasta));
            model.addAttribute("municipioConsultaSel", municipioConsulta);
            model.addAttribute("fuenteConsultaSel", fuenteConsulta.name());
            model.addAttribute("invDesdeSel", invDesde);
            model.addAttribute("invHastaSel", invHasta);
        }

        return "consultas";
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private UsuarioEntity usuarioActual(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("No hay usuario autenticado.");
        }
        return usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario autenticado no encontrado."));
    }

    private Integer sectorialFiltroEfectivo(Authentication authentication) {
        if (isAdmin(authentication)) {
            return null;
        }
        UsuarioEntity usuario = usuarioActual(authentication);
        if (usuario.getSectorial() == null) {
            throw new IllegalArgumentException("El usuario no tiene sectorial asignada.");
        }
        return usuario.getSectorial().getId();
    }

    private void validarAccesoProyecto(ProyectoEntity proyecto, Authentication authentication) {
        if (isAdmin(authentication)) {
            return;
        }
        Integer sectorialUsuario = sectorialFiltroEfectivo(authentication);
        if (proyecto.getSectorial() == null || !proyecto.getSectorial().getId().equals(sectorialUsuario)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a este proyecto.");
        }
    }
}

