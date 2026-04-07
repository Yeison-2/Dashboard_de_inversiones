package com.jdc.web2026i.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "seleccionales")
public class SeleccionalEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
}

