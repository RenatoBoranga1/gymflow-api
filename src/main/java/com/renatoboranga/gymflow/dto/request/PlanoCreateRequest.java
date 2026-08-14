package com.renatoboranga.gymflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PlanoCreateRequest(
        @NotBlank(message = "Nome do plano é obrigatório")
        @Size(max = 120, message = "Nome do plano deve ter no máximo 120 caracteres")
        String nome,
        @Positive(message = "Número de treinos deve ser positivo")
        int numeroTreinos,
        @NotNull(message = "Cliente é obrigatório")
        @Positive(message = "Cliente deve ser um identificador positivo")
        Long clienteId) {
}
