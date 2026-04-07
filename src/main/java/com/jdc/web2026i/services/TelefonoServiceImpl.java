package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.TelefonoEntity;
import com.jdc.web2026i.repository.TelefonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
public class TelefonoServiceImpl implements TelefonoService {

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TelefonoEntity> findAll() {
        return (List<TelefonoEntity>) telefonoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TelefonoEntity findById(Long id) {
        return telefonoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(TelefonoEntity telefono) {
        telefonoRepository.save(telefono);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        telefonoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelefonoEntity> findPrimeros8() {
        return telefonoRepository.findPrimeros8();
    }



}
