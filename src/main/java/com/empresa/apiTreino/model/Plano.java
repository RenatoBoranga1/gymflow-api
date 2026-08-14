package com.empresa.apiTreino.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Plano { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do plano é obrigatório")
    private String nomePlano;

    @Positive(message = "Número de treinos deve ser positivo")
    private int numeroDeTreinos;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treino> treinos;

    // Construtor com todos os campos
    public Plano(Long id, String nomePlano, int numeroDeTreinos, Cliente cliente, List<Treino> treinos) {
        this.id = id;
        this.nomePlano = nomePlano;
        this.numeroDeTreinos = numeroDeTreinos;
        this.cliente = cliente;
        this.treinos = treinos;
    }

    public Plano(Long id, String nomePlano, int numeroDeTreinos, Cliente cliente) {
        this.id = id;
        this.nomePlano = nomePlano;
        this.numeroDeTreinos = numeroDeTreinos;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public int getNumeroDeTreinos() {
        return numeroDeTreinos;
    }

    public void setNumeroDeTreinos(int numeroDeTreinos) {
        this.numeroDeTreinos = numeroDeTreinos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Treino> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<Treino> treinos) {
        this.treinos = treinos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plano plano = (Plano) o;
        return Objects.equals(id, plano.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlanoDeTreino{" +
                "id=" + id +
                ", nomePlano='" + nomePlano + '\'' +
                ", numeroDeTreinos=" + numeroDeTreinos +
                ", cliente=" + cliente.getNome() + 
                ", quantidadeDeTreinos=" + (treinos != null ? treinos.size() : 0) + 
                '}';
    }
}
