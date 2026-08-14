package com.renatoboranga.gymflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    protected Professor() {
    }

    public Professor(String nome) {
        atualizar(nome);
    }

    public void atualizar(String nome) {
        this.nome = nome.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
