package com.renatoboranga.gymflow.dto.response;

import java.time.LocalDate;

public record TreinoResponse(
        Long id,
        String descricao,
        LocalDate data,
        Long planoId,
        String planoNome,
        Long professorId,
        String professorNome) {
}
