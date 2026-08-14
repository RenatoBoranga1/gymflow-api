package com.renatoboranga.gymflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TreinoUpdateRequest(
        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 240, message = "Descrição deve ter no máximo 240 caracteres")
        String descricao,
        @NotNull(message = "Data é obrigatória")
        LocalDate data,
        @NotNull(message = "Plano é obrigatório")
        @Positive(message = "Plano deve ser um identificador positivo")
        Long planoId,
        @NotNull(message = "Professor é obrigatório")
        @Positive(message = "Professor deve ser um identificador positivo")
        Long professorId) {
}
