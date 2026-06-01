package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {
    Optional<RolEntity> findByNombreRol(String nombreRol);
}
