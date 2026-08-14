package com.renatoboranga.gymflow.repository;

import com.renatoboranga.gymflow.model.Plano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoRepository extends JpaRepository<Plano, Long> {

    Page<Plano> findByClienteId(Long clienteId, Pageable pageable);
}
