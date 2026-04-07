package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.TelefonoEntity;

import java.util.List;

public interface TelefonoService {
    List<TelefonoEntity> findAll();
    TelefonoEntity findById(Long id);
    void save(TelefonoEntity telefono);
    void delete(Long id);
    List<TelefonoEntity> findPrimeros8();

}
