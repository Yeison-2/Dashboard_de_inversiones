package com.jdc.web2026i.controller;

import com.jdc.web2026i.DTO.DashboardResumenDTO;
import com.jdc.web2026i.entities.EstadoProyecto;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.MunicipioEntity;
import com.jdc.web2026i.entities.ProyectoEntity;
import com.jdc.web2026i.entities.UsuarioEntity;
import com.jdc.web2026i.repository.MunicipioRepository;
import com.jdc.web2026i.repository.SectorialRepository;
import com.jdc.web2026i.services.DashboardService;
import com.jdc.web2026i.services.ReportePdfService;
import com.jdc.web2026i.services.EmailService;
import com.jdc.web2026i.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.web2026i.DTO.NombreValorDTO;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController {

    private final DashboardService dashboardService;
    private final SectorialRepository sectorialRepository;
    private final MunicipioRepository municipioRepository;
    private final ReportePdfService reportePdfService;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    public AppController(DashboardService dashboardService, SectorialRepository sectorialRepository,
                         MunicipioRepository municipioRepository,
                         ReportePdfService reportePdfService, EmailService emailService, UsuarioRepository usuarioRepository) {
        this.dashboardService = dashboardService;
        this.sectorialRepository = sectorialRepository;
        this.municipioRepository = municipioRepository;
        this.reportePdfService = reportePdfService;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) Integer municipioId,
                            @RequestParam(required = false) Integer sectorialId,
                            @RequestParam(required = false) Integer proyectoId,
                            @RequestParam(required = false) String estado,
                            Authentication authentication,
                            Model model) {
        Integer sectorialEfectiva = sectorialFiltroEfectivo(sectorialId, authentication);
        DashboardResumenDTO resumen = dashboardService.obtenerResumen(municipioId, sectorialEfectiva, proyectoId);
        model.addAttribute("resumen", resumen);
        List<ProyectoEntity> proyectosFiltrados =
                dashboardService.filtrarProyectos(municipioId, sectorialEfectiva, proyectoId, estado);
        List<InversionEntity> inversionesFiltradas =
                dashboardService.filtrarInversiones(municipioId, sectorialEfectiva, proyectoId);
        List<NombreValorDTO> inversionPorMunicipio =
                dashboardService.agruparInversionPorMunicipio(inversionesFiltradas);

        List<ProyectoEntity> proyectosPermitidos =
                dashboardService.filtrarProyectos(null, sectorialEfectiva, null, null);
        List<MunicipioEntity> municipiosVisibles = proyectosPermitidos.stream()
                .map(ProyectoEntity::getMunicipio)
                .filter(m -> m != null)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(MunicipioEntity::getId, m -> m, (a, b) -> a, LinkedHashMap::new),
                        m -> List.copyOf(m.values())));

        model.addAttribute("proyectos", proyectosFiltrados);
        model.addAttribute("inversiones", inversionesFiltradas);
        model.addAttribute("chartMunicipiosJson", toChartMunicipiosJson(inversionPorMunicipio));
        model.addAttribute("municipios", isAdmin(authentication) ? municipioRepository.findAll() : municipiosVisibles);
        model.addAttribute("sectoriales", isAdmin(authentication)
                ? sectorialRepository.findAll()
                : List.of(usuarioActual(authentication).getSectorial()));
        model.addAttribute("proyectosSelect", proyectosPermitidos);
        model.addAttribute("estados", EstadoProyecto.values());

        model.addAttribute("municipioId", municipioId);
        model.addAttribute("sectorialId", sectorialEfectiva);
        model.addAttribute("proyectoId", proyectoId);
        model.addAttribute("estado", estado);
        return "dashboard";
    }

    @GetMapping("/dashboard/pdf")
    public ResponseEntity<byte[]> descargarPdf(@RequestParam(required = false) Integer municipioId,
                                               @RequestParam(required = false) Integer sectorialId,
                                               @RequestParam(required = false) Integer proyectoId,
                                               @RequestParam(required = false) String estado,
                                               Authentication authentication) throws IOException {
        Integer sectorialEfectiva = sectorialFiltroEfectivo(sectorialId, authentication);
        List<ProyectoEntity> proyectos = dashboardService.filtrarProyectos(municipioId, sectorialEfectiva, proyectoId, estado);
        byte[] pdf = reportePdfService.generarInforme(proyectos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("informe-proyectos.pdf").build());
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @PostMapping("/dashboard/notify")
    @ResponseBody
    public ResponseEntity<?> notificarGeneracionReporte(@RequestParam(required = false) Integer municipioId,
                                                        @RequestParam(required = false) Integer sectorialId,
                                                        @RequestParam(required = false) Integer proyectoId,
                                                        @RequestParam(required = false) String estado,
                                                        Authentication authentication) {
        try {
            // determine recipients based on role
            List<String> recipients = List.of();
            String roleLabel = "";
            if (authentication != null) {
                var authorities = authentication.getAuthorities();
                if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"))) {
                    recipients = List.of("djhuertas@jdc.edu.co");
                    roleLabel = "ADMIN";
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO_TIC") || a.getAuthority().equals("USUARIO_TIC"))) {
                    recipients = List.of("ysromero@jdc.edu.co");
                    roleLabel = "USUARIO_TIC";
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO_INFRAESTRUCTURA") || a.getAuthority().equals("USUARIO_INFRAESTRUCTURA"))) {
                    recipients = List.of("moradaelparaiso@gmail.com");
                    roleLabel = "USUARIO_INFRAESTRUCTURA";
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO_AGRICULTURA") || a.getAuthority().equals("USUARIO_AGRICULTURA"))) {
                    recipients = List.of("yeisonstivenromero@gmail.com");
                    roleLabel = "USUARIO_AGRICULTURA";
                }

                if (recipients.isEmpty()) {
                    var opt = usuarioRepository.findByUsername(authentication.getName());
                    if (opt.isPresent() && opt.get().getEmail() != null) {
                        recipients = List.of(opt.get().getEmail());
                    }
                }
            }

            if (recipients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("status", "error", "message", "No se pudo determinar destinatario"));
            }

            String username = authentication != null ? authentication.getName() : "anónimo";
            String subject = "Notificación - Nuevo informe generado";
            String text = String.format("Se ha creado un reporte por el usuario %s con rol %s.", username, roleLabel);

            emailService.sendSimpleText(recipients, subject, text);

            return ResponseEntity.ok(java.util.Map.of("status", "ok", "message", "Correo enviado a: " + String.join(", ", recipients)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("status", "error", "message", "Error enviando correo: " + e.getMessage()));
        }
    }

    private static String toChartMunicipiosJson(List<NombreValorDTO> filas) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < filas.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            NombreValorDTO r = filas.get(i);
            sb.append("{\"nombre\":\"").append(escapeJson(r.nombre())).append("\",\"valor\":");
            sb.append(r.valor() != null ? r.valor().toPlainString() : "0");
            sb.append('}');
        }
        sb.append(']');
        return sb.toString();
    }

    private static String escapeJson(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
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

    private Integer sectorialFiltroEfectivo(Integer sectorialIdSolicitada, Authentication authentication) {
        if (isAdmin(authentication)) {
            return sectorialIdSolicitada;
        }
        UsuarioEntity usuario = usuarioActual(authentication);
        if (usuario.getSectorial() == null) {
            throw new IllegalArgumentException("El usuario no tiene sectorial asignada.");
        }
        return usuario.getSectorial().getId();
    }
}
