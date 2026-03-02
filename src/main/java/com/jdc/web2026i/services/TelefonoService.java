package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.TelefonoEntity;

import java.util.List;

public interface  TelefonoService{
    public List<TelefonoEntity> finAll();
    public TelefonoEntity findById(Long id);
    public void save(TelefonoEntity universidad);
    public void delete(Long id);


}
