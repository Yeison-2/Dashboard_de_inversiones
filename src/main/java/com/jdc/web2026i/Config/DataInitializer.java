package com.jdc.web2026i.Config;

import com.jdc.web2026i.entities.EstadoProyecto;
import com.jdc.web2026i.entities.FuenteInversion;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.MunicipioEntity;
import com.jdc.web2026i.entities.ProyectoEntity;
import com.jdc.web2026i.entities.RolEntity;
import com.jdc.web2026i.entities.SectorialEntity;
import com.jdc.web2026i.entities.UsuarioEntity;
import com.jdc.web2026i.repository.InversionRepository;
import com.jdc.web2026i.repository.MunicipioRepository;
import com.jdc.web2026i.repository.ProyectoRepository;
import com.jdc.web2026i.repository.RolRepository;
import com.jdc.web2026i.repository.SectorialRepository;
import com.jdc.web2026i.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final SectorialRepository sectorialRepository;
    private final MunicipioRepository municipioRepository;
    private final ProyectoRepository proyectoRepository;
    private final InversionRepository inversionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                           SectorialRepository sectorialRepository, MunicipioRepository municipioRepository,
                           ProyectoRepository proyectoRepository, InversionRepository inversionRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.sectorialRepository = sectorialRepository;
        this.municipioRepository = municipioRepository;
        this.proyectoRepository = proyectoRepository;
        this.inversionRepository = inversionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!rolRepository.findAll().isEmpty()) {
            return;
        }

        RolEntity admin = crearRol("ADMIN");
        RolEntity tic = crearRol("USUARIO_TIC");
        RolEntity infra = crearRol("USUARIO_INFRAESTRUCTURA");
        RolEntity agro = crearRol("USUARIO_AGRICULTURA");

        SectorialEntity ticSectorial = crearSectorial("TIC");
        SectorialEntity infraestructura = crearSectorial("INFRAESTRUCTURA");
        SectorialEntity agricultura = crearSectorial("AGRICULTURA");

        crearUsuario("Administrador", "admin", "admin123", "admin@inventario.com", admin, null);
        crearUsuario("Usuario TIC", "usertic", "tic123", "tic@inventario.com", tic, ticSectorial);
        crearUsuario("Usuario Infra", "userinfra", "infra123", "infra@inventario.com", infra, infraestructura);
        crearUsuario("Usuario Agro", "useragro", "agro123", "agro123@inventario.com", agro, agricultura);

        MunicipioEntity duitama = crearMunicipio("Duitama", "Boyaca");
        MunicipioEntity sogamoso = crearMunicipio("Sogamoso", "Boyaca");
        MunicipioEntity tunja = crearMunicipio("Tunja", "Boyaca");

        ProyectoEntity proyecto1 = crearProyecto("Placa Huellas Vereda Norte", infraestructura, duitama, "Carlos Mejia",
                "Placa huellas", EstadoProyecto.EJECUCION);
        ProyectoEntity proyecto2 = crearProyecto("Centro TIC Municipal", ticSectorial, sogamoso, "Laura Perez",
                "Centro digital", EstadoProyecto.FORMULACION);
        ProyectoEntity proyecto3 = crearProyecto("Riego asociativo sur", agricultura, tunja, "Ana Diaz",
                "Agronomia riego", EstadoProyecto.EJECUCION);

        crearInversion(proyecto1, LocalDate.now().minusDays(20), FuenteInversion.MUNICIPIO, new BigDecimal("20000000"));
        crearInversion(proyecto1, LocalDate.now().minusDays(8), FuenteInversion.DEPARTAMENTO, new BigDecimal("18500000"));
        crearInversion(proyecto1, LocalDate.now().minusDays(5), FuenteInversion.NACION, new BigDecimal("20000000"));
        crearInversion(proyecto2, LocalDate.now().minusDays(12), FuenteInversion.MUNICIPIO, new BigDecimal("12000000"));
        crearInversion(proyecto2, LocalDate.now().minusDays(2), FuenteInversion.NACION, new BigDecimal("8700000"));
        crearInversion(proyecto3, LocalDate.now().minusDays(15), FuenteInversion.MUNICIPIO, new BigDecimal("9500000"));
        crearInversion(proyecto3, LocalDate.now().minusDays(9), FuenteInversion.OTRO_APORTANTE, new BigDecimal("5500000"));
        crearInversion(proyecto3, LocalDate.now().minusDays(4), FuenteInversion.DEPARTAMENTO, new BigDecimal("11250000"));

    }

    private RolEntity crearRol(String nombre) {
        RolEntity rol = new RolEntity();
        rol.setNombreRol(nombre);
        return rolRepository.save(rol);
    }

    private SectorialEntity crearSectorial(String nombre) {
        SectorialEntity sectorial = new SectorialEntity();
        sectorial.setNombre(nombre);
        return sectorialRepository.save(sectorial);
    }

    private MunicipioEntity crearMunicipio(String nombre, String departamento) {
        MunicipioEntity municipio = new MunicipioEntity();
        municipio.setNombre(nombre);
        municipio.setDepartamento(departamento);
        return municipioRepository.save(municipio);
    }

    private ProyectoEntity crearProyecto(String nombre, SectorialEntity sectorial, MunicipioEntity municipio,
                                         String responsable, String tipo, EstadoProyecto estado) {
        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setNombreProyecto(nombre);
        proyecto.setFechaRegistro(LocalDate.now().minusDays(90));
        proyecto.setEstado(estado);
        proyecto.setTipo(tipo);
        proyecto.setResponsable(responsable);
        proyecto.setSectorial(sectorial);
        proyecto.setMunicipio(municipio);
        return proyectoRepository.save(proyecto);
    }

    private void crearUsuario(String nombre, String username, String plainPassword, String email,
                              RolEntity rol, SectorialEntity sectorial) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(plainPassword));
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setSectorial(sectorial);
        usuarioRepository.save(usuario);
    }

    private void crearInversion(ProyectoEntity proyecto, LocalDate fecha, FuenteInversion fuente, BigDecimal monto) {
        InversionEntity inversion = new InversionEntity();
        inversion.setProyecto(proyecto);
        inversion.setFechaInversion(fecha);
        inversion.setFuenteInversion(fuente);
        inversion.setMonto(monto);
        inversionRepository.save(inversion);
    }
}
