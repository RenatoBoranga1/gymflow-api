package com.empresa.apiTreino.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Data é obrigatória")
    private String data;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    @NotNull(message = "Plano é obrigatório")
    private Plano plano;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    @NotNull(message = "Professor é obrigatório")
    private Professor professor;

    public Treino() {
    }

    public Treino(Long id, String descricao, String data, Plano plano, Professor professor) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.plano = plano;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treino treino = (Treino) o;
        return Objects.equals(id, treino.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Treino{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", data='" + data + '\'' +
                ", plano=" + plano +
                ", professor=" + professor +
                '}';
    }
}
