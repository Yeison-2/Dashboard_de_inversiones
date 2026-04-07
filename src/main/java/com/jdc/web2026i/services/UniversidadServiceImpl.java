package com.jdc.web2026i.services;

import com.jdc.web2026i.entities.SeleccionalEntity;
import com.jdc.web2026i.entities.UniversidadEntity;
import com.jdc.web2026i.repository.SeleccionalRepository;
import com.jdc.web2026i.repository.UniversidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class    UniversidadServiceImpl implements UniversidadService{
    @Autowired
    private UniversidadRepository universidadRepository;
    @Autowired
    private SeleccionalServiceImpl seleccionalServiceImpl;
    @Autowired
    private SeleccionalRepository seleccionalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadEntity>findAll(){
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

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadEntity>findByRector(){return universidadRepository.findByRector();}

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadEntity> findByNombreComienzaU(String nombreComienzaU) {
        return universidadRepository.findByNombreComienza(nombreComienzaU);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean tieneRectorAsociado(Long idUniversidad) {
        UniversidadEntity universidad = universidadRepository.findById(idUniversidad).orElse(null);
        return universidad != null && universidad.getRector() != null;
    }



}
