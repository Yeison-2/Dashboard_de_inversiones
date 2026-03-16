package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.TelefonoEntity;
import com.jdc.web2026i.entities.TelefonoEntity;
import com.jdc.web2026i.repository.TelefonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TelefonoServiceImpl implements TelefonoService{

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TelefonoEntity> finAll() {
        return (List<TelefonoEntity>) telefonoRepository.findAll(); // se obtiene la lista de rectores del repositorio
    }

    @Override
    @Transactional()
    public TelefonoEntity findById(Long id) {
        return telefonoRepository.findById(id).orElse(null); // se obtiene el rector por su id, si no se encuentra se devuelve null
    }

    @Override
    @Transactional()
    public void save(TelefonoEntity rector) {
        telefonoRepository.save(rector); // se guarda el rector en el repositorio
    }

    @Override
    @Transactional
    public void delete(Long id) {
        telefonoRepository.deleteById(id); // se elimina el rector por su id
    }
}
