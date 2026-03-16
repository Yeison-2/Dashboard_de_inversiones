package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.RectorEntity;
import com.jdc.web2026i.repository.RectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RectorServiceImpl implements RectorService{

    @Autowired
    private RectorRepository rectorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RectorEntity> findAll() {
        return (List<RectorEntity>) rectorRepository.findAll(); // se obtiene la lista de rectores del repositorio
    }

    @Override
    @Transactional()
    public RectorEntity findById(Long id) {
        return rectorRepository.findById(id).orElse(null); // se obtiene el rector por su id, si no se encuentra se devuelve null
    }

    @Override
    @Transactional()
    public void save(RectorEntity rector) {
        rectorRepository.save(rector); // se guarda el rector en el repositorio
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rectorRepository.deleteById(id); // se elimina el rector por su id
    }

}
