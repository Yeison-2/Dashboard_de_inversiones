package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.UniversidadEntity;

import java.util.List;

public interface UniversidadService{
    public List<UniversidadEntity> findAll();
    public UniversidadEntity findById(Long id);
    public void save(UniversidadEntity universidad);
    public void delete(Long id);
    public List<UniversidadEntity> findByRector();
    boolean tieneRectorAsociado(Long idUniversidad);


    public List<UniversidadEntity> findByNombreComienzaU(String nombreComienzaU);

    //Interface para el servicio de universidad, con los metodos necesarios para realizar
    // las operaciones CRUD (Create, Read, Update, Delete) en la entidad UniversidadesEntity.
}
