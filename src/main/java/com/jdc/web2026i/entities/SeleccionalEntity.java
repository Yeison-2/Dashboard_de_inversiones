package com.jdc.web2026i.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "seleccionales")
public class SeleccionalEntity implements Serializable {
    @Serial
    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSeleccional")
    private Integer idSeleccional;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "direccion")
    @Size(min = 1, max = 200)
    private String direccion;


    @ManyToOne
    @JoinColumn(name = "idUniversidad", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UniversidadEntity universidad;

    @OneToMany(mappedBy = "seleccional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefonoEntity> telefonos;


    public Integer getIdSeleccional() {
        return idSeleccional;
    }

    public void setIdSeleccional(Integer idSeleccional) {
        this.idSeleccional = idSeleccional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public UniversidadEntity getUniversidad() {
        return universidad;
    }

    public void setUniversidad(UniversidadEntity universidad) {
        this.universidad = universidad;
    }
}
