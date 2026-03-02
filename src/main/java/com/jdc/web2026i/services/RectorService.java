package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.RectorEntity;

import java.util.List;

public interface RectorService {
    public List<RectorEntity> findAll();
    public RectorEntity findById(Long id);
    public void save(RectorEntity rector);
    public void delete(Long id);
}
