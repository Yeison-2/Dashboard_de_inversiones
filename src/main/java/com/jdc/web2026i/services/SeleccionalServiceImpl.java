package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.SeleccionalEntity;
import com.jdc.web2026i.repository.SeleccionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeleccionalServiceImpl implements SeleccionalService{

    @Autowired
    private SeleccionalRepository seleccionalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SeleccionalEntity>findAll() {
        return (List<SeleccionalEntity>) seleccionalRepository.findAll();
    }

    @Override
    @Transactional
    public SeleccionalEntity findById(Long id) {
        return seleccionalRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(SeleccionalEntity seleccional) {
        seleccionalRepository.save(seleccional);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        seleccionalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeleccionalEntity> findByIdMayorIgual(Integer idMinimo) {
        return seleccionalRepository.findByIdSeleccionalGreaterThanEqual(idMinimo);
    }

}
