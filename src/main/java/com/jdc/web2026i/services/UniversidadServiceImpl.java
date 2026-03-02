package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.UniversidadEntity;
import com.jdc.web2026i.repository.UniversidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UniversidadServiceImpl implements UniversidadService{
    @Autowired
    private UniversidadRepository universidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadEntity>finAll(){
        return (List<UniversidadEntity>) universidadRepository.findAll();
    }

    @Override
    @Transactional
    public UniversidadEntity findById(Long id){
        return universidadRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(UniversidadEntity universidad) {
        universidadRepository.save(universidad);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        universidadRepository.deleteById(id);
    }


}
