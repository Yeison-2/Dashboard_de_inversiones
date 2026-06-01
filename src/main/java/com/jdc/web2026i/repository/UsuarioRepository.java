package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    @EntityGraph(attributePaths = {"rol", "sectorial"})
    Optional<UsuarioEntity> findByUsername(String username);
}
