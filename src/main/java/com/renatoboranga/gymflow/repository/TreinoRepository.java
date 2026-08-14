package com.renatoboranga.gymflow.repository;

import com.renatoboranga.gymflow.model.Treino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreinoRepository extends JpaRepository<Treino, Long> {

    Page<Treino> findByPlanoId(Long planoId, Pageable pageable);
}
