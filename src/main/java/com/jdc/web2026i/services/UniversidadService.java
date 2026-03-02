package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.UniversidadEntity;

import java.util.List;

public interface UniversidadService{
    public List<UniversidadEntity> finAll();
    public UniversidadEntity findById(Long id);
    public void save(UniversidadEntity universidad);
    public void delete(Long id);

    //Interface para el servicio de universidad, con los metodos necesarios para realizar
    // las operaciones CRUD (Create, Read, Update, Delete) en la entidad UniversidadesEntity.
}
