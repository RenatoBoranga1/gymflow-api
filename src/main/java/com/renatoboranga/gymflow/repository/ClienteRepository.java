package com.renatoboranga.gymflow.repository;

import com.renatoboranga.gymflow.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Cliente> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Cliente> findByNomeContainingIgnoreCaseAndEmailContainingIgnoreCase(
            String nome, String email, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
