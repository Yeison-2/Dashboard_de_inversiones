package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.SeleccionalEntity;

import java.util.List;

public interface SeleccionalService {
    public List<SeleccionalEntity> findAll();
    public SeleccionalEntity findById(Long id);
    public void save(SeleccionalEntity rector);
    public void delete(Long id);
    List<SeleccionalEntity> findByIdMayorIgual(Integer idMinimo);

}
