package com.renatoboranga.gymflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "planos")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "numero_treinos", nullable = false)
    private int numeroTreinos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Plano() {
    }

    public Plano(String nome, int numeroTreinos, Cliente cliente) {
        atualizar(nome, numeroTreinos, cliente);
    }

    public void atualizar(String nome, int numeroTreinos, Cliente cliente) {
        this.nome = nome.trim();
        this.numeroTreinos = numeroTreinos;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNumeroTreinos() {
        return numeroTreinos;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
